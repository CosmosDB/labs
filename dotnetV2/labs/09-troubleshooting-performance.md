# Troubleshooting Azure Cosmos DB Performance

In this lab, you will use the .NET SDK to tune Azure Cosmos DB requests to optimize the performance and cost of your application.

> If this is your first lab and you have not already completed the setup for the lab content see the instructions for [Account Setup](00-account_setup.md) before starting this lab.

## Create a .NET Core Project

1. On your local machine, locate the CosmosLabs folder in your Documents folder and open the Lab09 folder that will be used to contain the content of your .NET Core project.

1. In the Lab09 folder, right-click the folder and select the **Open with Code** menu option.

    > Alternatively, you can run a terminal in your current directory and execute the ``code .`` command.

1. In the Visual Studio Code window that appears, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

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

1. In the **Explorer** pane verify that you have a **DataTypes.cs** file in your project folder.

    > This file contains the data classes you will be working with in the following steps.

1. Double-click the **Program.cs** link in the **Explorer** pane to open the file in the editor.

1. For the ``_endpointUri`` variable, replace the placeholder value with the **URI** value and for the ``_primaryKey`` variable, replace the placeholder value with the **PRIMARY KEY** value from your Azure Cosmos DB account. Use [these instructions](00-account_setup.md) to get these values if you do not already have them:

    > For example, if your **uri** is ``https://cosmosacct.documents.azure.com:443/``, your new variable assignment will look like this: ``private static readonly string _endpointUri = "https://cosmosacct.documents.azure.com:443/";``.

    > For example, if your **primary key** is ``elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ==``, your new variable assignment will look like this: ``private static readonly string _primaryKey = "elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ==";``.
    
    > We will now execute build the application to make sure our code compiles successfully.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet build
    ```

    > This command will build the console project.

## Examining Response Headers

*Azure Cosmos DB returns various response headers that can give you more metadata about your request and what operations occurred on the server-side. The .NET SDK exposes many of these headers to you as properties of the ``ResourceResponse<>`` class.*

### Observe RU Charge for Large Item

1. Locate the using block within the **Main** method:

    ```csharp
    using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
    {
        var database = client.GetDatabase(_databaseId);
        var peopleContainer = database.GetContainer(_peopleCollectionId);
        var transactionContainer = database.GetContainer(_transactionCollectionId);

    }
    ```
    
1. After the last line of code in the using block, add a new line of code to create a new object and store it in a variable named **member**:

    ```csharp
    object member = new Member { accountHolder = new Bogus.Person() };
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

1. Add a new line of code to invoke the **CreateItemAsync** method of the **Container** instance using the **member** variable as a parameter:

    ```csharp
    ItemResponse<object> response = await peopleContainer.CreateItemAsync(member);
    ```

1. After the last line of code in the using block, add a new line of code to print out the value of the **RequestCharge** property of the **ItemResponse<>** instance:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");
    ```

1. Your **Main** method should now look like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);
            object member = new Member { id = "example.document", accountHolder = new Bogus.Person() };
            ItemResponse<object> response = await peopleContainer.CreateItemAsync(member);
            await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");
        }
    }
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

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

1. In the **Resource groups** blade, locate and select the **cosmoslab** *Resource Group*.

1. In the **cosmoslab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node and then select the **PeopleCollection** node.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT TOP 2 * FROM coll ORDER BY coll._ts DESC
    ```

    > This query will return the latest two items added to your container.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. Return to the currently open **Visual Studio Code** editor containing your .NET Core project.

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. To view the RU charge for inserting a very large document, we will use the **Bogus** library to create a fictional family on our Member object. To create a fictional family, we will generate a spouse and an array of 4 fictional children:

    ```js
    {
        "accountHolder":  { ... }, 
        "relatives": {
            "spouse": { ... }, 
            "children": [
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
    object member = new Member { accountHolder = new Bogus.Person() };
    ```

    Replace that line of code with the following code:

    ```csharp
    object member = new Member
    {
        accountHolder = new Bogus.Person(),
        relatives = new Family
        {
            spouse = new Bogus.Person(),
            children = Enumerable.Range(0, 4).Select(r => new Bogus.Person())
        }
    };
    ```

    > This new block of code will create the large JSON object discussed above.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

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

1. In the **Resource groups** blade, locate and select the **cosmoslab** *Resource Group*.

