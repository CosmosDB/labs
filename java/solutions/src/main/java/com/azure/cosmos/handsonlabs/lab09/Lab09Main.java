// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.lab09;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javafaker.Animal;
import com.github.javafaker.Faker;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.azure.cosmos.ConnectionPolicy;
import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.handsonlabs.common.datatypes.Family;
import com.azure.cosmos.handsonlabs.common.datatypes.Member;
import com.azure.cosmos.handsonlabs.common.datatypes.Person;
import com.azure.cosmos.handsonlabs.common.datatypes.PurchaseFoodOrBeverage;
import com.azure.cosmos.handsonlabs.common.datatypes.Transaction;
import com.azure.cosmos.handsonlabs.common.datatypes.ViewMap;
import com.azure.cosmos.handsonlabs.common.datatypes.WatchLiveTelevisionChannel;
import com.azure.cosmos.models.CosmosAsyncItemResponse;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.FeedOptions;
import com.azure.cosmos.models.IndexingMode;
import com.azure.cosmos.models.IndexingPolicy;
import com.azure.cosmos.models.PartitionKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.azure.cosmos.models.IncludedPath;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

public class Lab09Main {
    protected static Logger logger = LoggerFactory.getLogger(Lab09Main.class.getSimpleName());
    private static ObjectMapper mapper = new ObjectMapper();
    private static String endpointUri = "<your uri>";
    private static String primaryKey = "<your key>";   
    private static CosmosAsyncDatabase database;
    private static CosmosAsyncContainer peopleContainer;  
    private static CosmosAsyncContainer transactionContainer;      
    public static void main(String[] args) {
        ConnectionPolicy defaultPolicy = ConnectionPolicy.getDefaultPolicy();
        defaultPolicy.setPreferredLocations(Lists.newArrayList("West US 2"));

        CosmosAsyncClient client = new CosmosClientBuilder()
                .setEndpoint(endpointUri)
                .setKey(primaryKey)
                .setConnectionPolicy(defaultPolicy)
                .setConsistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildAsyncClient();

        database = client.getDatabase("FinancialDatabase");
        peopleContainer = database.getContainer("PeopleCollection");
        transactionContainer = database.getContainer("TransactionCollection");

        Person person = new Person(); 
        CosmosAsyncItemResponse<Person> response = peopleContainer.createItem(person).block();

        logger.info("First item insert: {} RUs", response.getRequestCharge());

        
        List<Person> children = new ArrayList<Person>();
        for (int i=0; i<4; i++) children.add(new Person());
        Member member = new Member(UUID.randomUUID().toString(),
                                   new Person(), // accountHolder
                                   new Family(new Person(), // spouse
                                              children)); // children

        CosmosAsyncItemResponse<Member> response2 = peopleContainer.createItem(member).block();

        logger.info("Second item insert: {} RUs", response2.getRequestCharge());

        List<Transaction> transactions = new ArrayList<Transaction>();
        for (int i=0; i<100; i++) transactions.add(new Transaction());

        /**
         * Although this block of code uses Async API to insert Cosmos DB docs into a container,
         * we are blocking on each createItem call, so this implementation is effectively Sync.
         * We will not get enough throughput to saturate 400 RU/s with this approach.
                 
        for (Transaction transaction : transactions) {
            CosmosAsyncItemResponse<Transaction> result = transactionContainer.createItem(transaction).block();
            logger.info("Item Created {}", result.getItem().getId());
        }

        */

        /** Try this truly asynchronous use of createItem. You will see it can generate much more throughput to Azure Cosmos DB. */

        Flux<Transaction> transactionsFlux = Flux.fromIterable(transactions);
        List<CosmosAsyncItemResponse<Transaction>> results = 
            transactionsFlux.flatMap(interaction -> {
                return transactionContainer.createItem(interaction);
        })
        .collectList()
        .block();

        results.forEach(result -> logger.info("Item Created\t{}",result.getItem().getId()));

        int maxItemCount = 1000;
        int maxDegreeOfParallelism = -1;
        int maxBufferedItemCount = -1;

        FeedOptions options = new FeedOptions();
        options.setMaxItemCount(maxItemCount);
        options.setMaxBufferedItemCount(maxBufferedItemCount);
        options.setMaxDegreeOfParallelism(maxDegreeOfParallelism);

        logger.info("\n\n" +
                    "MaxItemCount:\t{}\n" +
                    "MaxDegreeOfParallelism:\t{}\n" +
                    "MaxBufferedItemCount:\t{}" + 
                    "\n\n",
                    maxItemCount, maxDegreeOfParallelism, maxBufferedItemCount);

        String sql = "SELECT * FROM c WHERE c.processed = true ORDER BY c.amount DESC";                    

        StopWatch timer = StopWatch.createStarted();

        transactionContainer.queryItems(sql, options, Transaction.class)
                .byPage()
                .flatMap(page -> {
                //Don't do anything with the query page results
                return Mono.empty();
        }).blockLast();

        timer.stop();

        logger.info("\n\nElapsed Time:\t{}s\n\n", ((double)timer.getTime(TimeUnit.MILLISECONDS))/1000.0);

        String sqlItemQuery = "SELECT TOP 1 * FROM c WHERE c.id = 'example.document'";                    

        FeedOptions optionsItemQuery = new FeedOptions();

        peopleContainer.queryItems(sqlItemQuery, optionsItemQuery, Member.class)
                .byPage()
                .next()
                .flatMap(page -> {
                logger.info("\n\n" +
                            "{} RUs for\n" +
                            "{}" + 
                            "\n\n",
                            page.getRequestCharge(),
                            page.getElements().iterator().next());
                return Mono.empty();
        }).block();

        peopleContainer.readItem("example.document", new PartitionKey("<LastName>"), Member.class)
        .flatMap(pointReadResponse -> {
           int expectedWritesPerSec = 200;
           int expectedReadsPerSec = 800;
           logger.info("\n\n{} RUs\n\n",pointReadResponse.getRequestCharge());
           logger.info("\n\nEstimated load: {} RU per sec\n\n",
                response.getRequestCharge() * expectedReadsPerSec + response.getRequestCharge() * expectedWritesPerSec);           
           return Mono.empty();
        }).block();
        
        Member memberItem = new Member();
        CosmosAsyncItemResponse<Member> createResponse = peopleContainer.createItem(memberItem).block();
        logger.info("{} RUs", createResponse.getRequestCharge());

        int throughput = peopleContainer.readProvisionedThroughput().block();
        logger.info("{} RU per sec", throughput);
        peopleContainer.replaceProvisionedThroughput(1000).block();
        
        client.close();        
    }
}