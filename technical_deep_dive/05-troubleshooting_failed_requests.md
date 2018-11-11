# Troubleshooting and Tuning Azure Cosmos DB Requests 

In this lab, you will use the .NET SDK to tune an Azure Cosmos DB request to optimize performance of your application.

## Setup

> Before you start this lab, you will need to create an Azure Cosmos DB database and collection that you will use throughout the lab. You will also use the **Data Migration Tool** to import existing data into your collection.

### Create Azure Cosmos DB Database and Collection

*You will now create a database and collection within your Azure Cosmos DB account.*

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/05-resource_groups.jpg)

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Overview** link on the left side of the blade.

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, select the **Create new** option and enter the value **FinancialDatabase**.

    1. Ensure the **Provision database throughput** option is not selected.

    1. In the **Collection id** field, enter the value **TransactionCollection**.

    1. In the **Storage capacity** section, select the **Unlimited** option.

    1. In the **Partition key** field, enter the value ``/costCenter``.

    1. In the **Throughput** field, enter the value ``10000``.

    1. Click the **OK** button.

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, select the **Existing database** option and then enter the value **FinancialDatabase**.

    1. In the **Collection id** field, enter the value **PeopleCollection**.

    1. In the **Storage capacity** section, select the **Fixed-Size** option.

    1. In the **Throughput** field, enter the value ``1000``.

    1. Click the **OK** button.

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

### Retrieve Account Credentials

*The Data Migration Tool and .NET SDKs both require credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.*

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

    ![Keys pane](../media/05-keys_pane.jpg)

1. In the **Keys** pane, record the values in the **CONNECTION STRING**, **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

    ![Credentials](../media/05-keys.jpg)

### Import Lab Data Into Collection

You will use **Azure Data Factory (ADF)** to import the JSON array stored in the **students.json** file from Azure Blob Storage.

1. On the left side of the portal, click the **Resource groups** link.

   > To learn more about copying data to Cosmos DB with ADF, please read [ADF's documentation](https://docs.microsoft.com/en-us/azure/data-factory/connector-azure-cosmos-db). 

   ![Resource groups](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-resource_groups.jpg)

2. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. Click **add** to add a new resource

![Add adf](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-add_adf.jpg)

1. Search for **Data Factory** and select it

![adf-search](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_search.png)

1. Create a new **Data Factory**. You should name this data factory **importtransactions** and select the relevant Azure subscription. You should ensure your existing **cosmosdblab-group** resource group is selected as well as a Version **V2**. Select **East US** as the region. Click **create**.

![df](C:../media/05-adf_selections.jpg)

1. Select **Copy Data**. We will be using ADF for a one-time copy of data from a source JSON file on Azure Blob Storage to a database in Cosmos DB's SQL API. ADF can also be used for more frequent data transfer from Cosmos DB to other data stores.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_copydata.jpg)

1. Edit basic properties for this data copy. You should name the task **ImportTransactions** and select to **Run once now**.

   ![adf-properties](C:..//media/05-adf_properties.jpg)

   1. **Create a new connection** and select **Azure Blob Storage**. We will import data from a json file on Azure Blob Storage. In addition to Blob Storage, you can use ADF to migrate from a wide variety of sources. We will not cover migration from these sources in this tutorial.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_blob.jpg)

1. Name the source **TransactionsJson** and select **Use SAS URI** as the Authentication method. Please use the following SAS URI for read-only access to this Blob Storage container:  

   https://cosmosdblabs.blob.core.windows.net/?sv=2017-11-09&ss=bfqt&srt=sco&sp=rwdlacup&se=2020-03-11T08:08:39Z&st=2018-11-10T02:08:39Z&spr=https&sig=ZSwZhcBdwLVIMRj94pxxGojWwyHkLTAgnL43BkbWKyg%3D

2. Select the **transactions** folder

   ![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_choosestudents.jpg)

   1. Ensure that **Copy file recursively** and **Binary Copy** are not checked off. Also ensure that **Compression Type** is "none".



   1. ADF should auto-detect the file format to be JSON. You can also select the file format as **JSON format.** You should also make sure you select **Array of Objects**  as the File pattern.



1. You have now successfully connected the Blob Storage container with the students.json file. You should select **TransactionsJson** as the source and click **Next**.



1. Add the Cosmos DB target data store by selecting **Create new connection** and selecting **Azure Cosmos DB**.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_selecttarget.jpg)

1. Name the linked service **targetcosmosdb** and select your Azure subscription and Cosmos DB account. You should also select the Cosmos DB database that you created earlier.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_selecttargetdb.jpg)

1. Select your newly created **targetcosmosdb** connection as the Destination date store.

   ![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_destconnectionnext.jpg)

2. Select your collection from the drop-down menu. You will map your Blob storage file to the correct Cosmos DB collection.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_correcttable.jpg)

1. You should have selected to skip column mappings in a previous step. Click through this screen.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_destinationconnectionfinal.jpg)

1. There is no need to change any settings. Click **next**.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_settings.jpg)

1. After deployment is complete, select **Monitor**.

![](C:/Users/tisande/OneDrive%20-%20Microsoft/LabEdits/labs/media/03-adf_deployment.jpg)

1. After a few minutes, refresh the page and the status for the ImportStudents pipeline should be listed as **Succeeded**.