1. In the **cosmoslab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node and then select the **PeopleCollection** node.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM coll WHERE IS_DEFINED(coll.relatives)
    ```

    > This query will return the only item in your container with a property named **Children**.

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
        "excludedPaths": [
            {
                "path":"/\"_etag\"/?"
            }
        ]
    }
    ```

    > This policy will index all paths in your JSON document. This policy implements maximum precision (-1) for both numbers (max 8) and strings (max 100) paths. This policy will also index spatial data.

1. Replace the indexing policy with a new policy that removes the ``/relatives/*`` path from the index:

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
                "path":"/\"_etag\"/?"
            },
            {
                "path":"/relatives/*"
            }
        ]
    }
    ```

    > This new policy will exclude the ``/relatives/*`` path from indexing effectively removing the **Children** property of your large JSON document from the index.

1. Click the **Save** button at the top of the section to persist your new indexing policy and "kick off" a transformation of the container's index.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM coll WHERE IS_DEFINED(coll.relatives)
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

    > You will see immediately that you can still determine if the **/relatives** path is defined.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM coll WHERE IS_DEFINED(coll.relatives) ORDER BY coll.relatives.Spouse.FirstName
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

    > This query will fail immediately since this property is not indexed. Keep in mind when defining indexes that only indexed properties can be used in query conditions. 

1. Return to the currently open **Visual Studio Code** editor containing your .NET Core project.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the console project.

    > You should see a difference in the number of RUs required to create this item. This is due to the indexer skipping the paths you excluded.

1. Click the **🗙** symbol to close the terminal pane.

## Troubleshooting Requests

*First, you will use the .NET SDK to issue request beyond the assigned capacity for a container. Request unit consumption is evaluated at a per-second rate. For applications that exceed the provisioned request unit rate, requests are rate-limited until the rate drops below the provisioned throughput level. When a request is rate-limited, the server preemptively ends the request with an HTTP status code of ``429 RequestRateTooLargeException`` and returns the ``x-ms-retry-after-ms`` header. The header indicates the amount of time, in milliseconds, that the client must wait before retrying the request. You will observe the rate-limiting of your requests in an example application.*

### Reducing R/U Throughput for a Container

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmoslab** *Resource Group*.

1. In the **cosmoslab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node, expand the **TransactionCollection** node, and then select the **Scale & Settings** option.

1. In the **Settings** section, locate the **Throughput** field and update it's value to **400**.

    > This is the minimum throughput that you can allocate to a container.

1. Click the **Save** button at the top of the section to persist your new throughput allocation.

### Observing Throttling (HTTP 429)

1. Return to the currently open **Visual Studio Code** editor containing your .NET Core project.

1. Double-click the **Program.cs** link in the **Explorer** pane to open the file in the editor.

1. Locate the *using* block within the **Main** method and delete the code added for the previous section so it again looks like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);

        }
    }
    ```

    > For the next few instructions, we will use the **Bogus** library to create test data. This library allows you to create a collection of objects with fake data set on each object's property. For this lab, our intent is to **focus on Azure Cosmos DB** instead of this library. With that intent in mind, the next set of instructions will expedite the process of creating test data.

1. Add the following code to create a collection of ``Transaction`` instances:

    ```csharp
    var transactions = new Bogus.Faker<Transaction>()
        .RuleFor(t => t.id, (fake) => Guid.NewGuid().ToString())
        .RuleFor(t => t.amount, (fake) => Math.Round(fake.Random.Double(5, 500), 2))
        .RuleFor(t => t.processed, (fake) => fake.Random.Bool(0.6f))
        .RuleFor(t => t.paidBy, (fake) => $"{fake.Name.FirstName().ToLower()}.{fake.Name.LastName().ToLower()}")
        .RuleFor(t => t.costCenter, (fake) => fake.Commerce.Department(1).ToLower())
        .GenerateLazy(100);
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 100 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 100 items by returning a variable of type **IEnumerable<Transaction>**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated.
    
1. Add the following foreach block to iterate over the ``Transaction`` instances:

    ```csharp
    foreach(var transaction in transactions)
    {
    }
    ```

1. Within the ``foreach`` block, add the following line of code to asynchronously create an item and save the result of the creation task to a variable:

    ```csharp
    ItemResponse<Transaction> result = await transactionContainer.CreateItemAsync(transaction);
    ```

    > The ``CreateItemAsync`` method of the ``Container`` class takes in an object that you would like to serialize into JSON and store as an item within the specified collection.

1. Still within the ``foreach`` block, add the following line of code to write the value of the newly created resource's ``id`` property to the console:

    ```csharp
    await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
    ```

    > The ``ItemResponse`` type has a property named ``Resource`` that can give you access to the item instance resulting from the operation.

