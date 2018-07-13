# Querying An Azure Cosmos DB Database using the SQL API

In this lab, you will query an Azure Cosmos DB database instance using the SQL language. You will use features common in SQL such as projection using SELECT statements and filtering using WHERE clauses. You will also get to use features unique to Azure Cosmos DB's SQL API such as projection into JSON, intra-document JOIN and filtering to a range of partition keys.

## Setup

*Before you start this lab, you will need to create an Azure Cosmos DB database and collection that you will use throughout the lab. You will also use the **Data Migration Tool** to import existing data into your collection.*

### Download Required Files

*A JSON file has been provided that will contain a collection 50,000 students. You will use this file later to import documents into your collection. This is a compressed JSON file, it will not be human-readable. The tool will decompress it. Please remember to check the decompress box.*

1. Download the [students.json](../files/students.json) file and save it to your local machine.

### Create Azure Cosmos DB Database and Collection

*You will now create a database and collection within your Azure Cosmos DB account.*

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/04-resource_groups.png)

1. In the **Resource groups** blade, locate and select the **LABQURY** *Resource Group*.

    ![Lab resource group](../media/04-lab_resource_group.png)

1. In the **LABQURY** blade, select the **Azure Cosmos DB** account you recently created.

    ![Cosmos resource](../media/04-cosmos_resource.png)

1. In the **Azure Cosmos DB** blade, locate and click the **Overview** link on the left side of the blade.

    ![Overview pane](../media/04-overview_pane.png)

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

    ![Add collection](../media/04-add_collection.png)

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, select the **Create new** option and enter the value **UniversityDatabase**.

    1. Ensure the **Provision database throughput** option is not selected.

        > Provisioning throughput for a database allows you to share the throughput among all the containers that belong to that database. Within an Azure Cosmos DB database, you can have a set of containers which shares the throughput as well as containers, which have dedicated throughput.

    1. In the **Collection id** field, enter the value **StudentCollection**.

    1. In the **Storage capacity** section, select the **Unlimited** option.

    1. In the **Partition key** field, enter the value ``/enrollmentYear``.

    1. In the **Throughput** field, enter the value ``11000``.

    1. Click the **+ Add Unique Key** link.

    1. In the new **Unique Keys** field, enter the value ``/studentAlias``.

    1. Click the **OK** button.

    ![Add collection](../media/04-add_collection_settings.png)

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

### Retrieve Account Credentials

*The Data Migration Tool and .NET SDKs both require credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.*

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

    ![Keys pane](../media/04-keys_pane.png)

1. In the **Keys** pane, record the values in the **CONNECTION STRING**, **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

    ![Credentials](../media/04-credentials.png)

### Import Lab Data Into Collection

*Finally, you will import the JSON documents contained in the **students.json** file you downloaded earlier in this lab. You will use the **Data Migration Tool** to import the JSON array stored in the **students.json** file from your local machine to your Azure Cosmos DB collection.

