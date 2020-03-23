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

        ArrayList<PurchaseFoodOrBeverage> foodInteractions = new ArrayList<PurchaseFoodOrBeverage>();
        Faker faker = new Faker();

        for (int i= 0; i < 500;i++){  
            PurchaseFoodOrBeverage doc = new PurchaseFoodOrBeverage(); 
            DecimalFormat df = new DecimalFormat("###.###");      
            doc.setType("PurchaseFoodOrBeverage");            
            doc.setQuantity(faker.random().nextInt(1, 5));            
            String unitPrice = df.format(Double.valueOf((Double)faker.random().nextDouble()));
            doc.setUnitPrice(new BigDecimal(unitPrice));
            int quantity = Integer.valueOf((Integer)doc.getQuantity());        
            String totalPrice = df.format(Double.valueOf(unitPrice) * quantity);
            doc.setTotalPrice(new BigDecimal(totalPrice));
            doc.setId(UUID.randomUUID().toString());
            foodInteractions.add(doc);
        }

        Flux<PurchaseFoodOrBeverage> foodInteractionsFlux = Flux.fromIterable(foodInteractions);
        List<CosmosAsyncItemResponse<PurchaseFoodOrBeverage>> results = 
            foodInteractionsFlux.flatMap(interaction -> customContainer.createItem(interaction)).collectList().block();

        results.forEach(result -> logger.info("Item Created\t{}",result.getItem().getId()));

        client.close();        
    }
}