// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.lab09;

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
import com.azure.cosmos.handsonlabs.common.datatypes.ViewMap;
import com.azure.cosmos.handsonlabs.common.datatypes.WatchLiveTelevisionChannel;
import com.azure.cosmos.models.CosmosAsyncItemResponse;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.IndexingMode;
import com.azure.cosmos.models.IndexingPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.azure.cosmos.models.IncludedPath;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
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

        try {

            System.out.println("\n\n" + mapper.writeValueAsString(new Person()) + "\n\n");

        } catch (Exception ex) {
            logger.error("Failed JSON.",ex);
        }


        client.close();        
    }
}