1. On your local machine, open the **Azure Cosmos DB Data Migration Tool**.

    > To learn more about the Data Migration Tool, refer to [/docs.microsoft.com/azure/cosmos-db/import-data](https://docs.microsoft.com/en-us/azure/cosmos-db/import-data).

1. In the **Welcome** step of the tool, click the **Next** button to begin the migration wizard.

    ![Data Migration Tool - Welcome](../media/04-dmt_welcome.png)

1. In the **Source Information** step of the tool, perform the following actions:

    1. In the **Import from** list, select the **JSON file(s)** option.

    1. Click the **Add Files** button.

    1. In the *Windows Explorer* dialog that opens, locate and select the **students.json** file you downloaded earlier in this lab. Click the **Open** button to add the file.

    1. Select the **Decompress data** checkbox.

    1. Click the **Next** button.

    ![Data Migration Tool - Source](../media/04-dmt_source.png)

1. In the **Target Information** step of the tool, perform the following actions:

    1. In the **Export to** list, select the **Azure Cosmos DB - Sequential record import (partitioned collection)** option.

    1. In the **Connection String** field, enter a newly constructed connection string replacing the placeholders with values from your Azure Cosmos DB account recorded earlier in this lab: ```AccountEndpoint=[uri];AccountKey=[key];Database=[database name];```. *Make sure you replace the **[uri]**, **[key]**, and **[database name]** placeholders with the corresponding values from your Azure Cosmos DB account. For example, if your **uri** is ``https://cosmosacct.documents.azure.com:443/``, your **key** is ``Get0qW1BrqxRUA9BskSRHti5HHzrp1cjTUJTBMIbxgCGXQgRWh5NZvhc0jIt4A5ZoljA2YiLxjNHtrfC6Bd2fA==`` and your **database's name** is ``UniversityDatabase``, then your connection string will look like this: ```AccountEndpoint=https://cosmosacct.documents.azure.com:443/;AccountKey=Get0qW1BrqxRUA9BskSRHti5HHzrp1cjTUJTBMIbxgCGXQgRWh5NZvhc0jIt4A5ZoljA2YiLxjNHtrfC6Bd2fA==;Database=UniversityDatabase;```. If you have followed the instructions up to this point, your database name is **UniversityDatabase**.*

    1. Click the **Verify** button to validate your connection string.

        > If you receive an error, proofread to make sure you properly replaced the placeholders.

    1. In the **Collection** field, enter the value **StudentCollection**.

    1. In the **Partition Key** field, enter the value ``/enrollmentYear``.

    1. In the **Collection Throughput** field, enter the value ``10000``.

    1. Click the **Advanced Options** button.

    1. In the **Number of Parallel Requests** field, increment the value from ``10`` to ``25``.

    1. Click the **Next** button.

    ![Data Migration Tool - Target](../media/04-dmt_target.png)

1. In the **Advanced** step of the tool, leave the existing options set to their default values and click the **Next** button.

    ![Data Migration Tool - Advanced](../media/04-dmt_advanced.png)

1. In the **Summary** step of the tool, review your options and then click the **Import** button.

    ![Data Migration Tool - Summary](../media/04-dmt_summary.png)

1. Wait for the import process to complete.

    ![Data Migration Tool - Progress](../media/04-dmt_progress.png)

    > You will know that the tool has run successfully once it has transferred 50000 records and the progress bar's animation ends. This step can take two to three minutes.

    ![Data Migration Tool - Results](../media/04-dmt_results.png)

1. Once the import process has completed, close the Azure Cosmos DB Data Migration Tool.

## Executing Simple Queries

*The Azure Cosmos DB Data Explorer allows you to view documents and run queries directly within the Azure Portal. In this exercise, you will use the Data Explorer to query the data stored in our collection.*

### Validate Imported Data

*First, you will validate that the data was successfully imported into your collection using the **Documents** view in the **Data Explorer**.*

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/04-resource_groups.png)

1. In the **Resource groups** blade, locate and select the **LABQURY** *Resource Group*.

    ![Lab resource group](../media/04-lab_resource_group.png)

1. In the **LABQURY** blade, select the **Azure Cosmos DB** account you recently created.

    ![Cosmos resource](../media/04-cosmos_resource.png)

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

    ![Data Explorer pane](../media/04-data_explorer_pane.png)

1. In the **Data Explorer** section, expand the **UniversityDatabase** database node and then expand the **StudentCollection** collection node. 

    ![Collection node](../media/04-collection_node.png)

1. Within the **StudentCollection** node, click the **Documents** link to view a subset of the various documents in the collection. Select a few of the documents and observe the properties and structure of the documents.

    ![Documents](../media/04-documents.png)

    ![Example document](../media/04-example_document.png)

### Executing a Simple SELECT Queries

*You will now use the query editor in the **Data Explorer** to execute a few simple SELECT queries using SQL syntax.*

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

    ![New SQL query](../media/04-new_query.png)

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM students s WHERE s.enrollmentYear = 2017
    ```

    > This first query will select all properties from all documents in the collection where the students where enrolled in 2017. You will notice that we are using the alias ``s`` to refer to the collection.

    ![Query editor](../media/04-query_editor.png)

1. Click the **Execute Query** button in the query tab to run the query. 

    ![Execute query](../media/04-execute_query.png)

1. In the **Results** pane, observe the results of your query.

    ![Query results](../media/04-query_results.png)

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT * FROM students WHERE students.enrollmentYear = 2017
    ```

    > In this query, we drop the ``s`` alias and use the ``students`` source. When we execute this query, we should see the same results as the previous query.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT * FROM arbitraryname WHERE arbitraryname.enrollmentYear = 2017
    ```

    > In this query, we will prove that the name used for the source can be any name you choose. We will use the name ``arbitraryname`` for the source. When we execute this query, we should see the same results as the previous query.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT s.studentAlias FROM students s WHERE s.enrollmentYear = 2017
    ```

    > Going back to ``s`` as an alias, we will now create a query where we only select the ``studentAlias`` property and return the value of that property in our result set.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE s.studentAlias FROM students s WHERE s.enrollmentYear = 2017
    ```

    > In some scenarios, you may need to return a flattened array as the result of your query. This query uses the ``VALUE`` keyword to flatten the array by taking the single returned (string) property and creating a string array.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

### Implicitly Executing a Cross-Partition Query

*The Data Explorer will allow you to create a cross-partition query without the need to manually configure any settings. You will now use the query editor in the Data Explorer to perform single or multi-partition queries*

1. Back in the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM students s WHERE s.enrollmentYear = 2016 
    ```

    > Since we know that our partition key is ``/enrollmentYear``, we know that any query that targets a single valid value for the ``enrollmentYear`` property will be a single partition query.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

    > Observe the Request Charge (in RU/s) for the executed query.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT * FROM students s
    ```

    > If we want to execute a blanket query that will fan-out to all partitions, we simply can drop our ``WHERE`` clause that filters on a single valid value for our partition key path.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

    > Observe the Request Charge (in RU/s) for the executed query. You will notice that the charge is relatively greater for this query.

1. Back in the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM students s WHERE s.enrollmentYear IN (2015, 2016, 2017)
    ```

    > Observe the Request Charge (in RU/s) for the executed query. You will notice that the charge is greater than a single partition but far less than a fan-out across all partitions.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

    > Observe the Request Charge (in RU/s) for the executed query.

