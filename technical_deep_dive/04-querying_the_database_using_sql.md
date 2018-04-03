# Querying An Azure Cosmos DB Database using the SQL API

## Required Software

| Software | Download Link |
| --- | --- |
| Visual Studio Code | [code.visualstudio.com](https://go.microsoft.com/fwlink/?Linkid=852157) |
| Azure Cosmos DB Data Migration Tool | [cosmosdb-data-migration-tool](../files/cosmosdt.zip) |

## Exercise 0: Setup

*Prior to starting this lab, we will create an Azure Cosmos DB account, database and container. We will then populate this account with placeholder data for the lab.*

### Task I: Download Required Files

1. Download the [students.json](../files/students.json) file and save it to your local machine. This file will be used later to import documents into your collection.

### Task II: Create Azure Cosmos DB Assets

1. In a new window, sign in to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Create a resource** link.

    ![Create a resource](../media/04-create_a_resource.png)

1. At the top of the **New** blade, locate the **Search the Marketplace** field.

1. Enter the text **Cosmos** into the search field and press **Enter**.

1. In the **Everything** search results blade, select the **Azure Cosmos DB** result.

1. In the **Azure Cosmos DB** blade, click the **Create** button.

1. In the new **Azure Cosmos DB** blade, perform the following actions:

    1. In the **ID** field, enter a globally unique value.

    1. In the **API** list, select the **SQL** option.

    1. Leave the **Subscription** field set to its default value.

    1. In the **Resource group** section, select the **Create new** option.

    1. In the **Resource group** section, enter the value **LABQURY**  into the empty field.

    1. In the **Location** field, select the **West US** location.

    1. Click the **Create** button.

1. Wait for the creation task to complete before moving on with this lab.  

### Task III: Create Azure Cosmos DB Database and Collection

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **LABQURY** *Resource Group* link.

1. In the **LABQURY** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Overview** link on the left side of the blade.

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, enter the value **UniversityDatabase**.

    1. In the **Collection id** field, enter the value **StudentCollection**.

    1. In the **Storage capacity** section, select the **Unlimited** option.

    1. In the **Partition key** field, enter the value ``/enrollmentYear``.

    1. In the **Throughput** field, enter the value ``10000``.

    1. Click the **+ Add Unique Key** link.

    1. In the new **Unique Keys** field, enter the value ``/studentAlias``.

    1. Click the **OK** button.

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

### Task IV: Retrieve Account Credentials

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

1. In the **Keys** pane, record the values in the **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

### Task V: Import Lab Data Into Collection

1. On your local machine, open the **Azure Cosmos DB Data Migration Tool**.

1. In the **Welcome** step of the tool, click the **Next** button to begin the migration wizard.

1. In the **Source Information** step of the tool, perform the following actions:

    1. In the **Import from** list, select the **JSON file(s)** option.

    1. Click the **Add Files** button.

    1. In the *Windows Explorer* dialog that opens, locate and select the **students.json** file you downloaded earlier in this lab. Click the **Open** button to add the file.

    1. Select the **Decompress data** checkbox.

    1. Click the **Next** button.

1. In the **Target Information** step of the tool, perform the following actions:

    1. In the **Export to** list, select the **Azure Cosmos DB - Sequential record import (partitioned collection)** option.

    1. In the **Connection String** field, enter a newly constructed connection string replacing the placeholders with values from your Azure Cosmos DB account recorded earlier in this lab: ```AccountEndpoint=[uri];AccountKey=[key];Database=[database name];```. *Make sure you replace the **[uri]**, **[key]**, and **[database name]** placeholders with the corresponding values from your Azure Cosmos DB account. For example, if your **uri** is ``https://labqury.documents.azure.com:443/``, your **key** is ``Get0qW1BrqxRUA9BskSRHti5HHzrp1cjTUJTBMIbxgCGXQgRWh5NZvhc0jIt4A5ZoljA2YiLxjNHtrfC6Bd2fA==`` and your **database's name** is ``UniversityDatabase``, then your connection string will look like this: ```AccountEndpoint=https://labqury.documents.azure.com:443/;AccountKey=Get0qW1BrqxRUA9BskSRHti5HHzrp1cjTUJTBMIbxgCGXQgRWh5NZvhc0jIt4A5ZoljA2YiLxjNHtrfC6Bd2fA==;Database=UniversityDatabase;```*

    1. Click the **Verify** button to validate your connection string.

    1. In the **Collection** field, enter the value **StudentCollection**.

    1. In the **Partition Key** field, enter the value ``/enrollmentYear``.

    1. In the **Collection Throughput** field, enter the value ``10000``.

    1. Click the **Advanced Options** button.

    1. 

    1. Click the **Next** button.

1. In the **Advanced** step of the tool, leave the existing options set to their default values and click the **Next** button.

1. In the **Summary** step of the tool, review your options and then click the **Import** button.

1. Wait for the import process to complete.

    > You will know that the tool has run successfully once it has transferred 50000 records and the progress bar's animation ends. This step can take two to five minutes.

1. Once the import process has completed, close the Azure Cosmos DB Data Migration Tool.

## Exercise 1: Executing Simple Queries

### Task I: Validate Imported Data

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **LABQURY** *Resource Group* link.

1. In the **LABQURY** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **UniversityDatabase** database node and then expand the **StudentCollection** collection node. 

1. Within the **StudentCollection** node, click the **Documents** link to view a subset of the various documents in the collection. Select a few of the documents and observe the properties and structure of the documents.

### Task II: Executing SELECT Queries

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM students s WHERE s.enrollmentYear = 2017
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a page of records from a single partition.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM students s
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a page of records from a single partition.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE s.studentAlias FROM students s WHERE s.enrollmentYear = 2016
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the aliases of students in the particular collection.

### Task III: Use Built-In Functions

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT CONCAT(s.studentAlias, '@contoso.edu') AS email FROM students s WHERE s.enrollmentYear = 2015
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the e-mail address of students in the particular collection.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE CONCAT(s.studentAlias, '@contoso.edu') FROM students s WHERE s.enrollmentYear = 2014
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

### Task IV: Projecting Query Results

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT {
        "name": CONCAT(s.firstName, " ", s.lastName), 
        "isWarned": s.academicStatus.warning, 
        "isSuspended": s.academicStatus.suspension, 
        "isExpelled": s.academicStatus.expulsion
    } AS studentStatus
    FROM students s WHERE s.enrollmentYear = 2018
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the status of all new students.

## Exercise 2: Use .NET SDK to Query Azure Cosmos DB

### Task I: Query Intra-document Array

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT clubs
    FROM students s
    JOIN clubs IN s.clubs
    WHERE s.enrollmentYear = 2018
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a list of all clubs that new students are participating in.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE clubs
    FROM students s
    JOIN clubs IN s.clubs
    WHERE s.enrollmentYear = 2018
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a more useful JSON array of string values.

### Task II: Execute Cross-Partition Query

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE clubs
    FROM students s
    JOIN clubs IN s.clubs
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the amount of time required to fetch the query and the RU charge.

1. Observe the results of your query.

    > This query should have returned all clubs for all students. You will quickly notice that the list of clubs is not unique.

### Task III: Projecting Query Results

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE {
        "id": s.id,    
        "email": {
            "school": CONCAT(s.studentAlias, '@contoso.edu')
        }
    } FROM students s WHERE s.enrollmentYear = 2018
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the filtered contact information necessary to welcome new students.

1. In the *query editor*, replace the current query with the following query:

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

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the unfiltered contact information necessary to welcome new students.

## Exercise 3: Implement Pagination using the .NET SDK

### Task I: 



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
