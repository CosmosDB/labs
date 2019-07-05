using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using Microsoft.Azure.Cosmos;

public class Program
{
    private static readonly string _endpointUri = "";
    private static readonly string _primaryKey = "";

    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            DatabaseResponse databaseResponse = await client.CreateDatabaseIfNotExistsAsync("EntertainmentDatabase");
            Database targetDatabase = databaseResponse.Database;
            await Console.Out.WriteLineAsync($"Database Id:\t{targetDatabase.Id}");

            ContainerResponse response = await targetDatabase.CreateContainerIfNotExistsAsync("DefaultCollection", "/id");
            Container defaultContainer = response.Container;
            await Console.Out.WriteLineAsync($"Default Container Id:\t{defaultContainer.Id}");

            IndexingPolicy indexingPolicy = new IndexingPolicy
            {
                IndexingMode = IndexingMode.Consistent,
                Automatic = true,
                IncludedPaths =
                {
                    new IncludedPath
                    {
                        Path = "/*"
                    }
                }
            };
            
            ContainerProperties containerProperties = new ContainerProperties("CustomCollection", $"/{nameof(IInteraction.type)}")
            {
                IndexingPolicy = indexingPolicy,
            };
            var containerResponse = await targetDatabase.CreateContainerIfNotExistsAsync(containerProperties, 10000);
            var customContainer = containerResponse.Container;
            //var customContainer = targetDatabase.GetContainer("CustomCollection");
            await Console.Out.WriteLineAsync($"Custom Container Id:\t{customContainer.Id}");

            var foodInteractions = new Bogus.Faker<PurchaseFoodOrBeverage>()
                .RuleFor(i => i.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(i => i.type, (fake) => nameof(PurchaseFoodOrBeverage))
                .RuleFor(i => i.unitPrice, (fake) => Math.Round(fake.Random.Decimal(1.99m, 15.99m), 2))
                .RuleFor(i => i.quantity, (fake) => fake.Random.Number(1, 5))
                .RuleFor(i => i.totalPrice, (fake, user) => Math.Round(user.unitPrice * user.quantity, 2))
                .GenerateLazy(500);

            foreach (var interaction in foodInteractions)
            {
                ItemResponse<PurchaseFoodOrBeverage> result = await customContainer.CreateItemAsync(interaction);
                await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
            }

            var tvInteractions = new Bogus.Faker<WatchLiveTelevisionChannel>()
                .RuleFor(i => i.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(i => i.type, (fake) => nameof(WatchLiveTelevisionChannel))
                .RuleFor(i => i.minutesViewed, (fake) => fake.Random.Number(1, 45))
                .RuleFor(i => i.channelName, (fake) => fake.PickRandom(new List<string> { "NEWS-6", "DRAMA-15", "ACTION-12", "DOCUMENTARY-4", "SPORTS-8" }))
                .GenerateLazy(500);

            foreach (var interaction in tvInteractions)
            {
                ItemResponse<WatchLiveTelevisionChannel> result = await customContainer.CreateItemAsync(interaction);
                await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
            }

            var mapInteractions = new Bogus.Faker<ViewMap>()
                .RuleFor(i => i.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(i => i.type, (fake) => nameof(ViewMap))
                .RuleFor(i => i.minutesViewed, (fake) => fake.Random.Number(1, 45))
                .GenerateLazy(500);

            foreach (var interaction in mapInteractions)
            {
                ItemResponse<ViewMap> result = await customContainer.CreateItemAsync(interaction);
                await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
            }

            FeedIterator<GeneralInteraction> query = customContainer.GetItemQueryIterator<GeneralInteraction>("SELECT * FROM c");
            while (query.HasMoreResults)
            {
                foreach (GeneralInteraction interaction in await query.ReadNextAsync())
                {
                    Console.Out.WriteLine($"[{interaction.type}]\t{interaction.id}");
                }
            }
        }
    }
}

public interface IInteraction
{
    string type { get; }
}

public class GeneralInteraction : IInteraction
{
    public string id { get; set; }

    public string type { get; set; }
}

public class PurchaseFoodOrBeverage : IInteraction
{
    public string id { get; set; }
    public decimal unitPrice { get; set; }
    public decimal totalPrice { get; set; }
    public int quantity { get; set; }
    public string type { get; set; }
}

public class ViewMap : IInteraction
{
    public string id { get; set; }
    public int minutesViewed { get; set; }
    public string type { get; set; }
}

public class WatchLiveTelevisionChannel : IInteraction
{
    public string id { get; set; }
    public string channelName { get; set; }
    public int minutesViewed { get; set; }
    public string type { get; set; }
}