### Use Built-In Functions

*There are a large variety of built-in functions available in the SQL query syntax for the SQL API in Azure Cosmos DB. We will focus on a single function in this task but you can learn more about the others here: [https://docs.microsoft.com/azure/cosmos-db/sql-api-sql-query-reference](https://docs.microsoft.com/azure/cosmos-db/sql-api-sql-query-reference#bk_built_in_functions)*

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT s.studentAlias FROM students s WHERE s.enrollmentYear = 2015
    ```

    > Our goal is to get the school-issued e-mail address for all students who enrolled in 2015. We can issue a simple query to start that will return the login alias for each student.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT CONCAT(s.studentAlias, '@contoso.edu') AS email FROM students s WHERE s.enrollmentYear = 2015
    ```

    > To get the school-issued e-mail address, we will need to concatenate the ``@contoso.edu`` string to the end of each alias. We can perform this action using the ``CONCAT`` built-in function.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE CONCAT(s.studentAlias, '@contoso.edu') FROM students s WHERE s.enrollmentYear = 2015
    ```

    > In most client-side applications, you likely would only need an array of strings as opposed to an array of objects. We can use the ``VALUE`` keyword here to flatten our result set.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

### Projecting Query Results

*In some use cases, we may need to reshape the structure of our result JSON array to a structure that our libraries or third-party APIs can parse. We will focus on a single query and re-shape the results into various formats using the native JSON capabilities in the SQL query syntax.*

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT 
        CONCAT(s.firstName, " ", s.lastName), 
        s.academicStatus.warning, 
        s.academicStatus.suspension, 
        s.academicStatus.expulsion,
        s.enrollmentYear,
        s.projectedGraduationYear
    FROM students s WHERE s.enrollmentYear = 2014
    ```

    > In this first query, we want to determine the current status of every student who enrolled in 2014. Our goal here is to eventually have a flattened, simple-to-understand view of every student and their current academic status.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

    > You will quickly notice that the value representing the name of the student, using the ``CONCAT`` function, has a placeholder property name instead of a simple string.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT 
        CONCAT(s.firstName, " ", s.lastName) AS name, 
        s.academicStatus.warning, 
        s.academicStatus.suspension, 
        s.academicStatus.expulsion,
        s.enrollmentYear,
        s.projectedGraduationYear
    FROM students s WHERE s.enrollmentYear = 2014
    ```

    > We will update our previous query by naming our property that uses a built-in function.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT {
        "name": CONCAT(s.firstName, " ", s.lastName), 
        "isWarned": s.academicStatus.warning, 
        "isSuspended": s.academicStatus.suspension, 
        "isExpelled": s.academicStatus.expulsion,
        "enrollment": {
            "start": s.enrollmentYear,
            "end": s.projectedGraduationYear
        }
    } AS studentStatus
    FROM students s WHERE s.enrollmentYear = 2014
    ```

    > Another alternative way to specify the structure of our JSON document is to use the curly braces from JSON. At this point, we are defining the structure of the JSON result directly in our query. 

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

    > You should notice that our JSON object is still "wrapped" in another JSON object. Essentially, we have an array of the parent type with a property named ``studentStatus`` that contains the actual data we want.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE {
        "name": CONCAT(s.firstName, " ", s.lastName), 
        "isWarned": s.academicStatus.warning, 
        "isSuspended": s.academicStatus.suspension, 
        "isExpelled": s.academicStatus.expulsion,
        "enrollment": {
            "start": s.enrollmentYear,
            "end": s.projectedGraduationYear
        }
    } FROM students s WHERE s.enrollmentYear = 2014
    ```

    > If we want to "unwrap" our JSON data and flatten to a simple array of like-structured objects, we need to use the ``VALUE`` keyword.

1. Click the **Execute Query** button in the query tab to run the query. In the **Results** pane, observe the results of your query.

## Use .NET SDK to Query Azure Cosmos DB

*After using the Azure Portal's **Data Explorer** to query an Azure Cosmos DB collection, you are now going to use the .NET SDK to issue similar queries.*

### Create a .NET Core Project

1. On your local machine, create a new folder that will be used to contain the content of your .NET Core project.

1. In the new folder, right-click the folder and select the **Open with Code** menu option.

    ![Open with Visual Studio Code](../media/04-open_with_code.png)

    > Alternatively, you can run a command prompt in your current directory and execute the ``code .`` command.

1. In the Visual Studio Code window that appears, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

    ![Open in Command Prompt](../media/04-open_command_prompt.png)

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet new console --output .
    ```

    > This command will create a new .NET Core 2.1 project. The project will be a **console** project and the project will be created in the current directly since you used the ``--output .`` option.

1. Visual Studio Code will most likely prompt you to install various extensions related to **.NET Core** or **Azure Cosmos DB** development. None of these extensions are required to complete the labs.

1. In the terminal pane, enter and execute the following command:

    ```sh
    dotnet add package Microsoft.Azure.DocumentDB.Core --version 1.9.1
    ```

    > This command will add the [Microsoft.Azure.DocumentDB.Core](https://www.nuget.org/packages/Microsoft.Azure.DocumentDB.Core/) NuGet package as a project dependency. The lab instructions have been tested using the ``1.9.1`` version of this NuGet package.

1. In the terminal pane, enter and execute the following command:

    ```sh
    dotnet restore
    ```

    > This command will restore all packages specified as dependencies in the project.

1. In the terminal pane, enter and execute the following command:

    ```sh
    dotnet build
    ```

    > This command will build the project.

1. Click the **🗙** symbol to close the terminal pane.

1. Observe the **Program.cs** and **[folder name].csproj** files created by the .NET Core CLI.

    ![Project files](../media/04-project_files.png)

1. Double-click the **[folder name].csproj** link in the **Explorer** pane to open the file in the editor.

1. Add a new **PropertyGroup** XML element to the project configuration within the **Project** element:

    ```xml
    <PropertyGroup>
        <LangVersion>latest</LangVersion>
    </PropertyGroup>
    ```

1. Your new XML should look like this:

    ```xml
    <Project Sdk="Microsoft.NET.Sdk">
        <PropertyGroup>
            <LangVersion>latest</LangVersion>
        </PropertyGroup>
        <PropertyGroup>
            <OutputType>Exe</OutputType>
            <TargetFramework>netcoreapp2.0</TargetFramework>
        </PropertyGroup>
        <ItemGroup>
            <PackageReference Include="Microsoft.Azure.DocumentDB.Core" Version="1.9.1" />
        </ItemGroup>
    </Project>
    ```

1. Double-click the **Program.cs** link in the **Explorer** pane to open the file in the editor.

    ![Open editor](../media/04-program_editor.png)

### Create DocumentClient Instance

*The DocumentClient class is the main "entry point" to using the SQL API in Azure Cosmos DB. We are going to create an instance of the **DocumentClient** class by passing in connection metadata as parameters of the class' constructor. We will then use this class instance throughout the lab.*

1. Within the **Program.cs** editor tab, Add the following using blocks to the top of the editor:

    ```csharp
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using Microsoft.Azure.Documents;
    using Microsoft.Azure.Documents.Client;
    using Microsoft.Azure.Documents.Linq;
    ```

1. Locate the **Program** class and replace it with the following class:

    ```csharp
    public class Program
    {
        public static async Task Main(string[] args)
        {         
        }
    }
    ```

1. Within the **Program** class, add the following lines of code to create variables for your connection information:

    ```csharp
    private static readonly Uri _endpointUri = new Uri("");
    private static readonly string _primaryKey = "";
    private static readonly string _databaseId = "UniversityDatabase";
    private static readonly string _collectionId = "StudentCollection";  
    ```

1. For the ``_endpointUri`` variable, replace the placeholder value with the **URI** value from your Azure Cosmos DB account that you recorded earlier in this lab: 

    > For example, if your **uri** is ``https://cosmosacct.documents.azure.com:443/``, your new variable assignment will look like this: ``private static readonly Uri _endpointUri = new Uri("https://cosmosacct.documents.azure.com:443/");``.

1. For the ``_primaryKey`` variable, replace the placeholder value with the **PRIMARY KEY** value from your Azure Cosmos DB account that you recorded earlier in this lab: 

    > For example, if your **primary key** is ``NAye14XRGsHFbhpOVUWB7CMG2MOTAigdei5eNjxHNHup7oaBbXyVYSLW2lkPGeKRlZrCkgMdFpCEnOjlHpz94g==``, your new variable assignment will look like this: ``private static readonly string _primaryKey = "NAye14XRGsHFbhpOVUWB7CMG2MOTAigdei5eNjxHNHup7oaBbXyVYSLW2lkPGeKRlZrCkgMdFpCEnOjlHpz94g==";``.
    
1. Locate the **Main** method:

    ```csharp
    public static async Task Main(string[] args)
    { 
    }
    ```

1. Within the **Main** method, add the following lines of code to author a using block that creates and disposes a **DocumentClient** instance:

    ```csharp
    using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
    {
        
    }
    ```

1. Your ``Program`` class definition should now look like this:

    ```csharp
    public class Program
    { 
        private static readonly Uri _endpointUri = new Uri("<your uri>");
        private static readonly string _primaryKey = "<your key>";
        private static readonly string _databaseId = "UniversityDatabase";
        private static readonly string _collectionId = "StudentCollection";

        public static async Task Main(string[] args)
        {    
            using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
            {
            }     
        }
    }
    ```

    > We are now going to implement a sample query to make sure our client connection code works.

1. Locate the using block within the **Main** method:

    ```csharp
    using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
    {
                        
    }
    ```

1. Add the following line of code to create a variable named ``collectionLink`` that references the *self-link* Uri for the collection:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Add the following line of code to create a string variable named ``sql`` that contains a sample SQL query:

    ```csharp
    string sql = "SELECT TOP 5 VALUE s.studentAlias FROM coll s WHERE s.enrollmentYear = 2018 ORDER BY s.studentAlias";
    ```

    > This query will get the alias of the top 5 2018-enrollees in the collection sorted by their alias alphabetically

1. Add the following line of code to create a document query:

    ```csharp
    IQueryable<string> query = client.CreateDocumentQuery<string>(collectionLink, new SqlQuerySpec(sql));
    ```

1. Add the following lines of code to enumerate over the results and print the strings to the console:

    ```csharp
    foreach(string alias in query)
    {
        await Console.Out.WriteLineAsync(alias);
    }
    ```

1. Your **Main** method should now look like this:

    ```csharp
    public static async Task Main(string[] args)
    {         
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
            string sql = "SELECT TOP 5 VALUE s.studentAlias FROM coll s WHERE s.enrollmentYear = 2018 ORDER BY s.studentAlias";
            IQueryable<string> query = client.CreateDocumentQuery<string>(collectionLink, new SqlQuerySpec(sql));
            foreach(string alias in query)
            {
                await Console.Out.WriteLineAsync(alias);
            }
        }   
    }
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

    > You should see five aliases printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

### Query Intra-document Array

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **New File** menu option.

    ![New File](../media/04-new_file.png)

1. Name the new file **Student.cs** . The editor tab will automatically open for the new file.

    ![Student Class File](../media/04-student_class.png)

1. Paste in the following code for the ``Student`` class:

    ```csharp
    public class Student
    {
        public string[] Clubs { get; set; }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT TOP 5 VALUE students.studentAlias FROM students WHERE students.enrollmentYear = 2018";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    > This new query will select the **clubs** property for each student in the result set. The value of the **clubs** property is a string array.

1. Locate the following line of code: 

    ```csharp
    IQueryable<string> query = client.CreateDocumentQuery<string>(collectionLink, new SqlQuerySpec(sql));
    ```

    Replace that line of code with the following code:

    ```csharp
    IQueryable<Student> query = client.CreateDocumentQuery<Student>(collectionLink, new SqlQuerySpec(sql));
    ```

    > The query was updated to return a collection of student entities instead of string values.

1. Locate the following line of code: 

    ```csharp
    foreach(string alias in query)
    {
        await Console.Out.WriteLineAsync(alias);
    }
    ```

    Replace that line of code with the following code:

    ```csharp
    foreach(Student student in query)
    foreach(string club in student.Clubs)
    {
        await Console.Out.WriteLineAsync(club);
    }
    ```

    > Our new query will need to iterate twice. First, we will iterate the collection of students and then we will iterate the collection of clubs for each student instance.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

    > You should see multiple club names printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

1. In the Visual Studio Code window, double-click the **Student.cs** file to open an editor tab for the file.

1. Within the **Student.cs** editor tab, replace all of the existing code with the following code for the ``Student`` class:

    ```csharp
    public class Student
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string[] Clubs { get; set; }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT s.firstName, s.lastName, s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    > We are now including the **firstName** and **lastName** fields in our query.

1. Locate the following block of code: 

    ```csharp
    foreach(Student student in query)
    foreach(string club in student.Clubs)
    {
        await Console.Out.WriteLineAsync(club);
    }
    ```

    Replace that block of code with the following code:

    ```csharp
    foreach(Student student in query)
    {
        await Console.Out.WriteLineAsync($"{student.FirstName} {student.LastName}");
        foreach(string club in student.Clubs)
        {
            await Console.Out.WriteLineAsync($"\t{club}");
        }
        await Console.Out.WriteLineAsync();
    }
    ```

    > This modification simply prints out more information to the console.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

1. Click the **🗙** symbol to close the terminal pane.

    > Since we only really care about the list of clubs, we want to peform a self-join that applies a cross product across the **club** properties of each student in the result set.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **New File** menu option.

1. Name the new file **StudentActivity.cs** . The editor tab will automatically open for the new file.

1. Paste in the following code for the ``StudentActivity`` class:

    ```csharp
    public class StudentActivity
    {
        public string Activity { get; set; }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT s.firstName, s.lastName, s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";
    ```

    > Here we are performing an intra-document JOIN to get a projection of all clubs across all matching students.

1. Locate the following line of code: 

    ```csharp
    IQueryable<Student> query = client.CreateDocumentQuery<Student>(collectionLink, new SqlQuerySpec(sql));
    ```

    Replace that line of code with the following code:

    ```csharp
    IQueryable<StudentActivity> query = client.CreateDocumentQuery<StudentActivity>(collectionLink, new SqlQuerySpec(sql));
    ```

1. Locate the following line of code: 

    ```csharp
    foreach(Student student in query)
    {
        await Console.Out.WriteLineAsync($"{student.FirstName} {student.LastName}");
        foreach(string club in student.Clubs)
        {
            await Console.Out.WriteLineAsync($"\t{club}");
        }
        await Console.Out.WriteLineAsync();
    }
    ```

    Replace that line of code with the following code:

    ```csharp
    foreach(StudentActivity studentActivity in query)
    {
        await Console.Out.WriteLineAsync(studentActivity.Activity);
    }
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

    > You should see multiple club names printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

    > While we did get very useful information with our JOIN query, it would be more useful to get the raw array values instead of a wrapped value. It would also make our query easier to read if we could simply create an array of strings.

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";  
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT VALUE activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";
    ```

    > Here we are using the ``VALUE`` keyword to flatten our query.

1. Locate the following line of code: 

    ```csharp
    IQueryable<StudentActivity> query = client.CreateDocumentQuery<StudentActivity>(collectionLink, new SqlQuerySpec(sql));
    ```

    Replace that line of code with the following code:

    ```csharp
    IQueryable<string> query = client.CreateDocumentQuery<string>(collectionLink, new SqlQuerySpec(sql));
    ```

1. Locate the following line of code: 

    ```csharp
    foreach(StudentActivity studentActivity in query)
    {
        await Console.Out.WriteLineAsync(studentActivity.Activity);
    }
    ```

    Replace that line of code with the following code:

    ```csharp
    foreach(string activity in query)
    {
        await Console.Out.WriteLineAsync(activity);
    }
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

    > You should see multiple club names printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

### Projecting Query Results

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **New File** menu option.

1. Name the new file **StudentProfile.cs** . The editor tab will automatically open for the new file.

1. Paste in the following code for the ``StudentProfile`` and ``StudentProfileEmailInformation`` classes:

    ```csharp
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
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT VALUE activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT VALUE { 'id': s.id, 'name': CONCAT(s.firstName, ' ', s.lastName), 'email': { 'home': s.homeEmailAddress, 'school': CONCAT(s.studentAlias, '@contoso.edu') } } FROM students s WHERE s.enrollmentYear = 2018"; 
    ```

    > This query will get relevant information about a student and format it to a specific JSON structure that our application expects. For your information, here's the query that we are using:

    ```sql
    SELECT VALUE {
        "id": s.id,
        "name": CONCAT(s.firstName, " ", s.lastName),    
        "email": {
            "home": s.homeEmailAddress,
            "school": CONCAT(s.studentAlias, '@contoso.edu')
        }
    } FROM students s WHERE s.enrollmentYear = 2018
    ```

1. Locate the following line of code: 

    ```csharp
    IQueryable<string> query = client.CreateDocumentQuery<string>(collectionLink, new SqlQuerySpec(sql));
    ```

    Replace that code with the following code:

    ```csharp
    IQueryable<StudentProfile> query = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sql));   
    ```

1. Locate the following lines of code: 

    ```csharp
    foreach(string activity in query)
    {
        await Console.Out.WriteLineAsync(activity);
    }
    ```

    Replace that code with the following code:

    ```csharp
    foreach(StudentProfile profile in query)
    {
        await Console.Out.WriteLineAsync($"[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-50}\t{profile.Email.Home}");
    }
    ```

    > This code uses the special alignment features of C# string formatting so you can see all properties of the ``StudentProfile`` instances.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

## Implement Pagination using the .NET SDK

*You will use the **HasMoreResults** boolean property and **ExecuteNextAsync** method of the **ResourceResponse** class to implement paging of your query results. Behind the scenes, these properties use a continuation token. A continuation token enables a client to retrieve the ‘next’ set of data in a follow-up query.*

### Use the **HasMoreResults** and **ExecuteNextAsync** Members to Implement Pagination

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    IQueryable<StudentProfile> query = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sql));  
    ```

    Replace that code with the following code:

    ```csharp
    IDocumentQuery<StudentProfile> query = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sql), new FeedOptions { MaxItemCount = 100 }).AsDocumentQuery();
    ```

    > The DocumentQuery class will allow us to determine if there are more results available and page through results.

1. Locate the following lines of code:

    ```csharp
    foreach(StudentProfile profile in query)
    {
        await Console.Out.WriteLineAsync($"[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-50}\t{profile.Email.Home}");
    }  
    ```

    Replace that code with the following code:

    ```csharp
    int pageCount = 0;
    while(query.HasMoreResults)
    {
        await Console.Out.WriteLineAsync($"---Page #{++pageCount:0000}---");
        foreach(StudentProfile profile in await query.ExecuteNextAsync())
        {
            await Console.Out.WriteLineAsync($"\t[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-50}\t{profile.Email.Home}");
        }
    }
    ```

    > First we check if there are more results using the ``HasMoreResults`` property of the ``IDocumentQuery<>`` interface. If this value is set to true, we invoke the ``ExecuteNextAsync`` method to get the next batch of results and enumerate them using a ``foreach`` block.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You can view the current page count by looking at the headers in the console output.

1. Click the **🗙** symbol to close the terminal pane.


## Implement Cross-Partition Queries

*With an unlimited container, you may wish to perform queries that are filtered to a partition key or perform queries across multiple partition keys. You will now implement both types of queries using the various options available in the **FeedOptions** class.*

### Execute Single-Partition Query

1. In the Visual Studio Code window, double-click the **Student.cs** file to open an editor tab for the file.

1. Replace the existing **Student** class implementation with the following code:

    ```csharp
    public class Student
    {
        public string studentAlias { get; set; }
        public int age { get; set; }
        public int enrollmentYear { get; set; }
        public int projectedGraduationYear { get; set; }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {       
    }
    ```   

1. Within the **Main** method, add the following lines of code to author a using block that creates and disposes a **DocumentClient** instance:

    ```csharp
    using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
    {
        
    }
    ```

1. Within the new *using* block, add the following line of code to asynchronously open a connection:

    ```csharp
    await client.OpenAsync();
    ```
    
1. Add the following line of code to create a variable named ``collectionLink`` that is a reference (self-link) to an existing collection:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Add the following block of code to create a query that is filtered to a single partition key:

    ```csharp
    IEnumerable<Student> query = client
        .CreateDocumentQuery<Student>(collectionLink, new FeedOptions { PartitionKey = new PartitionKey(2016) })
        .Where(student => student.projectedGraduationYear == 2020);
    ```

    > First we will restrict our query to a single partition key using the ``PartitionKey`` property of the ``FeedOptions`` class. One of our partition key values for the ``/enrollmentYear`` path is ``2016``. We will filter our query to only return documents that uses this partition key. Remember, partition key paths are case sensitive. Since our property is named ``enrollmentYear``, it will match on the partition key path of ``/enrollmentYear``.

1. Add the following block of code to print out the results of your query:

    ```csharp
    foreach(Student student in query)
    {
        Console.Out.WriteLine($"Enrolled: {student.enrollmentYear}\tGraduation: {student.projectedGraduationYear}\t{student.studentAlias}");
    }      
    ```

    > We are using the C# string formatting features to print out two properties of our student instances.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You should only see records from a single partition.

1. Click the **🗙** symbol to close the terminal pane.

### Execute Cross-Partition Query

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    IEnumerable<Student> query = client
        .CreateDocumentQuery<Student>(collectionLink, new FeedOptions { PartitionKey = new PartitionKey(2016) })
        .Where(student => student.projectedGraduationYear == 2020);
    ```

    Replace that code with the following code:

    ```csharp
    IEnumerable<Student> query = client
        .CreateDocumentQuery<Student>(collectionLink, new FeedOptions { EnableCrossPartitionQuery = true })
        .Where(student => student.projectedGraduationYear == 2020);
    ```

    > We could ignore the partition keys and simply enable cross-partition queries using the ``EnableCrossPartitionQuery`` property of the ``FeedOptions`` class. You must explicitly opt-in using the SDK classes if you wish to perform a cross-partition query from the SDK.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You will notice that results are coming from more than one partition. You can observe this by looking at the values for ``type`` on the left-hand side of the output.

1. Click the **🗙** symbol to close the terminal pane.

### Implement Continuation Token

1. Locate the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {    
                        
    }
    ```

1. Replace the **Main** method with the following implementation:

    ```csharp
    public static async Task Main(string[] args)
    {         
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();

            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);

            string continuationToken = String.Empty;
            do
            {
                FeedOptions options = new FeedOptions 
                { 
                    EnableCrossPartitionQuery = true, 
                    RequestContinuation = continuationToken 
                };
                IDocumentQuery<Student> query = client
                    .CreateDocumentQuery<Student>(collectionLink, options)
                    .Where(student => student.age < 18)
                    .AsDocumentQuery();

                FeedResponse<Student> results = await query.ExecuteNextAsync<Student>();                
                continuationToken = results.ResponseContinuation;

                await Console.Out.WriteLineAsync($"ContinuationToken:\t{continuationToken}");
                foreach(Student result in results)
                {
                    await Console.Out.WriteLineAsync($"[Age: {result.age}]\t{result.studentAlias}@consoto.edu");
                }
                await Console.Out.WriteLineAsync(); 
            } 
            while (!String.IsNullOrEmpty(continuationToken));          
        }
    }
    ```

    > A continuation token allows us to resume a paginated query either immediately or later. When creating a query, the results are automatically paged. If there are more results, the returned page of results will also include a continuation token. This token should be passed back in    This implementation creates a **do-while** loop that will continue to get pages of results as long as the continuation token is not null.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of documents grouped by "pages" of results. You should also see a continuation token associated with each page of results. This token can be used if you are in a client-server scenario where you need to continue a query that was executed earlier.

1. Click the **🗙** symbol to close the terminal pane.

### Observe How Partitions Are Accessed in a Cross-Partition Query

1. In the Visual Studio Code window, double-click the **Student.cs** file to open an editor tab for the file.

1. Replace the existing **Student** class implementation with the following code:

    ```csharp
    public class Student
    {
        public string studentAlias { get; set; }
        public int age { get; set; }
        public int enrollmentYear { get; set; }
        public int projectedGraduationYear { get; set; }

        public FinancialInfo financialData { get; set; }

        public class FinancialInfo
        {
            public double tuitionBalance { get; set; }
        }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Locate the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {    
                        
    }
    ```

1. Replace the **Main** method with the following implementation:

    ```csharp
        public static async Task Main(string[] args)
        {         
            using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
            {
                await client.OpenAsync();

                Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);

                FeedOptions options = new FeedOptions 
                { 
                    EnableCrossPartitionQuery = true
                };

                string sql = "SELECT * FROM students s WHERE s.academicStatus.suspension = true";

                IDocumentQuery<Student> query = client
                    .CreateDocumentQuery<Student>(collectionLink, sql, options)
                    .AsDocumentQuery();

                int pageCount = 0;
                while(query.HasMoreResults)
                {
                    await Console.Out.WriteLineAsync($"---Page #{++pageCount:0000}---");
                    foreach(Student result in await query.ExecuteNextAsync())
                    {
                        await Console.Out.WriteLineAsync($"Enrollment: {result.enrollmentYear}\tBalance: {result.financialData.tuitionBalance}\t{result.studentAlias}@consoto.edu");
                    }
                }        
            }
        }
    ```

    > We are creating a cross-partition query here that may (or may not) have results for each partition key. Since this is a server-side fan-out and we are not filtering on a partition key, the search will be forced to check each partition. You can potentially have pages returned that have no results for partition keys that do not have any matching data.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of documents grouped by "pages" of results. Scroll up and look at the results for **every page**. You should also notice that there is at least one page that does not have any results. This page occurs because the server-side fan-out is forced to check every partition since you are not filtering by partition keys. The next few examples will illustrate this even more.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.academicStatus.suspension = true";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14000";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You will notice in the results that one page exists that does not have any relevant data. This occurs because there's at least one partition that does not have any data that matches the query specified above. Since we are not filtering on partition keys, all partitions much be checked as part of the server-side fan-out.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14000";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14950";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > Now only 54 records will match your query. They are pretty evenly distributed across the partition keys, so you will only see one page without results.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14950";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14996";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > Only 3 records match this query. You should see more empty pages.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14996";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14998";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > Only 1 record matches this query. You should see every multiple empty pages.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

1. Close the Visual Studio Code application.

1. Close your browser application.