1. Your **Main** method should look like this:

    ```csharp
    public static async Task Main(string[] args)
    {    
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);
            var transactions = new Bogus.Faker<Transaction>()
                .RuleFor(t => t.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(t => t.amount, (fake) => Math.Round(fake.Random.Double(5, 500), 2))
                .RuleFor(t => t.processed, (fake) => fake.Random.Bool(0.6f))
                .RuleFor(t => t.paidBy, (fake) => $"{fake.Name.FirstName().ToLower()}.{fake.Name.LastName().ToLower()}")
                .RuleFor(t => t.costCenter, (fake) => fake.Commerce.Department(1).ToLower())
                .GenerateLazy(100);
            foreach (var transaction in transactions)
            {
                ItemResponse<Transaction> result = await transactionContainer.CreateItemAsync(transaction);
                await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
            }
        }
    }
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 100 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 100 items by returning a variable of type **IEnumerable<Transaction>**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated. The **foreach** loop at the end of this code block iterates over the collection and creates items in Azure Cosmos DB.

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

1. Back in the code editor tab, locate the following lines of code:

    ```csharp
    foreach (var transaction in transactions)
    {
        ItemResponse<Transaction> result = await transactionContainer.CreateItemAsync(transaction);
        await Console.Out.WriteLineAsync($"Item Created\t{result.Resource.id}");
    }
    ```

    Replace those lines of code with the following code:

    ```csharp
    List<Task<ItemResponse<Transaction>>> tasks = new List<Task<ItemResponse<Transaction>>>();
    foreach (var transaction in transactions)
    {
        Task<ItemResponse<Transaction>> resultTask = transactionContainer.CreateItemAsync(transaction);
        tasks.Add(resultTask);
    }
    Task.WaitAll(tasks.ToArray());
    foreach (var task in tasks)
    {
        await Console.Out.WriteLineAsync($"Item Created\t{task.Result.Resource.id}");
    }
    ```

    > We are going to attempt to run as many of these creation tasks in parallel as possible. Remember, our container is configured at 400 RU/s.

1. Your **Main** method should look like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);
            var transactions = new Bogus.Faker<Transaction>()
                .RuleFor(t => t.id, (fake) => Guid.NewGuid().ToString())
                .RuleFor(t => t.amount, (fake) => Math.Round(fake.Random.Double(5, 500), 2))
                .RuleFor(t => t.processed, (fake) => fake.Random.Bool(0.6f))
                .RuleFor(t => t.paidBy, (fake) => $"{fake.Name.FirstName().ToLower()}.{fake.Name.LastName().ToLower()}")
                .RuleFor(t => t.costCenter, (fake) => fake.Commerce.Department(1).ToLower())
                .GenerateLazy(100);
            List<Task<ItemResponse<Transaction>>> tasks = new List<Task<ItemResponse<Transaction>>>();
            foreach (var transaction in transactions)
            {
                Task<ItemResponse<Transaction>> resultTask = transactionContainer.CreateItemAsync(transaction);
                tasks.Add(resultTask);
            }
            Task.WaitAll(tasks.ToArray());
            foreach (var task in tasks)
            {
                await Console.Out.WriteLineAsync($"Item Created\t{task.Result.Resource.id}");
            }
        }  
    }
    ```

    > As a reminder, the Bogus library generates a set of test data. In this example, you are creating 100 items using the Bogus library and the rules listed above. The **GenerateLazy** method tells the Bogus library to prepare for a request of 100 items by returning a variable of type **IEnumerable<Transaction>**. Since LINQ uses deferred execution by default, the items aren't actually created until the collection is iterated. The **foreach** loops at the end of this code block iterates over the collection and creates asynchronous tasks. Each asynchronous task will issue a request to Azure Cosmos DB. These requests are issued in parallel and should cause an exceptional scenario since your  does not have enough assigned throughput to handle the volume of requests.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > This query should execute successfully. We are only creating 100 items and we most likely will not run into any throughput issues here.

