# Querying An Azure Cosmos DB Database using the SQL API

## Required Software

| Software | Download Link |
| --- | --- |
| .NET Core 2.1 (or greater) SDK | [/download.microsoft.com/dotnet-sdk-2.1](https://download.microsoft.com/download/E/2/6/E266C257-F7AF-4E79-8EA2-DF26031C84E2/dotnet-sdk-2.1.103-win-gs-x64.exe)
| Visual Studio Code | [/code.visualstudio.com/download](https://go.microsoft.com/fwlink/?Linkid=852157) |
| Azure Cosmos DB Data Migration Tool | [/cosmosdb-data-migration-tool](../files/cosmosdt.zip) |

## Exercise 0: Setup

Before starting any lab in this workshop, you will need to create the various Azure resources necessary to complete the lab. In this exercise, you will create an Azure Cosmos DB account, database and collection and then populate the collection with a collection of JSON documents.

### Task I: Download Required Files

*A JSON file has been provided that will contain a collection 50,000 students. You will use this file later to import documents into your collection.*

1. Download the [students.json](../files/students.json) file and save it to your local machine.

### Task II: Create Azure Cosmos DB Assets

*You will now create an Azure Cosmos DB account to use in this lab.*

1. In a new window, sign in to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Create a resource** link.

    ![Create a resource](../media/04-create_a_resource.png)

1. At the top of the **New** blade, locate the **Search the Marketplace** field.

    ![Search the Marketplace](../media/04-search_the_marketplace.png)

1. Enter the text **Cosmos** into the search field and press **Enter**.

1. In the **Everything** search results blade, select the **Azure Cosmos DB** result.

    ![Cosmos search results](../media/04-cosmos_search_result.png)

1. In the **Azure Cosmos DB** blade, click the **Create** button.

    ![Create Cosmos instance](../media/04-create_cosmos.png)

1. In the new **Azure Cosmos DB** blade, perform the following actions:

    1. In the **ID** field, enter a globally unique value.

    1. In the **API** list, select the **SQL** option.

    1. Leave the **Subscription** field set to its default value.

    1. In the **Resource group** section, select the **Create new** option.

    1. In the **Resource group** section, enter the value **LABQURY**  into the empty field.

    1. In the **Location** field, select the **West US** location.

    1. Click the **Create** button.

    ![Create Cosmos instance](../media/04-create_cosmos_settings.png)

1. Wait for the creation task to complete before moving on with this lab.  

### Task III: Create Azure Cosmos DB Database and Collection

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

    1. In the **Database id** field, enter the value **UniversityDatabase**.

    1. In the **Collection id** field, enter the value **StudentCollection**.

    1. In the **Storage capacity** section, select the **Unlimited** option.

    1. In the **Partition key** field, enter the value ``/enrollmentYear``.

    1. In the **Throughput** field, enter the value ``10000``.

    1. Click the **+ Add Unique Key** link.

    1. In the new **Unique Keys** field, enter the value ``/studentAlias``.

    1. Click the **OK** button.

    ![Add collection](../media/04-add_collection_settings.png)

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

### Task IV: Retrieve Account Credentials

*The Data Migration Tool and .NET SDKs both require credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.*

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

    ![Keys pane](../media/04-keys_pane.png)

1. In the **Keys** pane, record the values in the **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

    ![Credentials](../media/04-credentials.png)

### Task V: Import Lab Data Into Collection

*Finally, you will import the JSON documents contained in the **students.json** file you downloaded earlier in this lab.*

1. On your local machine, open the **Azure Cosmos DB Data Migration Tool**.

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

    1. In the **Connection String** field, enter a newly constructed connection string replacing the placeholders with values from your Azure Cosmos DB account recorded earlier in this lab: ```AccountEndpoint=[uri];AccountKey=[key];Database=[database name];```. *Make sure you replace the **[uri]**, **[key]**, and **[database name]** placeholders with the corresponding values from your Azure Cosmos DB account. For example, if your **uri** is ``https://labqury.documents.azure.com:443/``, your **key** is ``Get0qW1BrqxRUA9BskSRHti5HHzrp1cjTUJTBMIbxgCGXQgRWh5NZvhc0jIt4A5ZoljA2YiLxjNHtrfC6Bd2fA==`` and your **database's name** is ``UniversityDatabase``, then your connection string will look like this: ```AccountEndpoint=https://labqury.documents.azure.com:443/;AccountKey=Get0qW1BrqxRUA9BskSRHti5HHzrp1cjTUJTBMIbxgCGXQgRWh5NZvhc0jIt4A5ZoljA2YiLxjNHtrfC6Bd2fA==;Database=UniversityDatabase;```*

    1. Click the **Verify** button to validate your connection string.

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

    > You will know that the tool has run successfully once it has transferred 50000 records and the progress bar's animation ends. This step can take two to five minutes.

    ![Data Migration Tool - Results](../media/04-dmt_results.png)

1. Once the import process has completed, close the Azure Cosmos DB Data Migration Tool.

## Exercise 1: Executing Simple Queries

The Azure Cosmos DB Data Explorer allows you to view documents and run queries directly within the Azure Portal. In this exercise, you will use the Data Explorer to query the data stored in our collection.*

### Task I: Validate Imported Data

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

### Task II: Executing a Simple SELECT Queries

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

### Task II: Implicitly Executing a Cross-Partition Query

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

### Task III: Use Built-In Functions

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

### Task IV: Projecting Query Results

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

## Exercise 2: Use .NET SDK to Query Azure Cosmos DB

### Task 1: Create a .NET Core Project

**

1.

### Task II: Query Intra-document Array

**

1.

    ```sql
    SELECT s.clubs
    FROM students s
    WHERE s.enrollmentYear = 2018
    ```

    >

    ```sql
    SELECT activities
    FROM students s
    JOIN activities IN s.clubs
    WHERE s.enrollmentYear = 2018
    ```

    >

    ```sql
    SELECT VALUE activities
    FROM students s
    JOIN activities IN s.clubs
    WHERE s.enrollmentYear = 2018
    ```

    >

### Task III: Execute Cross-Partition Query

**

1.

    ```sql
    SELECT VALUE clubs
    FROM students s
    JOIN clubs IN s.clubs
    ```

    >

### Task IV: Projecting Query Results

**

1.

    ```sql
    SELECT {
        "id": s.id,    
        "email": {
            "school": CONCAT(s.studentAlias, '@contoso.edu')
        }
    } FROM students s WHERE s.enrollmentYear = 2018
    ```

    >

    ```sql
    SELECT {
        "id": s.id,    
        "email": {
            "school": CONCAT(s.studentAlias, '@contoso.edu')
        }
    } AS studentContact
    FROM students s WHERE s.enrollmentYear = 2018
    ```

    >

    ```sql
    SELECT VALUE {
        "id": s.id,    
        "email": {
            "school": CONCAT(s.studentAlias, '@contoso.edu')
        }
    } FROM students s WHERE s.enrollmentYear = 2018
    ```

    >

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

    >

## Exercise 3: Implement Pagination using the .NET SDK

### Task I: 

**

1.

    ```sql
    SELECT * FROM students s WHERE s.enrollmentYear = 2018
    ```

    >


## Lab Cleanup

### Task I: Open Cloud Shell

1. At the top of the portal, click the **Cloud Shell** icon to open a new shell instance.

    > If this is your first time using the cloud shell, you may need to configure the default Storage account and SMB file share.

### Task 2: Use Azure CLI to Delete Resource Group

1. In the **Cloud Shell** command prompt at the bottom of the portal, type in the following command and press **Enter** to list all resource groups in the subscription:

    ```
    az group list
    ```

1. Type in the following command and press **Enter** to delete the **LABQURY** *Resource Group*:

    ```
    az group delete --name LABQURY --no-wait --yes
    ```

1. Close the **Cloud Shell** prompt at the bottom of the portal.
