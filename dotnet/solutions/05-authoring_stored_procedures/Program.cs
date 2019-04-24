using System;
using Bogus;
using System.Threading.Tasks;
using Microsoft.Azure.Documents;
using Microsoft.Azure.Documents.Client;
using System.Collections.Generic;
using System.Linq;

public class Program
{ 
    private static readonly Uri _endpointUri = new Uri("");
    private static readonly string _primaryKey = "";

    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();

            Uri sprocLinkUpload = UriFactory.CreateStoredProcedureUri("FinancialDatabase", "InvestorCollection", "bulkUpload");
            List<Person> people = new Faker<Person>()
                .RuleFor(p => p.firstName, f => f.Name.FirstName())
                .RuleFor(p => p.lastName, f => f.Name.LastName())
                .RuleFor(p => p.company, f => "contosofinancial")
                .Generate(25000);
            int pointer = 0;
            while(pointer < people.Count)
            {
                RequestOptions options = new RequestOptions { PartitionKey = new PartitionKey("contosofinancial") };
                StoredProcedureResponse<int> result = await client.ExecuteStoredProcedureAsync<int>(sprocLinkUpload, options, people.Skip(pointer));
                pointer += result.Response;
                await Console.Out.WriteLineAsync($"{pointer} Total Documents\t{result.Response} Documents Uploaded in this Iteration");
            }

            Uri sprocLinkDelete = UriFactory.CreateStoredProcedureUri("FinancialDatabase", "InvestorCollection", "bulkDelete");
            bool resume = true;
            do
            {
                RequestOptions options = new RequestOptions { PartitionKey = new PartitionKey("contosofinancial") };
                string query = "SELECT * FROM investors i WHERE i.company = 'contosofinancial'";
                StoredProcedureResponse<DeleteStatus> result = await client.ExecuteStoredProcedureAsync<DeleteStatus>(sprocLinkDelete, options, query);
                await Console.Out.WriteLineAsync($"Batch Delete Completed.\tDeleted: {result.Response.Deleted}\tContinue: {result.Response.Continuation}");
                resume = result.Response.Continuation;
            }
            while(resume);
        }   
    }
}