1. Click the **🗙** symbol to close the terminal pane.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    .GenerateLazy(100);
    ```

    Replace that line of code with the following code:

    ```csharp
    .GenerateLazy(5000);
    ```

    > We are going to try and create 5000 items in parallel to see if we can hit out throughput limit.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe that the application will crash after some time.

    > This query will most likely hit our throughput limit. You will see multiple error messages indicating that specific requests have failed.

1. Click the **🗙** symbol to close the terminal pane.

### Increasing R/U Throughput to Reduce Throttling

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmoslab** *Resource Group*.

1. In the **cosmoslab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node, expand the **TransactionCollection** node, and then select the **Scale & Settings** option.

1. In the **Settings** section, locate the **Throughput** field and update it's value to **10000**.

1. Click the **Save** button at the top of the section to persist your new throughput allocation.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    .GenerateLazy(5000);
    ```

    Replace that line of code with the following code:

    ```csharp
    .GenerateLazy(10000);
    ```

    > We are going to try creating 10000 items in parallel against the new higher throughput limit.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe that the application will complete after some time.

1. Click the **🗙** symbol to close the terminal pane.

1. Return to the **Settings** section in the **Azure Portal** and change the **Throughput** value back to **400**.

1. Click the **Save** button at the top of the section to persist your new throughput allocation.

## Tuning Queries and Reads

*You will now tune your requests to Azure Cosmos DB by manipulating the SQL query and properties of the **RequestOptions** class in the .NET SDK.*

### Measuring RU Charge

1. Locate the *using* block within the **Main** method and delete the code added for the previous section so it again looks like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);

        }
    }
    ```

1. Add the following line of code that will store a SQL query in a string variable:

    ```csharp
    string sql = "SELECT TOP 1000 * FROM c WHERE c.processed = true ORDER BY c.amount DESC";
    ```

    > This query will perform a cross-partition ORDER BY and only return the top 1000 out of 50000 items.

1. Add the following line of code to create a item query instance:

    ```csharp
    FeedIterator<Transaction> query = transactionContainer.GetItemQueryIterator<Transaction>(sql);
    ```

1. Add the following line of code to get the first "page" of results:

    ```csharp
    var result = await query.ReadNextAsync();
    ```

    > We will not enumerate the full result set. We are only interested in the metrics for the first page of results.

1. Add the following lines of code to print out the Request Charge metric for the query to the console:

    ```csharp
    await Console.Out.WriteLineAsync($"Request Charge: {result.RequestCharge} RUs");
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see the **Request Charge** metric printed out in your console window.

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

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a reduction in both the **Request Charge** value.

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

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

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

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > Observe the slight differences in the metric value.

### Managing SDK Query Options

1. Locate the *using* block within the **Main** method and delete the code added for the previous section so it again looks like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);

        }
    }
    ```

1. Add the following lines of code to create variables to configure query options:

    ```csharp
    int maxItemCount = 100;
    int maxDegreeOfParallelism = 1;
    int maxBufferedItemCount = 0;
    ```

1. Add the following lines of code to configure options for a query from the variables:

    ```csharp
    QueryRequestOptions options = new QueryRequestOptions
    {
        MaxItemCount = maxItemCount,
        MaxBufferedItemCount = maxBufferedItemCount,
        MaxConcurrency = maxDegreeOfParallelism
    };
    ```

1. Add the following lines of code to write various values to the console window:

    ```csharp
    await Console.Out.WriteLineAsync($"MaxItemCount:\t{maxItemCount}");
    await Console.Out.WriteLineAsync($"MaxDegreeOfParallelism:\t{maxDegreeOfParallelism}");
    await Console.Out.WriteLineAsync($"MaxBufferedItemCount:\t{maxBufferedItemCount}");
    ```

1. Add the following line of code that will store a SQL query in a string variable:

    ```csharp
    string sql = "SELECT * FROM c WHERE c.processed = true ORDER BY c.amount DESC";
    ```

    > This query will perform a cross-partition ORDER BY on a filtered result set.

1. Add the following line of code to create and start new a high-precision timer:

    ```csharp
    Stopwatch timer = Stopwatch.StartNew();
    ```

1. Add the following line of code to create a item query instance:

    ```csharp
    FeedIterator<Transaction> query = transactionContainer.GetItemQueryIterator<Transaction>(sql, requestOptions: options);
    ```

1. Add the following lines of code to enumerate the result set.

    ```csharp
    while (query.HasMoreResults)  
    {
        var result = await query.ReadNextAsync();
    }
    ```

    > Since the results are paged, we will need to call the ``ReadNextAsync`` method multiple times in a while loop.

1. Add the following line of code stop the timer:

    ```csharp
    timer.Stop();
    ```

1. Add the following line of code to write the timer's results to the console window:

    ```csharp
    await Console.Out.WriteLineAsync($"Elapsed Time:\t{timer.Elapsed.TotalSeconds}");
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > This initial query should take an unexpectedly long amount of time. This will require us to optimize our SDK options.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    int maxDegreeOfParallelism = 1;
    ```

    Replace that line of code with the following:

    ```csharp
    int maxDegreeOfParallelism = 5;
    ```

    > Setting the ``maxConcurrency`` query parameter to a value of ``1`` effectively eliminates parallelism. Here we "bump up" the parallelism to a value of ``5``.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a slight difference considering you now have some form of parallelism.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    int maxBufferedItemCount = 0;
    ```

    Replace that line of code with the following code:

    ```csharp
    int maxBufferedItemCount = -1;
    ```

    > Setting the ``MaxBufferedItemCount`` property to a value of ``-1`` effectively tells the SDK to manage this setting.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > Again, this should have a slight impact on your performance time.
