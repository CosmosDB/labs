using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
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

    public static void Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();   

            Uri collectionSelfLink = UriFactory.CreateDocumentCollectionUri("FinancialDatabase", "TransactionCollection");
            Stopwatch timer = new Stopwatch();
            FeedOptions options = new FeedOptions
            {
                EnableCrossPartitionQuery = true,
                MaxItemCount = 1000,
                MaxDegreeOfParallelism = -1,
                MaxBufferedItemCount = 50000
            };  
            await Console.Out.WriteLineAsync($"MaxItemCount:\t{options.MaxItemCount}");
            await Console.Out.WriteLineAsync($"MaxDegreeOfParallelism:\t{options.MaxDegreeOfParallelism}");
            await Console.Out.WriteLineAsync($"MaxBufferedItemCount:\t{options.MaxBufferedItemCount}");
            string sql = "SELECT * FROM c WHERE c.processed = true ORDER BY c.amount DESC";
            timer.Start();
            IDocumentQuery<Document> query = client.CreateDocumentQuery<Document>(collectionSelfLink, sql, options).AsDocumentQuery();
            while (query.HasMoreResults)  
            {
                var result = await query.ExecuteNextAsync<Document>();
            }
            timer.Stop();
            await Console.Out.WriteLineAsync($"Elapsed Time:\t{timer.Elapsed.TotalSeconds}");

            Uri collectionSelfLink = UriFactory.CreateDocumentCollectionUri("FinancialDatabase", "TransactionCollection");
            var transactions = new Bogus.Faker<Transaction>()
                .RuleFor(t => t.amount, (fake) => Math.Round(fake.Random.Double(5, 500), 2))
                .RuleFor(t => t.processed, (fake) => fake.Random.Bool(0.6f))
                .RuleFor(t => t.paidBy, (fake) => $"{fake.Name.FirstName().ToLower()}.{fake.Name.LastName().ToLower()}")
                .RuleFor(t => t.costCenter, (fake) => fake.Commerce.Department(1).ToLower())
                .GenerateLazy(500);
            List<Task<ResourceResponse<Document>>> tasks = new List<Task<ResourceResponse<Document>>>();
            foreach(var transaction in transactions)
            {
                Task<ResourceResponse<Document>> resultTask = client.CreateDocumentAsync(collectionSelfLink, transaction);
                tasks.Add(resultTask);
            }    
            Task.WaitAll(tasks.ToArray());
            foreach(var task in tasks)
            {
                await Console.Out.WriteLineAsync($"Document Created\t{task.Result.Resource.Id}");
            }     
        }
    }
}

public class Transaction
{
    public double Amount { get; set; }
    public bool Processed { get; set; }
    public string PaidBy { get; set; }
    public string CostCenter { get; set; }
}