// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.handsonlabs.common.datatypes;

import java.math.BigDecimal;

public class Nutrient {
    private String id;
    private String description;
    private BigDecimal nutritionValue;
    private String units;

    public String getId() {
        return id;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public BigDecimal getNutritionValue() {
        return nutritionValue;
    }

    public void setNutritionValue(BigDecimal nutritionValue) {
        this.nutritionValue = nutritionValue;
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