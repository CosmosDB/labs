# Creating a Partitioned Container with .NET SDK

In this lab, you will create multiple Azure Cosmos DB containers using different partition keys and settings. You will then use the SQL API and .NET SDK to query specific containers using a single partition key or across multiple partition keys.

> If you have not already completed setup for the lab content see the instructions for [Account Setup](00-account_setup.md) before starting this lab.

## Create Containers using the .NET SDK

> You will start by using the .NET SDK to create containers to use in the lab.

### Create a .NET Core Project

1. On your local machine, locate the CosmosLabs folder in your Documents folder and open the Lab01 folder that will be used to contain the content of your .NET Core project.

1. In the Lab01 folder, right-click the folder and select the **Open with Code** menu option.

    ![Open with Visual Studio Code](../media/02-open_with_code.jpg)

    > Alternatively, you can run a terminal in your current directory and execute the ``code .`` command.

1. In the Visual Studio Code window that appears, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

    ![Open in Terminal](../media/open_in_terminal.jpg)

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet new console --output .
    ```

    > This command will create a new .NET Core 2.2 project. The project will be a **console** project and the project will be created in the current directly since you used the ``--output .`` option.

1. Visual Studio Code will most likely prompt you to install various extensions related to **.NET Core** or **Azure Cosmos DB** development. None of these extensions are required to complete the labs.

1. In the terminal pane, enter and execute the following command:

    ```sh
    dotnet add package Microsoft.Azure.Cosmos --version 3.0.0.18-preview
    ```

    > This command will add the [Microsoft.Azure.Cosmos](https://www.nuget.org/packages/Microsoft.Azure.Cosmos/) NuGet package as a project dependency. The lab instructions have been tested using the ``3.0.0.18-preview`` version of this NuGet package.

1. In the terminal pane, enter and execute the following command:

    ```sh
    dotnet add package Bogus --version 22.0.8
    ```

    > This command will add the [Bogus](../media/https://www.nuget.org/packages/Bogus/) NuGet package as a project dependency. This library will allow us to quickly generate test data using a fluent syntax and minimal code. We will use this library to generate test documents to upload to our Azure Cosmos DB instance. The lab instructions have been tested using the ``22.0.8`` version of this NuGet package.

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

    ![Project files](../media/02-project_files.jpg)

1. Double-click the **[folder name].csproj** link in the **Explorer** pane to open the file in the editor.

1. We will now add a new **PropertyGroup** XML element to the project configuration within the **Project** element. To add a new **PropertyGroup**, insert the following lines of code under the line that reads ``<Project Sdk="Microsoft.NET.Sdk">``:

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
            <TargetFramework>netcoreapp2.2</TargetFramework>
        </PropertyGroup>        
        <ItemGroup>
            <PackageReference Include="Bogus" Version="22.0.8" />
            <PackageReference Include="Microsoft.Azure.Cosmos" Version="3.0.0.18-preview" />
        </ItemGroup>        
    </Project>
    ```

1. Double-click the **Program.cs** link in the **Explorer** pane to open the file in the editor.

    ![Open editor](../media/02-program_editor.jpg)

### Create CosmosClient Instance

*The CosmosClient class is the main "entry point" to using the SQL API in Azure Cosmos DB. We are going to create an instance of the **CosmosClient** class by passing in connection metadata as parameters of the class' constructor. We will then use this class instance throughout the lab.*

1. Within the **Program.cs** editor tab, Add the following using blocks to the top of the editor:

    ```csharp
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Linq;
    using System.Threading.Tasks;
    using Microsoft.Azure.Cosmos;
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
    private static readonly string _endpointUri = "";
    private static readonly string _primaryKey = "";
    ```

1. For the ``_endpointUri`` variable, replace the placeholder value with the **URI** value and for the ``_primaryKey`` variable, replace the placeholder value with the **PRIMARY KEY** value from your Azure Cosmos DB account. Use [these instructions](00-account_setup.md) to get these values if you do not already have them:

    > For example, if your **uri** is ``https://cosmosacct.documents.azure.com:443/``, your new variable assignment will look like this: ``private static readonly string _endpointUri = "https://cosmosacct.documents.azure.com:443/";``.

    > For example, if your **primary key** is ``elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ==``, your new variable assignment will look like this: ``private static readonly string _primaryKey = "elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ==";``.

    > Keep the **URI** and **PRIMARY KEY** values recorded, you will use them again later in this lab.

1. Locate the **Main** method:

    ```csharp
    public static async Task Main(string[] args)
    { 
    }
    ```

1. Within the **Main** method, add the following lines of code to author a using block that creates and disposes a **CosmosClient** instance:

    ```csharp
    using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
    {        
    }
    ```