1. Back in the code editor tab, locate the following line of code:

    ```csharp
    int maxDegreeOfParallelism = 5;
    ```

    Replace that line of code with the following code:

    ```csharp
    int maxDegreeOfParallelism = -1;
    ```

    > Parallel query works by querying multiple partitions in parallel. However, data from an individual partitioned container is fetched serially with respect to the query setting the ``maxConcurrency`` property to a value of ``-1`` effectively tells the SDK to manage this setting. Setting the **MaxDegreeOfParallelism** to the number of partitions has the maximum chance of achieving the most performant query, provided all other system conditions remain the same.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > Again, this should have a slight impact on your performance time.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    int maxItemCount = 100;
    ```

    Replace that line of code with the following code:

    ```csharp
    int maxItemCount = 500;
    ```

    > We are increasing the amount of items returned per "page" in an attempt to improve the performance of the query.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You will notice that the query performance improved dramatically. This may be an indicator that our query was bottlenecked by the client computer.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    int maxItemCount = 500;
    ```

    Replace that line of code with the following code:

    ```csharp
    int maxItemCount = 1000;
    ```

    > For large queries, it is recommended that you increase the page size up to a value of 1000.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > By increasing the page size, you have sped up the query even more.

1. Back in the code editor tab, locate the following line of code:

    ```csharp
    int maxBufferedItemCount = -1;
    ```

    Replace that line of code with the following code:

    ```csharp
    int maxBufferedItemCount = 50000;
    ```

    > Parallel query is designed to pre-fetch results while the current batch of results is being processed by the client. The pre-fetching helps in overall latency improvement of a query. **MaxBufferedItemCount** is the parameter to limit the number of pre-fetched results. Setting MaxBufferedItemCount to the expected number of results returned (or a higher number) allows the query to receive maximum benefit from pre-fetching.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > This change should have decreased your query time by a small amount.

1. Click the **🗙** symbol to close the terminal pane.

### Reading and Querying Items

1. Locate the *using* block within the **Main** method and delete the code added for the previous section so it again looks like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);

        }
    }
    ```

1. Add the following line of code that will store a SQL query in a string variable:

    ```csharp
    string sql = "SELECT TOP 1 * FROM c WHERE c.id = 'example.document'";
    ```

    > This query will find a single item matching the specified unique id

1. Add the following line of code to create a item query instance:

    ```csharp
    FeedIterator<object> query = peopleContainer.GetItemQueryIterator<object>(sql);
    ```

1. Add the following line of code to get the first page of results and then store them in a variable of type **FeedResponse<>**:

    ```csharp
    FeedResponse<object> response = await query.ReadNextAsync();
    ```

    > We only need to retrieve a single page since we are getting the ``TOP 1`` items from the .

1. Add the following lines of code to print out the value of the **RequestCharge** property of the **FeedResponse<>** instance and then the content of the retrieved item:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs for ");    
    await Console.Out.WriteLineAsync($"{response.Resource.First()}");
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see the amount of RUs used to query for the item. Make note of the **LastName** object property value as you will use it in the next step.

1. Click the **🗙** symbol to close the terminal pane.

1. Locate the *using* block within the **Main** method and delete the code added for the previous section so it again looks like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);

        }
    }
    ```

1. Add the following code to use the **ReadItemAsync** method of the **Container** class to retrieve an item using the unique id and the partition key set to the last name from the previous step:

    ```csharp
    ItemResponse<object> response = await peopleContainer.ReadItemAsync<object>("example.document", new PartitionKey("<Last Name>"));
    ```

