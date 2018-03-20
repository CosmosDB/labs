# Querying An Azure Cosmos DB Database using the SQL API

## Exercise 0: Setup

> Prior to starting this lab, we will create an Azure Cosmos DB account, database and container. We will then populate this account with placeholder data for the lab.

### Task I: Download Required Files

1. Download the [students.json](files/students.json) file and save it to your local machine. This file will be used later to import documents into your collection.

### Task II: Create Azure Cosmos DB Assets

1. In a new window, sign in to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Create a resource** link.

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

    1. Click the **Next** button.

1. In the **Advanced** step of the tool, leave the existing options set to their default values and click the **Next** button.

1. In the **Summary** step of the tool, review your options and then click the **Import** button.

1. Wait for the import process to complete. This step can take two to five minutes.

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

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE s.studentAlias FROM students s WHERE s.enrollmentYear = 2016
    ```

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the aliases of students in the particular collection.

### Task III: Use Built-In Functions

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT CONCAT(s.studentAlias, '@contoso.edu') AS email FROM students s WHERE s.enrollmentYear = 2015
    ```

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the e-mail address of students in the particular collection.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE CONCAT(s.studentAlias, '@contoso.edu') FROM students s WHERE s.enrollmentYear = 2014
    ```

1. In the **Results** pane, observe the results of your query.

## Exercise 2: Running Intra-document Queries

### Task I: Query Intra-document Array

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT clubs
    FROM students s
    JOIN clubs IN s.clubs
    WHERE s.enrollmentYear = 2018
    ```

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a list of all clubs that new students are participating in.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE clubs
    FROM students s
    JOIN clubs IN s.clubs
    WHERE s.enrollmentYear = 2018
    ```

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a more useful JSON array of string values.

### Task II: Execute Cross-Platform Query

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE clubs
    FROM students s
    JOIN clubs IN s.clubs
    ```

1. In the **Results** pane, observe the amount of time required to fetch the query and the RU charge.

1. Observe the results of your query.

    > This query should have returned all clubs for all students. You will quickly notice that the list of clubs is not unique.

## Exercise 3: Projecting Query Results

### Task I: Modifying Structure of Query Results

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

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the status of all new students.

1. In the *query editor*, replace the current query with the following query:

    ```sql
    SELECT VALUE {
        "id": s.id,    
        "email": {
            "school": CONCAT(s.studentAlias, '@contoso.edu')
        }
    } FROM students s WHERE s.enrollmentYear = 2018
    ```

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

1. In the **Results** pane, observe the results of your query.

    > This query should have returned a JSON array containing the unfiltered contact information necessary to welcome new students.

## Exercise 4: Deploy Serverless API

### Task I: Create Serverless Assets

1. On the left side of the portal, click the **Create a resource** link.

1. At the top of the **New** blade, locate the **Search the Marketplace** field.

1. Enter the text **Function** into the search field and press **Enter**.

1. In the **Everything** search results blade, select the **Function App** result.

1. In the **Function App** blade, click the **Create** button.

1. In the new **Function App** blade, perform the following actions:

    1. In the **Name** field, enter a globally unique value.

    1. Leave the **Subscription** field set to its default value.

    1. In the **Resource group** section, select the **Use existing** option.

    1. In the **Resource group** list, select the **LABQURY** option.

    1. In the **Location** field, select the **West US** location.

    1. In the **Organization name** field, enter the value **Contoso University**.

    1. In the **Administrator email** field, enter the value **technology@contoso.edu**.

    1. In the **Pricing tier** list, select the **Developer** option.

    1. Click the **Create** button.

1. Wait for the creation task to complete before moving on with this lab. 
 
1. On the left side of the portal, click the **Create a resource** link.

1. At the top of the **New** blade, locate the **Search the Marketplace** field.

1. Enter the text **API** into the search field and press **Enter**.

1. In the **Everything** search results blade, select the **API management** result.

1. In the **API management** blade, click the **Create** button.

1. In the **API management service** blade, perform the following actions:

    1. In the **ID** field, enter a globally unique value.

    1. Leave the **Subscription** field set to its default value.

    1. In the **Resource group** section, select the **Use existing** option.

    1. In the **Resource group** list, select the **LABQURY** option.

    1. In the **Location** field, select the **West US** location.

    1. Click the **Create** button.

1. Wait for the creation task to complete before moving on with this lab.  

### Task II: Write Function App Code

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **LABQURY** *Resource Group* link.

1. In the **LABQURY** blade, select the **Function App** you recently created.

1. In the **Function Apps** blade, click the **Platform features** tab at the top of the blade.

1. In the **Platform features** tab, click the **Application Settings** link in the **General Settings** section.

1. In the **Application settings** tab, scroll down and locate the **Application Settings** section. Click the **Add new setting** button. Perform the following actions:

    a. In the **Enter a name** field, enter the value **EndpointUrl**.

    a. In the **Enter a value** field, enter the value you previously recorded for the Azure Cosmos DB account's **URI**.

