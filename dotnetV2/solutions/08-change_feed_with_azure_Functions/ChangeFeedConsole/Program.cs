using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Azure.Cosmos;
using shared;

namespace ChangeFeedConsole
{
    class Program
    {
        private static readonly string _endpointUrl = "<your-endpoint-url>";
        private static readonly string _primaryKey = "<your-primary-key>";
        private static readonly string _databaseId = "StoreDatabase";
        private static readonly string _containerId = "CartContainer";

        private static readonly string _destinationContainerId = "CartContainerByState";

        static async Task Main(string[] args)
        {
            using (var client = new CosmosClient(_endpointUrl, _primaryKey))
            {
                var db = client.GetDatabase(_databaseId);
                var container = db.GetContainer(_containerId);
                var destinationContainer = db.GetContainer(_destinationContainerId);

                Container leaseContainer = await db.CreateContainerIfNotExistsAsync(id: "consoleLeases", partitionKeyPath: "/id", throughput: 400);

                var builder = container.GetChangeFeedProcessorBuilder("migrationProcessor", (IReadOnlyCollection<CartAction> input, CancellationToken cancellationToken) =>
                {
                    Console.WriteLine(input.Count + " Changes Received");

                    var tasks = new List<Task>();

                    foreach (var doc in input)
                    {
                        tasks.Add(destinationContainer.CreateItemAsync(doc, new PartitionKey(doc.BuyerState)));
                    }

                    return Task.WhenAll(tasks);
                });

                var processor = builder.WithInstanceName("changeFeedConsole").WithLeaseContainer(leaseContainer).Build();

                await processor.StartAsync();
                Console.WriteLine("Started Change Feed Processor");
                Console.WriteLine("Press any key to stop the processor...");

                Console.ReadKey();

                Console.WriteLine("Stopping Change Feed Processor");
                await processor.StopAsync();
            }
        }
    }
}