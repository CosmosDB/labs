// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.lab05;

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
import com.azure.cosmos.CosmosPagedFlux;
import com.azure.cosmos.handsonlabs.common.datatypes.Food;
import com.azure.cosmos.handsonlabs.common.datatypes.PurchaseFoodOrBeverage;
import com.azure.cosmos.handsonlabs.common.datatypes.Serving;
import com.azure.cosmos.handsonlabs.common.datatypes.ViewMap;
import com.azure.cosmos.handsonlabs.common.datatypes.WatchLiveTelevisionChannel;
import com.azure.cosmos.models.CosmosAsyncItemResponse;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.FeedOptions;
import com.azure.cosmos.models.IndexingMode;
import com.azure.cosmos.models.IndexingPolicy;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.models.IncludedPath;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

public class Lab05Main {
    protected static Logger logger = LoggerFactory.getLogger(Lab05Main.class.getSimpleName());
    private static String endpointUri = "<your uri>";
    private static String primaryKey = "<your key>";   
    private static CosmosAsyncDatabase database;
    private static CosmosAsyncContainer container;  
    private static AtomicInteger pageCount = new AtomicInteger(0);
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
        
                container.readItem("19130", new PartitionKey("Sweets"), Food.class)
                         .flatMap(candyResponse -> {
                            Food candy = candyResponse.getItem();
                            logger.info("Read {}",candy.getDescription());
                            return Mono.empty();
                }).block();
                         
        
        
                String sqlA = "SELECT f.description, f.manufacturerName, " + 
                                "f.servings FROM foods f WHERE f.foodGroup = " + 
                                "'Sweets' and IS_DEFINED(f.description) and " + 
                                "IS_DEFINED(f.manufacturerName) and IS_DEFINED(f.servings)";
        
                FeedOptions optionsA = new FeedOptions();
                optionsA.setMaxDegreeOfParallelism(1);
                container.queryItems(sqlA, optionsA, Food.class).byPage()
                        .flatMap(page -> {
                        for (Food fd : page.getResults()) {
                            String msg="";
                            msg = String.format("%s by %s\n",fd.getDescription(),fd.getManufacturerName());
        
                            for (Serving sv : fd.getServings()) {
                                msg += String.format("\t%f %s\n",sv.getAmount(),sv.getDescription());
                            }
                            msg += "\n";
                            logger.info(msg);
                        }
        
                        return Mono.empty();
                }).blockLast();
                         
                String sqlB = "SELECT f.id, f.description, f.manufacturerName, f.servings " + 
                                "FROM foods f WHERE IS_DEFINED(f.manufacturerName)";
        
                FeedOptions optionsB = new FeedOptions();
                optionsB.setMaxDegreeOfParallelism(5);
                optionsB.setMaxItemCount(100);
                container.queryItems(sqlB, optionsB, Food.class).byPage()
                         .flatMap(page -> {
                            String msg="";
        
                            msg = String.format("---Page %d---\n",pageCount.getAndIncrement());
        
                            for (Food fd : page.getResults()) {
                                msg += String.format("\t[%s]\t%s\t%s\n",fd.getId(),fd.getDescription(),fd.getManufacturerName());
                            }
                            logger.info(msg);
                            return Mono.empty();
                }).blockLast();
        
                client.close();        
            }
        }