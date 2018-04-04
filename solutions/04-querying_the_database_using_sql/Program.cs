using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Azure.Documents;
using Microsoft.Azure.Documents.Client;
using Microsoft.Azure.Documents.Linq;

public class Program
{ 
    private static readonly Uri _endpointUri = new Uri("https://labqury.documents.azure.com:443/");
    private static readonly string _primaryKey = "NAye14XRGsHFbhpOVUWB7CMG2MOTAigdei5eNjxHNHup7oaBbXyVYSLW2lkPGeKRlZrCkgMdFpCEnOjlHpz94g==";
    private static readonly string _databaseId = "UniversityDatabase";
    private static readonly string _collectionId = "StudentCollection";  

    public static void Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            ExecuteLogic(client).Wait();
        }
    }
    private static async Task ExecuteLogic(DocumentClient client)
    {       
        Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);

        string sql = "SELECT VALUE { 'id': s.id, 'name': CONCAT(s.firstName, ' ', s.lastName), 'email': { 'home': s.homeEmailAddress, 'school': CONCAT(s.studentAlias, '@contoso.edu') } } FROM students s WHERE s.enrollmentYear = 2018";

        IDocumentQuery<StudentProfile> query = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sql), new FeedOptions { MaxItemCount = 100 }).AsDocumentQuery();

        int pageCount = 0;
        while(query.HasMoreResults)
        {
            Console.Out.WriteLine($"---Page #{++pageCount:0000}---");
            foreach(StudentProfile profile in await query.ExecuteNextAsync())
            {
                Console.Out.WriteLine($"\t[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-40}\t{profile.Email.Home}");
            }
        }
    }
}