1. Once the import process has completed, close the ADF. You will now proceed to execute simple queries on your imported data. 

### Create a .NET Core Project

1. On your local machine, create a new folder that will be used to contain the content of your .NET Core project.

1. In the new folder, right-click the folder and select the **Open with Code** menu option.

    ![Open with Visual Studio Code](../media/05-open_with_code.jpg)

    > Alternatively, you can run a command prompt in your current directory and execute the ``code .`` command.

1. In the Visual Studio Code window that appears, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

    ![Open in Command Prompt](../media/05-open_command_prompt.jpg)

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
    dotnet add package Bogus --version 22.0.8
    ```

    > This command will add the [Bogus](https://www.nuget.org/packages/Bogus/) NuGet package as a project dependency. This library will allow us to quickly generate test data using a fluent syntax and minimal code. We will use this library to generate test documents to upload to our Azure Cosmos DB instance. The lab instructions have been tested using the ``22.0.8`` version of this NuGet package.

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

    ![Project files](../media/05-project_files.jpg)

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
            <PackageReference Include="Bogus" Version="22.0.8" />
            <PackageReference Include="Microsoft.Azure.DocumentDB.Core" Version="1.9.1" />
        </ItemGroup>
    </Project>
    ```

1. Double-click the **Program.cs** link in the **Explorer** pane to open the file in the editor.

    ![Open editor](../media/05-program_editor.jpg)

### Create DocumentClient Instance

*The DocumentClient class is the main "entry point" to using the SQL API in Azure Cosmos DB. We are going to create an instance of the **DocumentClient** class by passing in connection metadata as parameters of the class' constructor. We will then use this class instance throughout the lab.*

1. Within the **Program.cs** editor tab, Add the following using blocks to the top of the editor:

    ```csharp
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Diagnostics;
    using System.Linq;
    using System.Net;
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
    private static readonly string _databaseId = "FinancialDatabase";
    private static readonly string _collectionId = "PeopleCollection";  
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

1. Your ``Program`` class definition should now look like this:

    ```csharp
    public class Program
    { 
        private static readonly Uri _endpointUri = new Uri("<your uri>");
        private static readonly string _primaryKey = "<your key>";
        private static readonly string _databaseId = "FinancialDatabase";
        private static readonly string _collectionId = "PeopleCollection";

        public static async Task Main(string[] args)
        {    
            using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
            {
                Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
            }     
        }
    }
    ```

    > We will now execute build the application to make sure our code compiles successfully.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet build
    ```

    > This command will build the console project.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

## Examining Response Headers

*Azure Cosmos DB returns various response headers that can give you more metadata about your request and what operations occured on the server-side. The .NET SDK exposes many of these headers to you as properties of the ``ResourceResponse<>`` class.*

### Observe RU Charge for Large Document

1. Locate the using block within the **Main** method:

    ```csharp
    using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
    {                        
    }
    ```
    
1. After the last line of code in the using block, add a new line of code to create a new object and store it in a variable named **doc**:

    ```csharp
    object doc = new Bogus.Person();
    ```

    > The **Bogus** library has a special helper class (``Bogus.Person``) that will generate a fictional person with randomized properties. Here's an example of a fictional person JSON document:
    
    ```js
    {
        "Gender": 1,
        "FirstName": "Rosalie",
        "LastName": "Dach",
        "FullName": "Rosalie Dach",
        "UserName": "Rosalie_Dach",
        "Avatar": "https://s3.amazonaws.com/uifaces/faces/twitter/mastermindesign/128.jpg",
        "Email": "Rosalie27@gmail.com",
        "DateOfBirth": "1962-02-22T21:48:51.9514906-05:00",
        "Address": {
            "Street": "79569 Wilton Trail",
            "Suite": "Suite 183",
            "City": "Caramouth",
            "ZipCode": "85941-7829",
            "Geo": {
                "Lat": -62.1607,
                "Lng": -123.9278
            }
        },
        "Phone": "303.318.0433 x5168",
        "Website": "gerhard.com",
        "Company": {
            "Name": "Mertz - Gibson",
            "CatchPhrase": "Focused even-keeled policy",
            "Bs": "architect mission-critical markets"
        }
    }
    ```

1. Add a new line of code to invoke the **CreateDocumentAsync** method of the **DocumentClient** instance using the **collectionLink** and **doc** variables as parameters:

    ```csharp
    ResourceResponse<Document> response = await client.CreateDocumentAsync(collectionLink, doc);
    ```
