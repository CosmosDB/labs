using System;
using System.Threading.Tasks;
using Microsoft.Azure.Cosmos;
using Microsoft.Azure.Cosmos.Scripts;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;

namespace MultiDocTransactions
{
    public class Program
    {
        private static readonly string _endpointUri = "<your-endpoint-url>";
        private static readonly string _primaryKey = "<your-primary-key>";
        private static readonly string _databaseId = "NutritionDatabase";
        private static readonly string _containerId = "FoodCollection";

        public static async Task Main(string[] args)
        {
            using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
            {
                Database database = client.GetDatabase(_databaseId);
                Container container = database.GetContainer(_containerId);

                List<Food> foods = new Bogus.Faker<Food>()
                .RuleFor(p => p.Id, f => (-1 - f.IndexGlobal).ToString())
                .RuleFor(p => p.Description, f => f.Commerce.ProductName())
                .RuleFor(p => p.ManufacturerName, f => f.Company.CompanyName())
                .RuleFor(p => p.FoodGroup, f => "Energy Bars")
                .Generate(10000);

                int pointer = 0;
                while (pointer < foods.Count)
                {
                    StoredProcedureExecuteResponse<int> result = await container.Scripts.ExecuteStoredProcedureAsync<IEnumerable<Food>, int>(new PartitionKey("Energy Bars"), "bulkUpload", foods.Skip(pointer));
                    pointer += result.Resource;
                    await Console.Out.WriteLineAsync($"{pointer} Total Items\t{result.Resource} Items Uploaded in this Iteration");
                }

                Console.WriteLine("Execution paused for verification. Press any key to continue to delete.");
                Console.ReadKey();

                bool resume = true;
                do
                {
                    string query = "SELECT * FROM foods f WHERE f.foodGroup = 'Energy Bars'";
                    StoredProcedureExecuteResponse<DeleteStatus> result = await container.Scripts.ExecuteStoredProcedureAsync<string, DeleteStatus>(new PartitionKey("Energy Bars"), "bulkDelete", query);
                    await Console.Out.WriteLineAsync($"Batch Delete Completed.\tDeleted: {result.Resource.Deleted}\tContinue: {result.Resource.Continuation}");
                    resume = result.Resource.Continuation;
                }
                while (resume);
            }
        }
    }

    public class Food
    {
        [JsonProperty("id")]
        public string Id { get; set; }
        [JsonProperty("description")]
        public string Description { get; set; }
        [JsonProperty("manufacturerName")]
        public string ManufacturerName { get; set; }
        [JsonProperty("foodGroup")]
        public string FoodGroup { get; set; }
    }

    public class DeleteStatus
    {
        public int Deleted { get; set; }
        public bool Continuation { get; set; }
    }
}
