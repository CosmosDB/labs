package com.azure.cosmos.handsonlabs.lab01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.javafaker.Faker;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.azure.cosmos.ConnectionPolicy;
import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.handsonlabs.common.datatypes.PurchaseFoodOrBeverage;
import com.azure.cosmos.handsonlabs.common.datatypes.ViewMap;
import com.azure.cosmos.handsonlabs.common.datatypes.WatchLiveTelevisionChannel;
import com.azure.cosmos.models.CosmosAsyncItemResponse;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.IndexingMode;
import com.azure.cosmos.models.IndexingPolicy;
import com.azure.cosmos.models.IncludedPath;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import com.google.common.collect.Lists;

public class Lab01Main {
    protected static Logger logger = LoggerFactory.getLogger(Lab01Main.class.getSimpleName());
    private static String endpointUri = "https://testdb3152.documents.azure.com:443/";
    private static String primaryKey = "BzrbjviV0weDwekxKbvFyKqaEZHbSAxOg8qS56xeKkAyquIP8tS6o0XIjpee7v3Sf7BtmKEufKN175RIX1a2NQ==";   
    private static CosmosAsyncDatabase targetDatabase;
    private static CosmosAsyncContainer customContainer;
    private static AtomicBoolean resourcesCreated = new AtomicBoolean(false);     
    public static void main(String[] args) {
        ConnectionPolicy defaultPolicy = ConnectionPolicy.getDefaultPolicy();
        defaultPolicy.setPreferredLocations(Lists.newArrayList("West US"));
    
        CosmosAsyncClient client = new CosmosClientBuilder()
                .setEndpoint(endpointUri)
                .setKey(primaryKey)
                .setConnectionPolicy(defaultPolicy)
                .setConsistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildAsyncClient();

        targetDatabase = client.getDatabase("EntertainmentDatabase");
        customContainer = targetDatabase.getContainer("CustomCollection");

        ArrayList<ViewMap> mapInteractions = new ArrayList<ViewMap>();
        Faker faker = new Faker();

        for (int i= 0; i < 500;i++){  
            ViewMap doc = new ViewMap(); 

            doc.setMinutesViewed(faker.random().nextInt(1, 60));
            doc.setType("WatchLiveTelevisionChannel");
            doc.setId(UUID.randomUUID().toString());
            mapInteractions.add(doc);
        }

        Flux<ViewMap> mapInteractionsFlux = Flux.fromIterable(mapInteractions);
        List<CosmosAsyncItemResponse<ViewMap>> results = 
            mapInteractionsFlux.flatMap(interaction -> customContainer.createItem(interaction)).collectList().block();

        results.forEach(result -> logger.info("Item Created\t{}",result.getItem().getId()));

        client.close();        
    }
}