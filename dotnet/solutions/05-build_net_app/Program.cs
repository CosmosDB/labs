using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Azure.Cosmos;

public class Program
{
    private static readonly string _endpointUri = "";
    private static readonly string _primaryKey = "";


    private static readonly string _databaseId = "NutritionDatabase";
    private static readonly string _containerId = "FoodCollection";

    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var container = database.GetContainer(_containerId);

            ItemResponse<Food> candyResponse = await container.ReadItemAsync<Food>("19130", new PartitionKey("Sweets"));
            Food candy = candyResponse.Resource;
            Console.Out.WriteLine($"Read {candy.Description}");

            string sqlA = "SELECT f.description, f.manufacturerName, f.servings FROM foods f WHERE f.foodGroup = 'Sweets'";
            FeedIterator<Food> queryA = container.GetItemQueryIterator<Food>(new QueryDefinition(sqlA), requestOptions: new QueryRequestOptions
            {
                MaxConcurrency = 1,
                PartitionKey = new PartitionKey("Sweets")
            });
            foreach (Food food in await queryA.ReadNextAsync())
            {
                await Console.Out.WriteLineAsync($"{food.Description} by {food.ManufacturerName}");

                foreach (Serving serving in food.Servings)
                {
                    await Console.Out.WriteLineAsync($"\t{serving.Amount} {serving.Description}");
                }
                await Console.Out.WriteLineAsync();
            }

            string sqlB = "SELECT f.id, f.description, f.manufacturerName, f.servings FROM foods f WHERE f.manufacturerName != null";
            FeedIterator<Food> queryB = container.GetItemQueryIterator<Food>(sqlB, requestOptions: new QueryRequestOptions { MaxConcurrency = 5, MaxItemCount = 100 });
            int pageCount = 0;
            while (queryB.HasMoreResults)
            {
                Console.Out.WriteLine($"---Page #{++pageCount:0000}---");
                foreach (var food in await queryB.ReadNextAsync())
                {
                    Console.Out.WriteLine($"\t[{food.Id}]\t{food.Description,-20}\t{food.ManufacturerName,-40}");
                }
            }
        }
    }
}

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
