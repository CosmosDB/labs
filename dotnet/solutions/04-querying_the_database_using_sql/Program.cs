using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Azure.Documents;
using Microsoft.Azure.Documents.Client;
using Microsoft.Azure.Documents.Linq;

public class Program
{
    private static readonly Uri _endpointUri = new Uri("");
    private static readonly string _primaryKey = "";
    private static readonly string _databaseId = "UniversityDatabase";
    private static readonly string _collectionId = "StudentCollection";

    public static async Task Main(string[] args)
    {
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);

            string sqlA = "SELECT s.firstName, s.lastName, s.clubs FROM students s WHERE s.enrollmentYear = 2018";
            IQueryable<Student> queryA = client.CreateDocumentQuery<Student>(collectionLink, new SqlQuerySpec(sqlA));
            foreach(Student student in queryA)
            {
                await Console.Out.WriteLineAsync($"{student.FirstName} {student.LastName}");
                foreach(string club in student.Clubs)
                {
                    await Console.Out.WriteLineAsync($"\t{club}");
                }
                await Console.Out.WriteLineAsync();
            }

            string sqlB = "SELECT VALUE { 'id': s.id, 'name': CONCAT(s.firstName, ' ', s.lastName), 'email': { 'home': s.homeEmailAddress, 'school': CONCAT(s.studentAlias, '@contoso.edu') } } FROM students s WHERE s.enrollmentYear = 2018";
            IDocumentQuery<StudentProfile> queryB = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sqlB), new FeedOptions { MaxItemCount = 100 }).AsDocumentQuery();
            int pageCount = 0;
            while (queryB.HasMoreResults)
            {
                Console.Out.WriteLine($"---Page #{++pageCount:0000}---");
                foreach (StudentProfile profile in await queryB.ExecuteNextAsync())
                {
                    Console.Out.WriteLine($"\t[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-40}\t{profile.Email.Home}");
                }
            }
        }
    }
}

public class Student
{
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public string[] Clubs { get; set; }
}

public class StudentProfile
{
    public string Id { get; set; }
    public string Name { get; set; }
    public StudentProfileEmailInformation Email { get; set; }
}

public class StudentProfileEmailInformation
{
    public string Home { get; set; }
    public string School { get; set; }
}