1. Your ``Program`` class definition should now look like this:

    ```csharp
    public class Program
    { 
        private static readonly string _endpointUri = "<your uri>";
        private static readonly string _primaryKey = "<your key>";
        public static async Task Main(string[] args)
        {    
            using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
            {
            }     
        }
    }
    ```

    > We will now execute a build of the application to make sure our code compiles successfully.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet build
    ```

    > This command will build the console project.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

### Create Database using the SDK

1. Locate the using block within the **Main** method:

    ```csharp
    using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
    {                        
    }
    ```

1. Add the following code to the method to create a new ``Database`` instance if one does not already exist:

    ```csharp
    DatabaseResponse databaseResponse = await client.CreateDatabaseIfNotExistsAsync("EntertainmentDatabase");
    Database targetDatabase = databaseResponse.Database;
    ```

    > This code will check to see if a database exists in your Azure Cosmos DB account that meets the specified parameters. If a database that matches does not exist, it will create a new database.

1. Add the following code to print out the ID of the database:

    ```csharp
    await Console.Out.WriteLineAsync($"Database Id:\t{targetDatabase.Id}");
    ```

    > The ``targetDatabase`` variable will have metadata about the database whether a new database is created or an existing one is read.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the running command.

    > In the console window, you will see the ID string for the database resource in your Azure Cosmos DB account.

1. In the open terminal pane, enter and execute the following command again:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Again, observe the output of the running command.

    > Since the database already exists, the SDK detected that the database already exists and used the existing database instance instead of creating a new instance of the database.

1. Click the **🗙** symbol to close the terminal pane.


### Create a Partitioned Container using the SDK

*To create a container, you must specify a name and a partition key path. You will specify those values when creating a container in this task. A partition key is a logical hint for distributing data onto a scaled out underlying set of physical partitions and for efficiently routing queries to the appropriate underlying partition. To learn more, refer to [/docs.microsoft.com/azure/cosmos-db/partition-data](../media/https://docs.microsoft.com/en-us/azure/cosmos-db/partition-data).*

1. Locate the using block within the **Main** method and delete any existing code:

    ```csharp
    using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
    {
    }
    ```

1. Add the following code to the method to create a reference to an existing database:

    ```csharp
    Database targetDatabase = client.GetDatabase("EntertainmentDatabase");
    ```

1. Add the following code to create a new ``IndexingPolicy`` instance with a custom indexing policy configured:

    ```csharp
    IndexingPolicy indexingPolicy = new IndexingPolicy
    {
        IndexingMode = IndexingMode.Consistent,
        Automatic = true,
        IncludedPaths =
        {
            new IncludedPath
            {
                Path = "/*"
            }
        }
    };
    ```

    > By default, all Azure Cosmos DB data is indexed. Although many customers are happy to let Azure Cosmos DB automatically handle all aspects of indexing, you can specify a custom indexing policy for containers. This indexing policy is very similar to the default indexing policy created by the SDK.

1. Add the following code to create a new ``ContainerProperties`` instance with a single partition key of ``/type`` defined and including the previously created ``IndexingPolicy``:

    ```csharp
    var containerProperties = new ContainerProperties("CustomCollection", "/type")
    {
        IndexingPolicy = indexingPolicy
    };
    ```

    > This definition will create a partition key on the ``/type`` path. Partition key paths are case sensitive. This is especially important when you consider JSON property casing in the context of .NET CLR object to JSON object serialization.

1. Add the following lines of code to create a new ``Container`` instance if one does not already exist within your database. Specify the previously created settings and a value for **throughput**:

    ```csharp
    var containerResponse = await targetDatabase.CreateContainerIfNotExistsAsync(containerProperties, 10000);
    var customContainer = containerResponse.Container;
    ```

    > This code will check to see if a container exists in your database that meets all of the specified parameters. If a container that matches does not exist, it will create a new container. Here is where we can specify the RU/s allocated for a newly created container. If this is not specified, the SDK has a default value for RU/s assigned to a container.

1. Add the following code to print out the ID of the database:

    ```csharp
    await Console.Out.WriteLineAsync($"Custom Container Id:\t{customContainer.Id}");
    ```

    > The ``customContainer`` variable will have metadata about the container whether a new container is created or an existing one is read.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the running command.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.


## Populate a Container with Items using the SDK

> You will now use the .NET SDK to populate your container with various items of varying schemas. These items will be serialized instances of multiple C# classes in your project.

### Populate Container with Data

1. In the Visual Studio Code window, look in the **Explorer** pane and verify that you have a **DataTypes.cs** file in your project folder.

    > This file contains the data classes you will be working with in the following steps.

1. Double-click the **Program.cs** link in the **Explorer** pane to open the file in the editor.

1. Locate the using block within the **Main** method and delete any existing code:

    ```csharp
    using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
    {                        
    }
    ```

1. Add the following code to the method to create a reference to an existing container:

    ```csharp
    var targetDatabase = client.GetDatabase("EntertainmentDatabase");
    var customContainer = targetDatabase.GetContainer("CustomCollection");
    ```

1. Observe the code in the **Main** method.

    > For the next few instructions, we will use the **Bogus** library to create test data. This library allows you to create a collection of objects with fake data set on each object's property. For this lab, our intent is to **focus on Azure Cosmos DB** instead of this library. With that intent in mind, the next set of instructions will expedite the process of creating test data.

1. Add the following code to create a collection of ``PurchaseFoodOrBeverage`` instances:

    ```csharp
    var foodInteractions = new Bogus.Faker<PurchaseFoodOrBeverage>()
        .RuleFor(i => i.id, (fake) => Guid.NewGuid().ToString())
        .RuleFor(i => i.type, (fake) => nameof(PurchaseFoodOrBeverage))
        .RuleFor(i => i.unitPrice, (fake) => Math.Round(fake.Random.Decimal(1.99m, 15.99m), 2))
        .RuleFor(i => i.quantity, (fake) => fake.Random.Number(1, 5))
        .RuleFor(i => i.totalPrice, (fake, user) => Math.Round(user.unitPrice * user.quantity, 2))
        .GenerateLazy(500);
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 500 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 500 items by returning a variable of type **IEnumerable**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated.
    