1. Back in the **Application Settings** section, click the **Add new setting** button. Perform the following actions:

    a. In the **Enter a name** field, enter the value **AuthorizationKey**.

    a. In the **Enter a value** field, enter the value you previously recorded for the Azure Cosmos DB account's **PRIMARY KEY**.

1. Back in the **Application Settings** section, click the **Add new setting** button. Perform the following actions:

    a. In the **Enter a name** field, enter the value **DatabaseId**.

    a. In the **Enter a value** field, enter the value **UniversityDatabase**.

1. Back in the **Application Settings** section, click the **Add new setting** button. Perform the following actions:

    a. In the **Enter a name** field, enter the value **CollectionId**.

    a. In the **Enter a value** field, enter the value **StudentCollection**.

1. Click the **Save** button at the top of the **Application settings** tab.

1. Back in the **Function Apps** blade, locate and click the **Functions** link on the left side of the blade.

1. At the top of the **Functions** pane, click the **New function** button.

1. In the *Templates* pane, perform the following actions:

    1. In the **Language** list, select the **C#** option.

    1. In the **Scenario** list, select the **API & Webhooks** option.

    1. Select the **HTTP trigger** template.

1. In the **HTTP trigger** popup that appears, perform the following actions:

    1. In the **Language** list, select the **C#** option.

    1. In the **Name** field, enter the name **RetrieveFilteredStudents**.

    1. In the **Authorization level** list, select the **Function** option.

    1. Click the **Create** button.

1. After the *Function editor* appears, locate the **Functions** list on the left side of the blade. Click the **Integrate** button under the **RetrieveFilteredStudents** node.

1. In the *Integrations* pane, perform the following actions:

    1. In the **Request parameter name** field, enter the value **request**.

    1. In the **Selected HTTP methods** section, ensure only the **GET** option is selected.

    1. Leave all other options set to their default values.

    1. Click the **Save** button.

1. Location the **Functions** list on the left side of the blade again. Click the **Manage** button under the **RetrieveFilteredStudents** node.

1. In the *Manage* pane, perform the following actions:

    1. Click the **Add new function key** button.

    1. In the **NAME** field, enter the name **apiManagementKey**.

    1. In the **VALUE** field, enter the value **f3bSmEgZstlaWqcHlXrX9csLEAAp4NmXDvvzPxHFzMD0ehgnYUbt7l==**.

    1. In the **ACTIONS** section, click the **Save** button.

1. Location the **Functions** list on the left side of the blade again. Click the **RetrieveFilteredStudents** node.

1. In the *Function editor*, click the **View files** link on the right side of the editor.

1. In the **View files** popup that appears, click the **Add** button. Enter the name **project.json** for the new file.

1. In the *open editor for **project.json***, enter the following JSON object:

    ```
    {
        "frameworks": {
            "net46":{
                "dependencies": {
                    "Microsoft.Azure.DocumentDB": "1.21.1"
                }
            }
        }
    }
    ```

1. Click the **Save** button at the top of the editor.

1. Back in the **View files** popup, click the **run.csx** file.

1. In the *open editor for **run.csx***, enter the following C# code:

    ```
    using System.Configuration;
    using System.Net;
    using Microsoft.Azure.Documents;
    using Microsoft.Azure.Documents.Client;
    using Microsoft.Azure.Documents.Linq;

    public static async Task<HttpResponseMessage> Run(HttpRequestMessage request, TraceWriter log)
    {
        string endpointUrl = ConfigurationManager.AppSettings["EndPointUrl"];
        string authorizationKey = ConfigurationManager.AppSettings["AuthorizationKey"];
        string databaseId = ConfigurationManager.AppSettings["DatabaseId"];
        string collectionId = ConfigurationManager.AppSettings["CollectionId"];

        DocumentClient client = new DocumentClient(new Uri(endpointUrl), authorizationKey);
        Uri collectionUri = UriFactory.CreateDocumentCollectionUri(databaseId, collectionId);
        DocumentCollection collection = await client.ReadDocumentCollectionAsync(collectionUri);
        
        string sqlQuery = "SELECT VALUE { 'id': s.id, 'email': { 'school': CONCAT(s.studentAlias, '@contoso.edu') } } FROM students s WHERE s.enrollmentYear = 2018 AND s.age <= 17";

        var docQuery = client.CreateDocumentQuery<dynamic>(collection.SelfLink, sqlQuery, new FeedOptions { MaxItemCount = 50 }).AsDocumentQuery();
        List<dynamic> results = new List<dynamic>();
        while (docQuery.HasMoreResults)
        {
            results.AddRange(await docQuery.ExecuteNextAsync());
        }
        return request.CreateResponse(HttpStatusCode.OK, results);
    }
    ```

1. Click the **Save** button at the top of the editor.

### Task III: Generate Function App OpenAPI Specification and Enable CORS

1. Locate the **Function Apps** list on the left side of the blade. Click the node with the name of the *Function App* you created earlier in this lab.

1. Click the **Platform features** tab at the top of the blade.

1. In the **Platform features** tab, click the **API definition** link in the **API** section.

