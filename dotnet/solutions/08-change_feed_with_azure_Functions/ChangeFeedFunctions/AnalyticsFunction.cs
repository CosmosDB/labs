using System.Collections.Generic;
using Microsoft.Azure.Documents;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Host;
using Microsoft.Extensions.Logging;
using Microsoft.Azure.EventHubs;
using System.Threading.Tasks;
using System.Text;

namespace ChangeFeedFunctions
{
    public static class AnalyticsFunction
    {
        private static readonly string _eventHubConnection = "<your-event-hub-connection>";
        private static readonly string _eventHubName = "carteventhub";

        [FunctionName("AnalyticsFunction")]
        public static async Task Run([CosmosDBTrigger(
            databaseName: "StoreDatabase",
            collectionName: "CartContainer",
            ConnectionStringSetting = "DBConnection",
            CreateLeaseCollectionIfNotExists = true,
            LeaseCollectionName = "analyticsLeases")]IReadOnlyList<Document> input, ILogger log)
        {
            if (input != null && input.Count > 0)
            {
                var sbEventHubConnection = new EventHubsConnectionStringBuilder(_eventHubConnection)
                {
                    EntityPath = _eventHubName
                };

                var eventHubClient = EventHubClient.CreateFromConnectionString(sbEventHubConnection.ToString());

                var tasks = new List<Task>();

                foreach (var doc in input)
                {
                    var json = doc.ToString();

                    var eventData = new EventData(Encoding.UTF8.GetBytes(json));

                    log.LogInformation("Writing to Event Hub");
                    tasks.Add(eventHubClient.SendAsync(eventData));
                }

                await Task.WhenAll(tasks);
            }
        }
    }
}
