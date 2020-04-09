// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import java.util.List;
import java.util.ArrayList;

public class Food {
    private String id;
    private String description;
    private String manufacturerName;
    private List<Tag> tags;
    private String foodGroup;
    private List<Nutrient> nutrients;
    private List<Serving> servings;

    public String getId() {
        return id;
    }

    public List<Serving> getServings() {
        return servings;
    }

    public void setServings(List<Serving> servings) {
        this.servings = servings;
    }

    public List<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public void addNutrient(Nutrient nutrient) {
        if (this.nutrients == null)
            this.nutrients = new ArrayList<Nutrient>();

        this.nutrients.add(nutrient);
    }

    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (this.tags == null)
            this.tags = new ArrayList<Tag>();

        this.tags.add(tag);
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }
}