1. Add the following foreach block to iterate over the ``PurchaseFoodOrBeverage`` instances:

    ```csharp
    foreach(var interaction in foodInteractions)
    {
    }
    ```

1. Within the ``foreach`` block, add the following line of code to asynchronously create a container item and save the result of the creation task to a variable:

    ```csharp
    ItemResponse<PurchaseFoodOrBeverage> result = await customContainer.CreateItemAsync(interaction);
    ```

    > The ``CreateItemAsync`` method of the ``CosmosItems`` class takes in an object that you would like to serialize into JSON and store as a document within the specified container. The ``id`` property, which here we've assigned to a unique Guid on each object, is a special required value in Cosmos DB that is used for indexing and must be unique for every item in a container.

1. Still within the ``foreach`` block, add the following line of code to write the value of the newly created resource's ``id`` property to the console:

    ```csharp
    await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
    ```

    > The ``CosmosItemResponse`` type has a property named ``Resource`` that contains the object representing the item as well as other properties to give you access to interesting data about an item such as its ETag.

1. Your **Main** method should look like this:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var targetDatabase = client.GetDatabase("EntertainmentDatabase");
            var customContainer = targetDatabase.GetContainer("CustomCollection");
            var foodInteractions = new Bogus.Faker<PurchaseFoodOrBeverage>()
                .RuleFor(i => i.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(i => i.type, (fake) => nameof(PurchaseFoodOrBeverage))
                .RuleFor(i => i.unitPrice, (fake) => Math.Round(fake.Random.Decimal(1.99m, 15.99m), 2))
                .RuleFor(i => i.quantity, (fake) => fake.Random.Number(1, 5))
                .RuleFor(i => i.totalPrice, (fake, user) => Math.Round(user.unitPrice * user.quantity, 2))
                .GenerateLazy(500);
            foreach(var interaction in foodInteractions)
            {
                ItemResponse<PurchaseFoodOrBeverage> result = await customContainer.CreateItemAsync(interaction);
                await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
            }
        }     
    }
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 500 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 500 items by returning a variable of type **IEnumerable**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated. The **foreach** loop at the end of this code block iterates over the collection and creates items in Azure Cosmos DB.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of item ids associated with new items that are being created by this tool.

1. Click the **🗙** symbol to close the terminal pane.

### Populate Container with Data of Different Types

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
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var targetDatabase = client.GetDatabase("EntertainmentDatabase");
            var customContainer = targetDatabase.GetContainer("CustomCollection");
            var tvInteractions = new Bogus.Faker<WatchLiveTelevisionChannel>()
                .RuleFor(i => i.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(i => i.type, (fake) => nameof(WatchLiveTelevisionChannel))
                .RuleFor(i => i.minutesViewed, (fake) => fake.Random.Number(1, 45))
                .RuleFor(i => i.channelName, (fake) => fake.PickRandom(new List<string> { "NEWS-6", "DRAMA-15", "ACTION-12", "DOCUMENTARY-4", "SPORTS-8" }))
                .GenerateLazy(500);
            foreach(var interaction in tvInteractions)
            {
                ItemResponse<WatchLiveTelevisionChannel> result = await customContainer.CreateItemAsync(interaction);
                await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
            }
        }
    }
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 500 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 500 items by returning a variable of type **IEnumerable**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated. The **foreach** loop at the end of this code block iterates over the collection and creates items in Azure Cosmos DB.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of item ids associated with new items that are being created.

1. Click the **🗙** symbol to close the terminal pane.

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
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var targetDatabase = client.GetDatabase("EntertainmentDatabase");
            var customContainer = targetDatabase.GetContainer("CustomCollection");
            var mapInteractions = new Bogus.Faker<ViewMap>()
                .RuleFor(i => i.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(i => i.type, (fake) => nameof(ViewMap))
                .RuleFor(i => i.minutesViewed, (fake) => fake.Random.Number(1, 45))
                .GenerateLazy(500);
            foreach(var interaction in mapInteractions)
            {
                ItemResponse<ViewMap> result = await customContainer.CreateItemAsync(interaction);
                await Console.Out.WriteLineAsync($"Document Created\t{result.Resource.id}");
            }
        }
    }
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 500 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 500 items by returning a variable of type **IEnumerable**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated. The **foreach** loop at the end of this code block iterates over the collection and creates items in Azure Cosmos DB.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of item ids associated with new items that are being created.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

1. Close the Visual Studio Code application.