1. In the **API definition** tab, perform the following actions:

    1. In the **API definition source** section, select the **Function** option.

    1. In the editor that appears, click the **Generate API definition template** button.

    1. Once the OpenAPI definition template is generated in the editor, observe the content of the template.
    
    1. Click the **Save** button at the top of the tab.

1. Copy the value of the **API definition URL** field. You will use this value later in this lab.

1. Close the **API definition** tab and return to the **Platform features** tab.

1. Click the **CORS** link in the **API** section.

1. In the **CORS** popup that appears, perform the following actions:

    1. In the field at the bottom of the popup, enter the value: **\***
    
    1. Click the **Save** button at the top of the popup.

    1. Close the **CORS** popup.

### Task IV: Validate Function App

1. Locate the **Function Apps** list on the left side of the blade. Click the **RetrieveFilteredStudents** node.

1. In the *Function editor*, click the **Test** link on the right side of the editor.

1. In the **Test** popup, perform the following actions:

    1. In the **HTTP method** list, select the **GET** option.

    1. Click the **Run** button.

    > This should run your function. If successful, you should see a JSON array in the **Output** pane that contains ~941 records.

1. At the top of the *Function editor*, click the **Get function URL** hyperlink.

1. In the **Get function URL** dialog that appears, perform the following actions:

    1. In the **Key** list, select the **default (Function key)** option.

    1. Copy the entire URL in the **URL** field. You will use this value later in this lab.

    1. Click the **🗙** button to close the dialog.

1. In a new browser tab or window, navigate to <https://www.hurl.it/>.

1. In the **Hurl.it** website, perform the following actions:

    1. In the **Destination** list, select the **GET** option.

    1. In the **Destination** field, enter the Function **URL** you copied eariler in this lab.

    1. Complete the *CAPTCHA* element to prove that you are a human user.

    1. Click the **Launch Request** button.

    1. Observe the results of the request.

### Task V: Configure API Management Instance

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **LABQURY** *Resource Group* link.

1. In the **LABQURY** blade, select the **API management service** you recently created.

1. In the **API management service** blade, locate the **API Management** section on the left side of the blade and click the **APIs** link.

1. In the **APIs** blade, click the **OpenAPI specification** option.

1. In the **Create from Function App** dialog that appears, perform the following actions:

    1. In the **OpenAPI specification** field, enter the **API definition URL** you recorded earlier in this lab.
    
    1. In the **Display name** field, enter the value **Contoso University Marketing API**.

    1. In the **Name** field, enter the value **contosoedu.api.marketing**.

    1. In the **Description** field, enter the value **API for external marketing partners to access filtered student data**.

    1. In the **URL scheme** field, select the **HTTPs** option.
    
    1. In the **API URL suffix** field, enter the value **marketing**.

    1. In the **Products** field, click the field and then select the **Starter** product from the list that appears.

    1. Click the **Create** button.

1. The *API editor* will now open and show the API, operations and customizations.

1. In the *API editor*, click the **All operations** option in the list of operations.

1. Click the *pencil icon* in the **Inbound processing** section.

1. In the policy editor, click the **Set query parameters** tab.

    1. Click the **Add parameter** button.

    1. In the **NAME** field, enter the value **code**.

    1. In the **VALUE** field, enter the value **f3bSmEgZstlaWqcHlXrX9csLEAAp4NmXDvvzPxHFzMD0ehgnYUbt7l==**.

    1. In the **ACTION** list, sleect the **append** option.

    1. Click the **Save** button at the bottom of the tab.

1. Back in the *API editor*, click the **GET /api/RetrieveFilteredStudents** option in the list of operations.

1. Click the *pencil icon* in the **Outbound processing** section.

1. In the policy editor, click the **Set headers** tab.

1. In the **Set headers** tab, perform the following actions:

    1. Click the **Add header** button.

    1. In the **NAME** field, enter the value **x-ms-database**.

    1. In the **VALUE** field, enter the value **Azure Cosmos DB**.

    1. In the **ACTION** list, sleect the **append** option.

    1. Click the **Add header** button again.

    1. In the **NAME** field, enter the value **x-powered-by**.

    1. In the **VALUE** field, enter the value **Azure Functions**.

    1. In the **ACTION** list, sleect the **override** option.

    1. Click the **Save** button at the bottom of the tab.

### Task VI: Validate API Endpoint

1. Back in the *API editor*, click the **Test** tab at the top of the editor.

1. In the **Test** tab, click the **GET /api/RetrieveFilteredStudents** option in the list of operations.

1. In the testing webpage that opens to the right of the operations' list, click the **Send** button.

1. Observe the results of the test run.

### Task VII: View API Management Developer Portal

1. At the top of the *API editor*, click the **Developer portal** button.

1. In the public developer portal that opens in a new browser window, click the **APIS** link at the top.

1. Select the **Contoso University Marketing API** hyperlink from the list of available APIs.

1. The default operation (GET /api/RetrieveFilteredStudents) is selected by default. Click the **Try it** button.

1. At the bottom of the *Try it* page, click the **Send** button.

1. Observe the results of the test run.

## Exercise 5: Lab Cleanup

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