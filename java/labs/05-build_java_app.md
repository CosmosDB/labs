## Build A Simple Java Console App

_After using the Azure Portal's **Data Explorer** to query an Azure Cosmos DB container. You are now going to use the Java SDK to issue similar queries._

> If this is your first lab and you have not already completed the setup for the lab content see the instructions for [Account Setup](00-account_setup.md) before starting this lab.

## Open the CosmosLabs Maven Project Template

1. Open Visual Studio Code.

1. If you are completing this lab through Microsoft Hands-on Labs, the CosmosLabs folder will be located at the path: **your\home\directory\Documents\CosmosLabs**. In Visual Studio Code, go to **File > Open Folder >** to get an Open Folder dialog and and use the dialog to open the CosmosLabs folder. 

    ![Open with Visual Studio Code](../media/01-vscode_open_folder.jpg)

1. The **Explorer** pane button is emphasized in red in the image below. To see where Java source for the Labs is located, go to Visual Studio Code and click the **Explorer** pane. Expand the contents of the CosmosLabs folder in Visual Studio Code; expand down to **src\main\java\com\azure\cosmos\handsonlabs\\common\\datatypes\\** and double-check that you still have datatype Java files for use in the Labs.

    ![Expand datatypes in Visual Studio Code](../media/01-vscode_expanded_datatypes.jpg)

1. Looking now two directories above, expand the **handsonlabs\\lab05\\** folder. This directory is where you will develop code for this Lab. You should see only a **Lab05Main.java** file - this is the **main** class for the project.

1. Open **Lab05Main.java** in the editor by clicking on it in the **Explorer** pane.

    ![Open Lab05Main.java in editor](../media/01-vscode-first-time-editor.jpg)

1. In the Visual Studio Code window, in the **Explorer** pane, right-click the empty space in pane and choose the **Open in Terminal** menu option.

    ![Open in terminal](../media/01-vscode_terminal.jpg)

1. Let's start by building the template code. In the open terminal pane, enter and execute the following command:

    ```sh
    mvn clean package
    ```

    > This command will build the console project.

1. Click the **🗙** symbol to close the terminal pane.

1. For the `endpointUri` variable, replace the placeholder value with the **URI** value and for the `primaryKey` variable, replace the placeholder value with the **PRIMARY KEY** value from your Azure Cosmos DB account. Use [these instructions](00-account_setup.md) to get these values if you do not already have them:

   > For example, if your **uri** is `https://cosmosacct.documents.azure.com:443/`, your new variable assignment will look like this: `private static String endpointUri = "https://cosmosacct.documents.azure.com:443/";`.

   > For example, if your **primary key** is `elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ==`, your new variable assignment will look like this: `private static String primaryKey = "elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ==";`.

   > We are now going to implement a sample query to make sure our client connection code works.

## Read a single Document in Azure Cosmos DB Using readItem()

_```readItem()``` allows a single item to be retrieved from Cosmos DB by its ID._ In Azure Cosmos DB, this is the most efficient method of reading a single document.

1. Locate the client-create/client-close block within the **main** method:

    ```java
    CosmosAsyncClient client = new CosmosClientBuilder()
            .setEndpoint(endpointUri)
            .setKey(primaryKey)
            .setConnectionPolicy(defaultPolicy)
            .setConsistencyLevel(ConsistencyLevel.EVENTUAL)
            .buildAsyncClient();

    targetDatabase = client.getDatabase("EntertainmentDatabase");
    customContainer = targetDatabase.getContainer("CustomCollection");            

    client.close();
    ```

1. Add the following lines of code to use the **readItem** function to retrieve a single item from your Cosmos DB by its `id` and write its description to the console.

    ```java
    ItemResponse<Food> candyResponse = await container.ReadItemAsync<Food>("19130", new PartitionKey("Sweets"));
    Food candy = candyResponse.Resource;
    Console.Out.WriteLine($"Read {candy.Description}");
    ```

1. Save all of your open editor tabs.

1. In the **Explorer** pane, right-click **Lab01Main.java** and choose the **Run** menu option.

    ![Run Lab01Main.java](../media/01-vscode_run.jpg)

    > This command will build and execute the console project.