1. After the last line of code in the using block, add a new line of code to print out the value of the **RequestCharge** property of the **ResourceResponse<>** instance:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");
    ```

1. Your **Main** method should now look like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
            object doc = new Bogus.Person();
            ResourceResponse<Document> response = await client.CreateDocumentAsync(collectionLink, doc);
            await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");
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

    > You should see the document creation operation use approximately 10 RUs.

1. Click the **🗙** symbol to close the terminal pane.

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node and then observe select the **PeopleCollection** node.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT TOP 2 * FROM coll ORDER BY coll._ts DESC
    ```

    > This query will return the latest two documents added to your collection.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. Return to the currently open **Visual Studio Code** editor containing your .NET Core project.

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. To view the RU charge for inserting a very large document, we will use the **Bogus** library to create a fictional family. To create a fictional family, we will generate two fictional parents and an array of 4 fictional children:

    ```js
    {
        "Person":  { ... }, 
        "Relatives": {
            "Spouse": { ... }, 
            "Children": [
                { ... }, 
                { ... }, 
                { ... }, 
                { ... }
            ]
        }
    }
    ```

    Each property will have a **Bogus**-generated fictional person. This should create a large JSON document that we can use to observe RU charges.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    object doc = new Bogus.Person();
    ```

    Replace that line of code with the following code:

    ```csharp
    object doc = new
    {
        Person = new Bogus.Person(),
        Relatives = new
        {
            Spouse = new Bogus.Person(), 
            Children = Enumerable.Range(0, 4).Select(r => new Bogus.Person())
        }
    };
    ```

    > This new block of code will create the large JSON object discussed above.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

    > You should see this new operation require far more RUs than the simple JSON document.

1. Click the **🗙** symbol to close the terminal pane.

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node and then observe select the **PeopleCollection** node.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM coll WHERE IS_DEFINED(coll.Relatives)
    ```

    > This query will return the only document in your collection with a property named **Children**.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

### Tune Index Policy

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node, expand the **PeopleCollection** node, and then select the **Scale & Settings** option.

1. In the **Settings** section, locate the **Indexing Policy** field and observe the current default indexing policy:

    ```js
    {
        "indexingMode": "consistent",
        "automatic": true,
        "includedPaths": [
            {
                "path": "/*",
                "indexes": [
                    {
                        "kind": "Range",
                        "dataType": "Number",
                        "precision": -1
                    },
                    {
                        "kind": "Range",
                        "dataType": "String",
                        "precision": -1
                    },
                    {
                        "kind": "Spatial",
                        "dataType": "Point"
                    }
                ]
            }
        ],
        "excludedPaths": []
    }
    ```

    > This policy will index all paths in your JSON document. This policy implements maximum percision (-1) for both numbers (max 8) and strings (max 100) paths. This policy will also index spatial data.

1. Replace the indexing policy with a new policy that removes the ``/Relatives/*`` path from the index:

    ```js
    {
        "indexingMode": "consistent",
        "automatic": true,
        "includedPaths": [
            {
                "path":"/*",
                "indexes":[
                    {
                        "kind": "Range",
                        "dataType": "String",
                        "precision": -1
                    },
                    {
                        "kind": "Range",
                        "dataType": "Number",
                        "precision": -1
                    }
                ]
            }
        ],
        "excludedPaths": [
            {
                "path":"/Relatives/*"
            }
        ]
    }
    ```

    > This new policy will exclude the ``/Relatives/*`` path from indexing effectively removing the **Children** property of your large JSON document from the index.

1. Click the **Save** button at the top of the section to persist your new indexing policy and "kick off" a transformation of the collection's index.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM coll WHERE IS_DEFINED(coll.Relatives)
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

    > You will see immediately that you can still determine if the **/Relatives** path is defined.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM coll WHERE IS_DEFINED(coll.Relatives) ORDER BY coll.Relatives.Spouse.FirstName
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

    > This query will fail immediately since this property is not indexed.

1. Return to the currently open **Visual Studio Code** editor containing your .NET Core project.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

    > You should see a difference in the number of RUs required to create this document. This is due to the indexer skipping the paths you excluded.

1. Click the **🗙** symbol to close the terminal pane.

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node, expand the **PeopleCollection** node, and then select the **Scale & Settings** option.

