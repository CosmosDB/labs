using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using Microsoft.Azure.Documents;
using Microsoft.Azure.Documents.Client;
using Microsoft.Azure.Documents.Linq;

public class Program
{  
    private static readonly Uri _endpointUri = new Uri("");
    private static readonly string _primaryKey = "";

    public static async Task Main(string[] args)    
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        { 
            await client.OpenAsync();   

            Database targetDatabase = new Database { Id = "EntertainmentDatabase" };
            targetDatabase = await client.CreateDatabaseIfNotExistsAsync(targetDatabase);
            await Console.Out.WriteLineAsync($"Database Self-Link:\t{targetDatabase.SelfLink}");   

            DocumentCollection defaultCollection = new DocumentCollection 
            { 
                Id = "DefaultCollection" 
            };
            defaultCollection = await client.CreateDocumentCollectionIfNotExistsAsync(targetDatabase.SelfLink, defaultCollection);
            await Console.Out.WriteLineAsync($"Default Collection Self-Link:\t{defaultCollection.SelfLink}"); 

            IndexingPolicy indexingPolicy = new IndexingPolicy
            {
                IndexingMode = IndexingMode.Consistent,
                Automatic = true,
                IncludedPaths = new Collection<IncludedPath>
                {
                    new IncludedPath
                    {
                        Path = "/*",
                        Indexes = new Collection<Index>
                        {
                            new RangeIndex(DataType.Number, -1),
                            new RangeIndex(DataType.String, -1)                           
                        }
                    }
                }
            };
            PartitionKeyDefinition partitionKeyDefinition = new PartitionKeyDefinition
            {
                Paths = new Collection<string> { $"/{nameof(IInteraction.type)}" }
            };
            DocumentCollection customCollection = new DocumentCollection
            {
                Id = "CustomCollection",
                PartitionKey = partitionKeyDefinition,
                IndexingPolicy = indexingPolicy
            };
            RequestOptions requestOptions = new RequestOptions
            {
                OfferThroughput = 10000
            };
            customCollection = await client.CreateDocumentCollectionIfNotExistsAsync(targetDatabase.SelfLink, customCollection, requestOptions);
            await Console.Out.WriteLineAsync($"Custom Collection Self-Link:\t{customCollection.SelfLink}"); 
            
            var foodInteractions = new Bogus.Faker<PurchaseFoodOrBeverage>()
                .RuleFor(i => i.type, (fake) => nameof(PurchaseFoodOrBeverage))
                .RuleFor(i => i.unitPrice, (fake) => Math.Round(fake.Random.Decimal(1.99m, 15.99m), 2))
                .RuleFor(i => i.quantity, (fake) => fake.Random.Number(1, 5))
                .RuleFor(i => i.totalPrice, (fake, user) => Math.Round(user.unitPrice * user.quantity, 2))
                .GenerateLazy(500);

            foreach(var interaction in foodInteractions)
            {
                ResourceResponse<Document> result = await client.CreateDocumentAsync(customCollection.SelfLink, interaction);
                await Console.Out.WriteLineAsync($"Document Created\t{result.Resource.Id}");
            }

            var tvInteractions = new Bogus.Faker<WatchLiveTelevisionChannel>()
                .RuleFor(i => i.type, (fake) => nameof(WatchLiveTelevisionChannel))
                .RuleFor(i => i.minutesViewed, (fake) => fake.Random.Number(1, 45))
                .RuleFor(i => i.channelName, (fake) => fake.PickRandom(new List<string> { "NEWS-6", "DRAMA-15", "ACTION-12", "DOCUMENTARY-4", "SPORTS-8" }))
                .GenerateLazy(500);

            foreach(var interaction in tvInteractions)
            {
                ResourceResponse<Document> result = await client.CreateDocumentAsync(customCollection.SelfLink, interaction);
                await Console.Out.WriteLineAsync($"Document Created\t{result.Resource.Id}");
            }

            var mapInteractions = new Bogus.Faker<ViewMap>()
                .RuleFor(i => i.type, (fake) => nameof(ViewMap))
                .RuleFor(i => i.minutesViewed, (fake) => fake.Random.Number(1, 45))
                .GenerateLazy(500);

            foreach(var interaction in mapInteractions)
            {
                ResourceResponse<Document> result = await client.CreateDocumentAsync(customCollection.SelfLink, interaction);
                await Console.Out.WriteLineAsync($"Document Created\t{result.Resource.Id}");
            }
            
            Uri collectionSelfLink = UriFactory.CreateDocumentCollectionUri("EntertainmentDatabase", "CustomCollection"); 
            IQueryable<GeneralInteraction> query = client.CreateDocumentQuery<GeneralInteraction>(collectionSelfLink, new FeedOptions { EnableCrossPartitionQuery = true });
            foreach(GeneralInteraction interaction in query)
            {
                Console.Out.WriteLine($"[{interaction.type}]\t{interaction.id}");
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
    public decimal unitPrice { get; set; }
    public decimal totalPrice { get; set; }
    public int quantity { get; set; }
    public string type { get; set; }
}

public class ViewMap : IInteraction
{	
	public int minutesViewed { get; set; }
	public string type { get; set; }
}

public class WatchLiveTelevisionChannel : IInteraction
{
	public string channelName { get; set; }
	public int minutesViewed { get; set; }
	public string type { get; set; }
}