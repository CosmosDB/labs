using System.Collections.Generic;

public class Tag
{
    public string Name { get; set; }
}

public class Nutrient
{
    public string Id { get; set; }
    public string Description { get; set; }
    public decimal NutritionValue { get; set; }
    public string Units { get; set; }
}

public class Serving
{
    public decimal Amount { get; set; }
    public string Description { get; set; }
    public decimal WeightInGrams { get; set; }
}

public class Food
{
    public string Id { get; set; }
    public string Description { get; set; }
    public string ManufacturerName { get; set; }
    public List<Tag> Tags { get; set; }
    public string FoodGroup { get; set; }
    public List<Nutrient> Nutrients { get; set; }
    public List<Serving> Servings { get; set; }
}

public class GroceryProduct
{
    public string Id { get; set; }
    public string ProductName { get; set; }
    public string Company { get; set; }
    public RetailPackage Package { get; set; }
}

public class RetailPackage
{
    public string Name { get; set; }
    public double Weight { get; set; }
}