1. You should see the following line output in the console, indicating that **ReadItemAsync** completed successfully:

   ```sh
   Read Candies, HERSHEY''S POT OF GOLD Almond Bar
   ```

1. Click the **🗙** symbol to close the terminal pane.

## Execute a Query Against a Single Azure Cosmos DB Partition 

1.  Find the last line of code you wrote:

    ```java
    Console.Out.WriteLine($"Read {candy.Description}");
    ```

1.  Create a SQL Query against your data, as follows:

    ```java
    string sqlA = "SELECT f.description, f.manufacturerName, f.servings FROM foods f WHERE f.foodGroup = 'Sweets' and IS_DEFINED(f.description) and IS_DEFINED(f.manufacturerName) and IS_DEFINED(f.servings)";
    ```

    > This query will select all food where the foodGroup is set to the value `Sweets`. It will also only select documents that have description, manufacturerName, and servings properties defined. You'll note that the syntax is very familiar if you've done work with SQL before. Also note that because this query has the partition key in the WHERE clause, this query can execute within a single partition.

1. Add the following code to execute and read the results of this query

   ```java
   FeedIterator<Food> queryA = container.GetItemQueryIterator<Food>(new QueryDefinition(sqlA), requestOptions: new QueryRequestOptions{MaxConcurrency = 1});
   foreach (Food food in await queryA.ReadNextAsync())
   {
       await Console.Out.WriteLineAsync($"{food.Description} by {food.ManufacturerName}");
       foreach (Serving serving in food.Servings)
       {
           await Console.Out.WriteLineAsync($"\t{serving.Amount} {serving.Description}");
       }
       await Console.Out.WriteLineAsync();
   }
   ```

1. Save all of your open editor tabs.

1. In the **Explorer** pane, right-click **Lab01Main.java** and choose the **Run** menu option.

1.  The code will loop through each result of the SQL query and output a message to the console similar to the following:

    ```sh
    ...

    Puddings, coconut cream, dry mix, instant by
        1 package (3.5 oz)
        1 portion, amount to make 1/2 cup

    ...
    ```

1. Click the **🗙** symbol to close the terminal pane.

### Execute a Query Against Multiple Azure Cosmos DB Partitions

1.  Following your `foreach` loop, create a SQL Query against your data, as follows:

    ```java
    string sqlB = @"SELECT f.id, f.description, f.manufacturerName, f.servings FROM foods f WHERE IS_DEFINED(f.manufacturerName)";
    ```

1.  Add the following line of code after the definition of `sqlB` to create your next item query:

    ```java
    FeedIterator<Food> queryB = container.GetItemQueryIterator<Food>(sqlB, requestOptions: new QueryRequestOptions{MaxConcurrency = 5, MaxItemCount = 100});
    ```

    > Take note of the differences in this call to **GetItemQueryIterator** as compared to the previous section. **maxConcurrency** is set to `5` and we are limiting the **MaxItemCount** to `100` items. This will result in paging if there are more than 100 items that match the query.

1.  Add the following lines of code to page through the results of this query using a while loop.

    ```java
    int pageCount = 0;
    while (queryB.HasMoreResults)
    {
        Console.Out.WriteLine($"---Page #{++pageCount:0000}---");
        foreach (var food in await queryB.ReadNextAsync())
        {
            Console.Out.WriteLine($"\t[{food.Id}]\t{food.Description,-20}\t{food.ManufacturerName,-40}");
        }
    }
    ```

1. Save all of your open editor tabs.

1. In the **Explorer** pane, right-click **Lab01Main.java** and choose the **Run** menu option.

1.  You should see a number of new results, each separated by the a line indicating the page, as follows:

    ```
        [19067] Candies, TWIZZLERS CHERRY BITES Hershey Food Corp.
    ---Page #0016---
        [19065] Candies, ALMOND JOY Candy Bar   Hershey Food Corp.
    ```

> Note that the results are coming from multiple partitions.

1. Click the **🗙** symbol to close the terminal pane.

> If this is your final lab, follow the steps in [Removing Lab Assets](11-cleaning_up.md) to remove all lab resources. 