1. Add the following line of code to print out the value of the **RequestCharge** property of the **ItemResponse<>** instance:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");    
    ```
   
1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see that it took fewer RUs to obtain the item directly if you have it's unique id.

1. Click the **🗙** symbol to close the terminal pane.

## Setting Throughput for Expected Workloads

*Using appropriate RU settings for container or database throughput can allow you to meet desired performance at minimal cost. Deciding on a good baseline and varying settings based on expected usage patterns are both strategies that can help.*

### Estimating Throughput Needs

1. We'll start out by looking at actual usage of your Cosmos account for these labs. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmoslab** *Resource Group*.

1. In the **cosmoslab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Metrics** link on the left side of the blade under the **Monitoring** section. Observe the values in the *Number of requests* graph to see the volume of requests your lab work has been making to your Cosmos containers.

    ![Metrics](../media/09-metrics.jpg)

    > Various parameters can be changed to adjust the data shown in the graphs and there is also an option to export data to csv for further analysis. For an existing application this can be helpful in determining your query volume.

1. Return to the Visual Studio Code window and locate the *WriteLineAsync* line within the **Main** method in **Program.cs**:

    ```csharp
    await Console.Out.WriteLineAsync($"{response.RequestCharge} RUs");    
    ```

1. Following that line, add the following code to use the **CreateItemAsync** method of the **Container** class to add a new item and print out the value of the **RequestCharge** property of the **ItemResponse<>** instance:

    ```csharp
    object member = new Member { accountHolder = new Bogus.Person() };
    ItemResponse<object> createResponse = await peopleContainer.CreateItemAsync(member);
    await Console.Out.WriteLineAsync($"{createResponse.RequestCharge} RUs");
    ```

1. Add the following lines of code to create variables to represent the estimated workload for our application:

    ```csharp
    int expectedWritesPerSec = 200;
    int expectedReadsPerSec = 800;
    ```

    > These types of numbers could come from planning a new application or tracking actual usage of an existing one. Details of determining workload are outside the scope of this lab.

1. Add the following line of code to print out the estimated throughput needs of our application based on our test queries:

    ```csharp
    await Console.Out.WriteLineAsync($"Estimated load: {response.RequestCharge * expectedReadsPerSec + createResponse.RequestCharge * expectedWritesPerSec} RU per sec");
    ```

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see the total throughput needed for our application based on our estimates, which can then be used to guide our provisioned throughput setting.

1. Click the **🗙** symbol to close the terminal pane.

### Adjusting for Usage Patterns

*Many applications have workloads that vary over time in a predictable way. For example, business applications that have a heavy workload during a 9-5 business day but minimal usage outside of those hours. Cosmos throughput settings can also be varied to match this type of usage pattern.*

1. Locate the *using* block within the **Main** method and delete the code added for the previous section:

1. Locate the *using* block within the **Main** method and delete the code added for the previous section so it again looks like this:

    ```csharp
    public static async Task Main(string[] args)
    {
        using (CosmosClient client = new CosmosClient(_endpointUri, _primaryKey))
        {
            var database = client.GetDatabase(_databaseId);
            var peopleContainer = database.GetContainer(_peopleCollectionId);
            var transactionContainer = database.GetContainer(_transactionCollectionId);

        }
    }
    ```

1. Add the following code to retrieve the current RU/sec setting for the container:

    ```csharp
    ThroughputResponse response = await peopleContainer.ReadThroughputAsync();
    int? current = response.Resource.Throughput;
    ```

    > Note that the type of the **Throughput** property is a nullable value. Provisioned throughput can be set either at the container or database level. If set at the database level, this property read from the **Container** will return null. When set at the container level, the same method on **Database** will return null.

1. Add the following line of code to print out the provisioned throughput value:

    ```csharp
    await Console.Out.WriteLineAsync($"{current} RU per sec");
    ```

1. Add the following code to update the RU/sec setting for the container:

    ```csharp
    await peopleContainer.ReplaceThroughputAsync(1000);
    ```

    > Although the overall minimum throughput that can be set is 400 RU/s, specific containers or databases may have higher limits depending on size of stored data, previous maximum throughput settings, or number of containers in a database. Trying to set a value below the available minimum will cause an exception here. The current allowed minumum value can be found on the **ThroughputResponse.MinThroughput** property.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Terminal** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see the initial provisioned value before changing to **1000**.

1. Click the **🗙** symbol to close the terminal pane.

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmoslab** *Resource Group*.

1. In the **cosmoslab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node, expand the **PeopleCollection** node, and then select the **Scale & Settings** option.

1. In the **Settings** section, locate the **Throughput** field and note that is is now set to **1000**.