1. In the **Settings** section, locate the **Indexing Policy** field and replace the indexing policy with a new policy:

    ```js
    {
        "indexingMode": "consistent",
        "automatic": true,
        "includedPaths": [
            {
                "path":"/*",
                "indexes":[
                    {
                        "kind": "Range",
                        "dataType": "String",
                        "precision": -1
                    },
                    {
                        "kind": "Range",
                        "dataType": "Number",
                        "precision": -1
                    }
                ]
            }
        ]
    }
    ```

    > This new policy removes the ``/Relatives/*`` path from the excluded path list so that the path can be indexed again.

1. Click the **Save** button at the top of the section to persist your new indexing policy and "kick off" a transformation of the collection's index.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM coll WHERE IS_DEFINED(coll.Relatives) ORDER BY coll.Relatives.Spouse.FirstName
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

    > This query should now work. If you see an empty result set, this is because the indexer has not finished indexing all of the documents in the collection. Simply rerun the query until you see a non-empty result set.

### Implement Upsert using Response Status Codes

1. Return to the currently open **Visual Studio Code** editor containing your .NET Core project.

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

    Replace that line of code with the following code:

    ```csharp
    Uri documentLink = UriFactory.CreateDocumentUri(_databaseId, _collectionId, "example.document");
    ```

    > Instead of having a Uri that references a collection, we will create a Uri that references a specific document. To create this Uri, you will need a third parameter specifying the unique string identifier for the document. In this example, our string id is ``example.document``.

1. Locate the following block of code:

    ```csharp
    object doc = new
    {
        Person = new Bogus.Person(),
        Relatives = new
        {
            Spouse = new Bogus.Person(), 
            Children = Enumerable.Range(0, 4).Select(r => new Bogus.Person())
        }
    };
    ```

    Replace that line of code with the following code:

    ```csharp
    object doc = new {
        id = "example.document",
        FirstName = "Example",
        LastName = "Person"
    };
    ```

    > Here we are creating a new document that has an **id** property set to a value of ``example.document``.

1. Locate the following line of code:

    ```csharp
    ResourceResponse<Document> response = await client.CreateDocumentAsync(collectionLink, doc);
    ```

    Replace that line of code with the following code:

    ```csharp
    ResourceResponse<Document> readResponse = await client.ReadDocumentAsync(documentLink);
    ```

    > We will now use the **ReadDocumentAsync** method of the **DocumentClient** class to retrieve a document using the unique id.

1. Locate the following line of code:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");
    ```

    Replace that line of code with the following code:

    ```csharp
    await Console.Out.WriteLineAsync($"{readResponse.StatusCode}");
    ```

    > This will print out the status code that was returned as a response for your operation.

1. Your ``Main`` method should now look like this:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            Uri documentLink = UriFactory.CreateDocumentUri(_databaseId, _collectionId, "example.document");
            object doc = new {
                id = "example.document",
                FirstName = "Example",
                LastName = "Person"
            };
            ResourceResponse<Document> readResponse = await client.ReadDocumentAsync(documentLink);
            await Console.Out.WriteLineAsync($"{readResponse.StatusCode}");
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

1. Observe the exception message.

    > You should see that an exception was thrown since a document was not found that matches the specified id.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following block of code: 

    ```csharp
    ResourceResponse<Document> readResponse = await client.ReadDocumentAsync(documentLink);
    await Console.Out.WriteLineAsync($"{readResponse.StatusCode}");
    ```

    Replace that line of code with the following code:

    ```csharp
    try
    {
        ResourceResponse<Document> readResponse = await client.ReadDocumentAsync(documentLink);
        await Console.Out.WriteLineAsync($"Success: {readResponse.StatusCode}");
    }
    catch (DocumentClientException dex)
    {
        await Console.Out.WriteLineAsync($"Exception: {dex.StatusCode}");
    }
    ```

    > This try-catch block will handle a **DocumentClientException** throw by printing out the status code related to the exception.

1. Your ``Main`` method should now look like this:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            Uri documentLink = UriFactory.CreateDocumentUri(_databaseId, _collectionId, "example.document");
            object doc = new {
                id = "example.document",
                FirstName = "Example",
                LastName = "Person"
            };
            try
            {
                ResourceResponse<Document> readResponse = await client.ReadDocumentAsync(documentLink);
                await Console.Out.WriteLineAsync($"Success: {readResponse.StatusCode}");
            }
            catch (DocumentClientException dex)
            {
                await Console.Out.WriteLineAsync($"Exception: {dex.StatusCode}");
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

1. Observe the exception message.

    > You should see a status code indicating that a document was not found. The catch block worked successfully.

1. Click the **🗙** symbol to close the terminal pane.

    > While you could manually implement upsert logic, the .NET SDK contains a useful method to implement this logic for you.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    Uri documentLink = UriFactory.CreateDocumentUri(_databaseId, _collectionId, "example.document");
    ```

    Replace that line of code with the following code:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Locate the following block of code: 

    ```csharp
    try
    {
        ResourceResponse<Document> readResponse = await client.ReadDocumentAsync(documentLink);
        await Console.Out.WriteLineAsync($"Success: {readResponse.StatusCode}");
    }
    catch (DocumentClientException dex)
    {
        await Console.Out.WriteLineAsync($"Exception: {dex.StatusCode}");
    }
    ```

    Replace that line of code with the following code:

    ```csharp
    ResourceResponse<Document> readResponse = await client.UpsertDocumentAsync(collectionLink, doc);
    await Console.Out.WriteLineAsync($"{readResponse.StatusCode}");
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the message.

    > Since we are "upserting" a document with a unique **id**, the server-side operation will be to create a new document. You should see the status code ``Created`` indicating that the create operation was completed successfully.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the message.

    > Since we are "upserting" a document with the same **id**, the server-side operation will be to update the existing document with the same **id**. You should see the status code ``OK`` indicating that the update operation was completed successfully.

1. Click the **🗙** symbol to close the terminal pane.

## Troubleshooting Requests

*First, you will use the .NET SDK to issue request beyond the assigned capacity for a container. Request unit consumption is evaluated at a per-second rate. For applications that exceed the provisioned request unit rate, requests are rate-limited until the rate drops below the provisioned throughput level. When a request is rate-limited, the server preemptively ends the request with an HTTP status code of ``429 RequestRateTooLargeException`` and returns the ``x-ms-retry-after-ms`` header. The header indicates the amount of time, in milliseconds, that the client must wait before retrying the request. You will observe the rate-limiting of your requests in an example application.*

### Reducing R/U Throughput for a Collection

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node, expand the **TransactionCollection** node, and then select the **Scale & Settings** option.

1. In the **Settings** section, locate the **Throughput** field and update it's value to **1000**.

    > This is the minimum throughput that you can allocate to an *unlimited* collection.

1. Click the **Save** button at the top of the section to persist your new throughput allocation.

### Observing Throttling (HTTP 429)

1. Return to the currently open **Visual Studio Code** editor containing your .NET Core project.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **New File** menu option.

    ![New File](../media/05-new_file.jpg)

1. Name the new file **Transaction.cs** . The editor tab will automatically open for the new file.

1. Paste in the following code for the ``Transaction`` class:

    ```csharp
    public class Transaction
    {
        public double amount { get; set; }
        public bool processed { get; set; }
        public string paidBy { get; set; }
        public string costCenter { get; set; }
    }
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet build
    ```

    > This command will build the console project.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

1. Double-click the **Program.cs** link in the **Explorer** pane to open the file in the editor.

1. Locate the following line of code that identifies the collection that will be used by the application:

    ```csharp
    private static readonly string _collectionId = "PeopleCollection";  
    ```

    Replace the line of code with the following line of code:

    ```csharp
    private static readonly string _collectionId = "TransactionCollection";
    ```

    > We will use a different collection for the next section of the lab.

1. Locate the *using* block within the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
        }
    }
    ```

1. Add the following code to the method to create an asynchronous connection:

    ```csharp
    await client.OpenAsync();
    ```
    
1. Add the following line of code to create a variable named ``collectionLink`` that is a reference (self-link) to an existing collection:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Observe the code in the **Main** method.

    > For the next few instructions, we will use the **Bogus** library to create test data. This library allows you to create a collection of objects with fake data set on each object's property. For this lab, our intent is to **focus on Azure Cosmos DB** instead of this library. With that intent in mind, the next set of instructions will expedite the process of creating test data.

1. Add the following code to create a collection of ``Transaction`` instances:

    ```csharp
    var transactions = new Bogus.Faker<Transaction>()
        .RuleFor(t => t.amount, (fake) => Math.Round(fake.Random.Double(5, 500), 2))
        .RuleFor(t => t.processed, (fake) => fake.Random.Bool(0.6f))
        .RuleFor(t => t.paidBy, (fake) => $"{fake.Name.FirstName().ToLower()}.{fake.Name.LastName().ToLower()}")
        .RuleFor(t => t.costCenter, (fake) => fake.Commerce.Department(1).ToLower())
        .GenerateLazy(100);
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 100 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 1000 items by returning a variable of type **IEnumerable<Transaction>**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated.
    
1. Add the following foreach block to iterate over the ``PurchaseFoodOrBeverage`` instances:

    ```csharp
    foreach(var transaction in transactions)
    {
    }
    ```

1. Within the ``foreach`` block, add the following line of code to asynchronously create a document and save the result of the creation task to a variable:

    ```csharp
    ResourceResponse<Document> result = await client.CreateDocumentAsync(collectionLink, transaction);
    ```

    > The ``CreateDocumentAsync`` method of the ``DocumentClient`` class takes in a self-link for a collection and an object that you would like to serialize into JSON and store as a document within the specified collection.

1. Still within the ``foreach`` block, add the following line of code to write the value of the newly created resource's ``id`` property to the console:

    ```csharp
    await Console.Out.WriteLineAsync($"Document Created\t{result.Resource.Id}");
    ```

    > The ``ResourceResponse`` type has a property named ``Resource`` that can give you access to interesting data about a document such as it's unique id, time-to-live value, self-link, ETag, timestamp,  and attachments.

1. Your **Main** method should look like this:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();
            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
            var transactions = new Bogus.Faker<Transaction>()
                .RuleFor(t => t.amount, (fake) => Math.Round(fake.Random.Double(5, 500), 2))
                .RuleFor(t => t.processed, (fake) => fake.Random.Bool(0.6f))
                .RuleFor(t => t.paidBy, (fake) => $"{fake.Name.FirstName().ToLower()}.{fake.Name.LastName().ToLower()}")
                .RuleFor(t => t.costCenter, (fake) => fake.Commerce.Department(1).ToLower())
                .GenerateLazy(100);
            foreach(var transaction in transactions)
            {
                ResourceResponse<Document> result = await client.CreateDocumentAsync(collectionLink, transaction);    
                await Console.Out.WriteLineAsync($"Document Created\t{result.Resource.Id}");
            }
        }  
    }
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 100 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 500 items by returning a variable of type **IEnumerable<Transaction>**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated. The **foreach** loop at the end of this code block iterates over the collection and creates documents in Azure Cosmos DB.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of document ids associated with new documents that are being created by this tool.

1. Click the **🗙** symbol to close the terminal pane.

1. Back in the code editor tab, locate the following lines of code:

    ```csharp
    foreach(var transaction in transactions)
    {
        ResourceResponse<Document> result = await client.CreateDocumentAsync(collectionSelfLink, transaction);
        await Console.Out.WriteLineAsync($"Document Created\t{result.Resource.Id}");
    } 
    ```

    Replace those lines of code with the following code:

    ```csharp
    List<Task<ResourceResponse<Document>>> tasks = new List<Task<ResourceResponse<Document>>>();
    foreach(var transaction in transactions)
    {
        Task<ResourceResponse<Document>> resultTask = client.CreateDocumentAsync(collectionLink, transaction);
        tasks.Add(resultTask);
    }    
    Task.WaitAll(tasks.ToArray());
    foreach(var task in tasks)
    {
        await Console.Out.WriteLineAsync($"Document Created\t{task.Result.Resource.Id}");
    }  
    ```

    > We are going to attempt to run as many of these creation tasks in parallel as possible. Remember, our collection is configured at 1,000 RU/s.

1. Your **Main** method should look like this:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();
            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
            var transactions = new Bogus.Faker<Transaction>()
                .RuleFor(t => t.amount, (fake) => Math.Round(fake.Random.Double(5, 500), 2))
                .RuleFor(t => t.processed, (fake) => fake.Random.Bool(0.6f))
                .RuleFor(t => t.paidBy, (fake) => $"{fake.Name.FirstName().ToLower()}.{fake.Name.LastName().ToLower()}")
                .RuleFor(t => t.costCenter, (fake) => fake.Commerce.Department(1).ToLower())
                .GenerateLazy(100);
            List<Task<ResourceResponse<Document>>> tasks = new List<Task<ResourceResponse<Document>>>();
            foreach(var transaction in transactions)
            {
                Task<ResourceResponse<Document>> resultTask = client.CreateDocumentAsync(collectionLink, transaction);
                tasks.Add(resultTask);
            }    
            Task.WaitAll(tasks.ToArray());
            foreach(var task in tasks)
            {
                await Console.Out.WriteLineAsync($"Document Created\t{task.Result.Resource.Id}");
            } 
        }  
    }
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 100 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 500 items by returning a variable of type **IEnumerable<Transaction>**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated. The **foreach** loops at the end of this code block iterates over the collection and creates asynchronous tasks. Each asynchronous task will issue a request to Azure Cosmos DB. These requests are issued in parallel and should cause an exceptional scenario since your collection does not have enough assigned throughput to handle the volume of requests.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > This query should execute successfully. We are only creating 100 documents and we most likely will not run into any throughput issues here.

1. Click the **🗙** symbol to close the terminal pane.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    .GenerateLazy(100);
    ```

    Replace that line of code with the following code:

    ```csharp
    .GenerateLazy(5000);
    ```

    > We are going to try and create 5000 documents in parallel to see if we can hit out throughput limit.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe that the application will crash after some time.

    > This query will most likely hit our throughput limit. You will see multiple error messages indicating that specific requests have failed.

1. Click the **🗙** symbol to close the terminal pane.

## Tuning Queries and Reads

*You will now tune your requests to Azure Cosmos DB by manipulating properties of the **FeedOptions** class in the .NET SDK.*

### Measuing RU Charge

1. Locate the *using* block within the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
        }
    }
    ```

1. Add the following code to the method to create an asynchronous connection:

    ```csharp
    await client.OpenAsync();
    ```
    
1. Add the following line of code to create a variable named ``collectionLink`` that is a reference (self-link) to an existing collection:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Add the following lines of code to configure options for a query:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        PopulateQueryMetrics = true
    };
    ```

1. Add the following line of code that will store a SQL query in a string variable:

    ```csharp
    string sql = "SELECT TOP 1000 * FROM c WHERE c.processed = true ORDER BY c.amount DESC";
    ```

    > This query will perform a cross-partition ORDER BY and only return the top 1000 out of 50000 documents.

1. Add the following line of code to create a document query instance:

    ```csharp
    IDocumentQuery<Document> query = client.CreateDocumentQuery<Document>(collectionLink, sql, options).AsDocumentQuery();
    ```

1. Add the following line of code to get the first "page" of results:

    ```csharp
    var result = await query.ExecuteNextAsync();
    ```

    > We will not enumerate the full result set. We are only interested in the metrics for the first page of results.

1. Add the following lines of code to print out all of the query metrics to the console:

    ```csharp
    foreach(string key in result.QueryMetrics.Keys)
    {
        await Console.Out.WriteLineAsync($"{key}\t{result.QueryMetrics[key]}");
    }
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see multiple metrics printed out in your console window. Pay close attention to the **Total Query Execution Time**, **Request Charge** and **Retrieved Document Size** metrics.

1. Click the **🗙** symbol to close the terminal pane.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    string sql = "SELECT TOP 1000 * FROM c WHERE c.processed = true ORDER BY c.amount DESC";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT * FROM c WHERE c.processed = true";
    ```

    > This new query does not perform a cross-partition ORDER BY.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a reduction in both the **Request Charge** and **Total Query Execution Time** values.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    string sql = "SELECT * FROM c WHERE c.processed = true";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT * FROM c";
    ```

    > This new query does not filter the result set.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > Observe the slight differences in the various metric values.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    string sql = "SELECT * FROM c";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT c.id FROM c";
    ```

    > This new query does not filter the result set.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > Observe the slight differences in the various metric values.

### Managing SDK Query Options

1. Locate the *using* block within the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
        }
    }
    ```

1. Add the following code to the method to create an asynchronous connection:

    ```csharp
    await client.OpenAsync();
    ```
    
1. Add the following line of code to create a variable named ``collectionLink`` that is a reference (self-link) to an existing collection:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Add the following line of code to create a high-precision timer:

    ```csharp
    Stopwatch timer = new Stopwatch();
    ```

1. Add the following lines of code to configure options for a query:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = 1,
        MaxBufferedItemCount = 0
    }; 
    ```

1. Add the following lines of code to write various values to the console window:

    ```csharp
    await Console.Out.WriteLineAsync($"MaxItemCount:\t{options.MaxItemCount}");
    await Console.Out.WriteLineAsync($"MaxDegreeOfParallelism:\t{options.MaxDegreeOfParallelism}");
    await Console.Out.WriteLineAsync($"MaxBufferedItemCount:\t{options.MaxBufferedItemCount}");
    ```

1. Add the following line of code that will store a SQL query in a string variable:

    ```csharp
    string sql = "SELECT * FROM c WHERE c.processed = true ORDER BY c.amount DESC";
    ```

    > This query will perform a cross-partition ORDER BY on a filtered result set.

1. Add the following line of code to start the timer:

    ```csharp
    timer.Start();
    ```

1. Add the following line of code to create a document query instance:

    ```csharp
    IDocumentQuery<Document> query = client.CreateDocumentQuery<Document>(collectionLink, sql, options).AsDocumentQuery();
    ```

1. Add the following lines of code to enumerate the result set.

    ```csharp
    while (query.HasMoreResults)  
    {
        var result = await query.ExecuteNextAsync<Document>();
    }
    ```

    > Since the results are paged, we will need to call the ``ExecuteNextAsync`` method multiple times in a while loop.

1. Add the following line of code stop the timer:

    ```csharp
    timer.Stop();
    ```

1. Add the following line of code to write the timer's results to the console window:

    ```csharp
    await Console.Out.WriteLineAsync($"Elapsed Time:\t{timer.Elapsed.TotalSeconds}");
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > This initial query should take an unexpectedly long amount of time. This will require us to optimize our SDK options.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = 1,
        MaxBufferedItemCount = 0
    }; 
    ```

    Replace that line of code with the following code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = 5,
        MaxBufferedItemCount = 0
    };   
    ```

    > Setting the ``MaxDegreeOfParallelism`` property to a value of ``1`` effectively creates eliminates parallelism. Here we "bump up" the parallelism to a value of ``5``.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You shouldn't see a slight difference considering you now have some form of parallelism.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = 5,
        MaxBufferedItemCount = 0
    }; 
    ```

    Replace that line of code with the following code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = 5,
        MaxBufferedItemCount = -1
    };   
    ```

    > Setting the ``MaxBufferedItemCount`` property to a value of ``-1`` effectively tells the SDK to manage this setting.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > Again, this should have a slight impact on your performance time.
1. Back in the code editor tab, locate the following line of code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = 5,
        MaxBufferedItemCount = -1
    }; 
    ```

    Replace that line of code with the following code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = -1,
        MaxBufferedItemCount = -1
    };   
    ```

    > Parallel query works by querying multiple partitions in parallel. However, data from an individual partitioned collect is fetched serially with respect to the query Setting the ``MaxDegreeOfParallelism`` property to a value of ``-1`` effectively tells the SDK to manage this setting. Setting the **MaxDegreeOfParallelism** to the number of partitions has the maximum chance of achieving the most performant query, provided all other system conditions remain the same.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > Again, this should have a slight impact on your performance time.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 100,
        MaxDegreeOfParallelism = -1,
        MaxBufferedItemCount = -1
    };     
    ```

    Replace that line of code with the following code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 500,
        MaxDegreeOfParallelism = -1,
        MaxBufferedItemCount = -1
    };   
    ```

    > We are increasing the amount of items returned per "page" in an attempt to improve the performance of the query.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You will notice that the query performance improved dramatically. This may be an indicator that our query was bottlenecked by the client computer.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 500,
        MaxDegreeOfParallelism = -1,
        MaxBufferedItemCount = -1
    };   
    ```

    Replace that line of code with the following code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 1000,
        MaxDegreeOfParallelism = -1,
        MaxBufferedItemCount = -1
    }; 
    ```

    > For large queries, it is recommended that you increase the page size up to a value of 1000.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > By increasing the page size, you have sped up the query even more.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 1000,
        MaxDegreeOfParallelism = -1,
        MaxBufferedItemCount = -1
    }; 
    ```

    Replace that line of code with the following code:

    ```csharp
    FeedOptions options = new FeedOptions
    {
        EnableCrossPartitionQuery = true,
        MaxItemCount = 1000,
        MaxDegreeOfParallelism = -1,
        MaxBufferedItemCount = 50000
    };  
    ```

    > Parallel query is designed to pre-fetch results while the current batch of results is being processed by the client. The pre-fetching helps in overall latency improvement of a query. **MaxBufferedItemCount** is the parameter to limit the number of pre-fetched results. Setting MaxBufferedItemCount to the expected number of results returned (or a higher number) allows the query to receive maximum benefit from pre-fetching.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > This change should have decreased your query time by a small amount.

1. Click the **🗙** symbol to close the terminal pane.

### Reading and Querying Documents

1. Locate the *using* block within the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
        }
    }
    ```

1. Add the following code to the method to create an asynchronous connection:

    ```csharp
    await client.OpenAsync();
    ```
    
1. Add the following line of code to create a variable named ``collectionLink`` that is a reference (self-link) to an existing collection:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Add the following line of code that will store a SQL query in a string variable:

    ```csharp
    string sql = "SELECT TOP 1 * FROM c WHERE c.id = 'example.document'";
    ```

    > This query will find a single document matching the specified unique id

1. Add the following line of code to create a document query instance:

    ```csharp
    IDocumentQuery<Document> query = client.CreateDocumentQuery<Document>(collectionLink, sql).AsDocumentQuery();
    ```

1. Add the following line of code to get the first page of results and then store them in a variable of type **FeedResponse<>**:

    ```csharp
    FeedResponse<Document> response = await query.ExecuteNextAsync<Document>();
    ```

    > We only need to retrieve a single page since we are getting the ``TOP 1`` documents from the collection.

1. Add the following line of code to print out the value of the **RequestCharge** property of the **ResourceResponse<>** instance:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");    
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see the amount of RUs used to query for the document in your collection.

1. Click the **🗙** symbol to close the terminal pane.

1. Locate the *using* block within the **Main** method and delete any existing code:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
        }
    }
    ```

1. Add the following code to the method to create an asynchronous connection:

    ```csharp
    await client.OpenAsync();
    ```

1. Add the following code to create a Uri referencing the document you wish to search for:

    ```csharp
    Uri documentLink = UriFactory.CreateDocumentUri(_databaseId, _collectionId, "example.document");   
    ```

1. Add the following code to use the **ReadDocumentAsync** method of the **DocumentClient** class to retrieve a document using the unique id:

    ```csharp
    ResourceResponse<Document> response = await client.ReadDocumentAsync(documentLink);
    ```

1. Add the following line of code to print out the value of the **RequestCharge** property of the **ResourceResponse<>** instance:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");    
    ```
   
1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see that it took fewer RUs to obtain the document directly if you have it's unique id.

1. Click the **🗙** symbol to close the terminal pane.

## Viewing the ETag Property of a Requested Resource

*The SQL API supports optimistic concurrency control (OCC) through HTTP entity tags, or ETags. Every SQL API resource has an ETag, and the ETag is set on the server every time a document is updated. In this exercise, we will view the ETag property of a resource that is requested using the SDK.*

### Observe the ETag Property 

1. Locate the *using* block within the **Main** method:

    ```csharp
    using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
    {
    }
    ```

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");
    ```

    Replace that line of code with the following code:

    ```csharp
    await Console.Out.WriteLineAsync($"ETag: {response.Resource.ETag}");    
    ```

    > The ETag header and the current value are included in all response messages.
   
1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see an ETag for the document.

1. Enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > The ETag should remain unchanged since the document has not been changed.

1. Click the **🗙** symbol to close the terminal pane.

1. Locate the *using* block within the **Main** method:

    ```csharp
    using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
    {
    }
    ```
    
1. Within the **Main** method, locate the following line of code: 

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");
    ```

    Replace that line of code with the following code:

    ```csharp
    await Console.Out.WriteLineAsync($"Existing ETag:\t{response.Resource.ETag}");    
    ```

1. Within the **using** block, add a new line of code to create an **AccessCondition** instance that will use the **ETag** from the document and specify an **If-Match** header:

    ```csharp
    AccessCondition cond = new AccessCondition { Condition = response.Resource.ETag, Type = AccessConditionType.IfMatch };
    ```

1. Add a new line of code to update a property of the document using the **SetPropertyValue** method:

    ```csharp
    response.Resource.SetPropertyValue("FirstName", "Demo");
    ```

    > This line of code will modify a property of the document. Here we are modifying the **FirstName** property and changing it's value from **Example** to **Demo**.

1. Add a new line of code to create an instance of the **RequestOptions** class using the **AccessCondition** created earlier:

    ```csharp
    RequestOptions options = new RequestOptions { AccessCondition = cond };
    ```

1. Add a new line of code to invoke the **ReplaceDocumentAsync** method passing in both the document and the options:

    ```csharp
    response = await client.ReplaceDocumentAsync(response.Resource, options);
    ```

1. Add a new line of code to print out the **ETag** of the newly updated document:

    ```csharp
    await Console.Out.WriteLineAsync($"New ETag:\t{response.Resource.ETag}"); 
    ```

1. Your **Main** method should now look like this:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();
            Uri documentLink = UriFactory.CreateDocumentUri(_databaseId, _collectionId, "example.document");            
            ResourceResponse<Document> response = await client.ReadDocumentAsync(documentLink);
            await Console.Out.WriteLineAsync($"Existing ETag:\t{response.Resource.ETag}"); 

            AccessCondition cond = new AccessCondition { Condition = response.Resource.ETag, Type = AccessConditionType.IfMatch };
            response.Resource.SetPropertyValue("FirstName", "Demo");
            RequestOptions options = new RequestOptions { AccessCondition = cond };
            response = await client.ReplaceDocumentAsync(response.Resource, options);
            await Console.Out.WriteLineAsync($"New ETag:\t{response.Resource.ETag}"); 
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

1. Observe the output of the console application.

    > You should see that the value of the ETag property has changed. The **AccessCondition** class helped us implement optimistic concurrency by specifying that we wanted the SDK to use the If-Match header to allow the server to decide whether a resource should be updated. The If-Match value is the ETag value to be checked against. If the ETag value matches the server ETag value, the resource is updated. If the ETag is no longer current, the server rejects the operation with an "HTTP 412 Precondition failure" response code. The client then re-fetches the resource to acquire the current ETag value for the resource.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

1. Close the Visual Studio Code application.

1. Close your browser application.
