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
    private static readonly string _databaseId = "FinancialDatabase";
    private static readonly string _collectionId = "TransactionCollection";

    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();
            Uri documentLink = UriFactory.CreateDocumentUri(_databaseId, _collectionId, "example.document");            
            ResourceResponse<Document> response = await client.ReadDocumentAsync(documentLink);
            await Console.Out.WriteLineAsync($"Existing eTag:\t{response.Resource.ETag}"); 
            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);            
            response.Resource.SetPropertyValue("FirstName", "Demo");
            response = await client.UpsertDocumentAsync(collectionLink, response.Resource);
            await Console.Out.WriteLineAsync($"New eTag:\t{response.Resource.ETag}"); 
        }
    }
}