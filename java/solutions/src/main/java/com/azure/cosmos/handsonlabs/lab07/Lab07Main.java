// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.lab07;

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
import com.azure.cosmos.handsonlabs.common.datatypes.DeleteStatus;
import com.azure.cosmos.handsonlabs.common.datatypes.Food;
import com.azure.cosmos.handsonlabs.common.datatypes.Nutrient;
import com.azure.cosmos.handsonlabs.common.datatypes.PurchaseFoodOrBeverage;
import com.azure.cosmos.handsonlabs.common.datatypes.Serving;
import com.azure.cosmos.handsonlabs.common.datatypes.ViewMap;
import com.azure.cosmos.handsonlabs.common.datatypes.WatchLiveTelevisionChannel;
import com.azure.cosmos.handsonlabs.common.datatypes.Tag;
import com.azure.cosmos.models.CosmosAsyncItemResponse;
import com.azure.cosmos.models.CosmosAsyncStoredProcedureResponse;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosStoredProcedureRequestOptions;
import com.azure.cosmos.models.CosmosStoredProcedureResponse;
import com.azure.cosmos.models.IndexingMode;
import com.azure.cosmos.models.IndexingPolicy;
import com.azure.cosmos.models.PartitionKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.azure.cosmos.models.IncludedPath;
import com.azure.cosmos.models.CosmosAsyncStoredProcedureResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import com.google.common.collect.Lists;

public class Lab07Main {
    protected static Logger logger = LoggerFactory.getLogger(Lab07Main.class.getSimpleName());
    private static ObjectMapper mapper = new ObjectMapper();
    private static String endpointUri = "<your uri>";
    private static String primaryKey = "<your key>";   
    private static CosmosAsyncDatabase database;
    private static CosmosAsyncContainer container;  
    private static int pointer = 0;
    private static boolean resume = false;
    
    public static void main(String[] args) {
        ConnectionPolicy defaultPolicy = ConnectionPolicy.getDefaultPolicy();
        defaultPolicy.setPreferredLocations(Lists.newArrayList("<your cosmos db account location>"));
    
        CosmosAsyncClient client = new CosmosClientBuilder()
                .setEndpoint(endpointUri)
                .setKey(primaryKey)
                .setConnectionPolicy(defaultPolicy)
                .setConsistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildAsyncClient();

        database = client.getDatabase("NutritionDatabase");
        container = database.getContainer("FoodCollection");

        List<Food> foods = new ArrayList<Food>();
        Faker faker = new Faker();

        for (int i= 0; i < 1000;i++){  
            Food food = new Food(); 

            food.setId(UUID.randomUUID().toString());
            food.setDescription(faker.food().dish());
            food.setManufacturerName(faker.company().name());
            food.setFoodGroup("Energy Bars");
            food.addTag(new Tag("Food"));
            food.addNutrient(new Nutrient());
            food.addServing(new Serving());
            foods.add(food);
        }

        while (pointer < foods.size()) {
        
            CosmosStoredProcedureRequestOptions options = new CosmosStoredProcedureRequestOptions();
            options.setPartitionKey(new PartitionKey("Energy Bars"));
    
            Object sprocArgs[] = new Object[] {foods.subList(pointer,foods.size())};

            container.getScripts()
                    .getStoredProcedure("bulkUpload")
                    .execute(sprocArgs,options)
                    .flatMap(executeResponse -> {
                        logger.info("Response: {}",executeResponse.getStatusCode());
                        int delta_items = Integer.parseInt(executeResponse.getResponseAsString());
                        pointer += delta_items;

                        logger.info("{} Total Items {} Items Uploaded in this Iteration",pointer,delta_items);

                        return Mono.empty();
            }).block();
        }

        resume = true;
        do
        {
            String query = "SELECT * FROM foods f WHERE f.foodGroup = 'Energy Bars'";

            CosmosStoredProcedureRequestOptions options = new CosmosStoredProcedureRequestOptions();
            options.setPartitionKey(new PartitionKey("Energy Bars"));
    
            Object sprocArgs[] = new Object[] {query};

            container.getScripts()
                    .getStoredProcedure("bulkDelete")
                    .execute(sprocArgs,options)
                    .flatMap(executeResponse -> {

                        DeleteStatus result = null;
                        
                        try {
                            result = mapper.readValue(executeResponse.getResponseAsString(),DeleteStatus.class);
                        } catch (Exception ex) {
                            logger.error("Failed to parse bulkDelete response.",ex);
                        }

                        logger.info("Batch Delete Completed. Deleted: {} Continue: {} Items Uploaded in this Iteration",
                            result.getDeleted(),
                            result.isContinuation());

                        resume = result.isContinuation();

                        return Mono.empty();
            }).block();
        }
        while (resume);

        client.close();        
    }
}