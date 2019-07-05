using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Azure.Cosmos;

public class Program
{
    private static readonly string _endpointUri = "";
    private static readonly string _primaryKey = "";

    private static readonly string _databaseId = "FinancialDatabase";
    private static readonly string _peopleCollectionId = "PeopleCollection";
    private static readonly string _transactionCollectionId = "TransactionCollection";

    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);

        }
    }
}
