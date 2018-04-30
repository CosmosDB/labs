# Authoring Azure Cosmos DB Stored Procedures using JavaScript 

## Setup

Before starting any lab in this workshop, you will need to create the various Azure resources necessary to complete the lab. In this exercise, you will create an Azure Cosmos DB account, database and collection.

### Create Azure Cosmos DB Database and Collection

*You will now create a database and collection within your Azure Cosmos DB account.*

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/04-resource_groups.png)

1. In the **Resource groups** blade, locate and select the **LABPROC** *Resource Group*.

1. In the **LABPROC** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Overview** link on the left side of the blade.

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, enter the value **FinancialDatabase**.

    1. In the **Collection id** field, enter the value **InvestorCollection**.

    1. In the **Storage capacity** section, select the **Unlimited** option.

    1. In the **Partition key** field, enter the value ``/company``.

    1. In the **Throughput** field, enter the value ``10000``.

    1. Click the **OK** button.

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

### Retrieve Account Credentials

*The Data Migration Tool and .NET SDKs both require credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.*

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

1. In the **Keys** pane, record the values in the **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

    ![Credentials](../media/04-credentials.png)

## Author Simple Stored Procedure



### Open Data Explorer

**

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node and then expand the **InvestorCollection** collection node. 

1. Within the **InvestorCollection** node, click the **Documents** link.

### Create Simple Stored Procedure

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **greetCaller**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function greetCaller(name) {
        var context = getContext();
        var response = context.getResponse();
        response.setBody("Hello " + name);
    }
    ```

    > This simple stored procedure will echo the input parameter string with the text ``Hello `` as a prefix.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``example``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``Person``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > The output should be ``"Hello Person"``.

### Create Stored Procedure with Nested Callbacks

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **createDocument**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createDocument(doc) {
        var context = getContext();
        var collection = context.getCollection();
        var accepted = collection.createDocument(
            collection.getSelfLink(),
            doc,
            function (err, newDoc) {
                if (err) throw new Error('Error' + err.message);
                context.getResponse().setBody(newDoc);
            }
        );
        if (!accepted) return;
    }
    ```

    > This stored procedures creates a new document and uses a nested callback function to returnt he document as the body of the response.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``contosoairlines``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``{ "company": "contosoairlines", "industry": "travel" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see a new document in your collection. Azure Cosmos DB has assigned additional fields to the document such as ``id`` and ``_etag``.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM investors WHERE investors.company = "contosoairlines" AND investors.industry = "travel"
    ```

    > This query will retrieve the document you have just created.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. Close the **Query** tab.

### Create Stored Procedure with Logging

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **createDocumentWithLogging**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createDocumentWithLogging(doc) {
        console.log("procedural-start ");
        var context = getContext();
        var collection = context.getCollection();
        console.log("metadata-retrieved ");
        var accepted = collection.createDocument(
            collection.getSelfLink(),
            doc,
            function (err, newDoc) {
                console.log("callback-started ");
                if (err) throw new Error('Error' + err.message);
                context.getResponse().setBody(newDoc.id);
            }
        );
        console.log("async-doc-creation-started ");
        if (!accepted) return;
        console.log("procedural-end");
    }
    ```

    >

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``contosoairlines``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``{ "company": "contosoairlines" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see the unique id of a new document in your collection.

1. Click the *console.log* link in the **Result** pane to view the log data for your stored procedure execution.

    > You can see that the procedural components of the stored procedure finished first and then the callback function was executed once the document was created. This can help you understand the asynchronous nature of JavaScript callbacks.

### Create Stored Procedure with Callback Functions

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **createDocumentWithFunction**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createDocumentWithFunction(document) {
        var context = getContext();
        var collection = context.getCollection();
        if (!collection.createDocument(collection.getSelfLink(), document, documentCreated))
            return;
        function documentCreated(error, newDocument) {
            if (error) throw new Error('Error' + error.message);
            context.getResponse().setBody(newDocument);
        }
    }
    ```

    > This is the same stored procedure as you created previously but it is using a named function instead of an implicit callback function inline.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``adventureworks``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``{ "company": "contosoairlines" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe that the stored procedure execution has failed.

    > Stored procedures are bound to a specific partition key. You are not able to create or manipulate documents across partition keys within a stored procedure.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``adventureworks``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``{ "company": "adventureworks" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see a new document in your collection. Azure Cosmos DB has assigned additional fields to the document such as ``id`` and ``_etag``.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM investors WHERE investors.company = "adventureworks"
    ```

    > This query will retrieve the document you have just created.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. Close the **Query** tab.

### Create Stored Procedure with Error Handling

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **createTwoDocuments**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createTwoDocuments(companyName, industry, taxRate) {
        var context = getContext();
        var collection = context.getCollection();
        var firstDocument = {
            company: companyName,
            industry: industry
        };
        var secondDocument = {
            company: companyName,
            tax: {
                exempt: false,
                rate: taxRate
            }
        };
        var firstAccepted = collection.createDocument(collection.getSelfLink(), firstDocument, 
            function (firstError, newFirstDocument) {
                if (firstError) throw new Error('Error' + firstError.message);
                var secondAccepted = collection.createDocument(collection.getSelfLink(), secondDocument,
                    function (secondError, newSecondDocument) {
                        if (secondError) throw new Error('Error' + secondError.message);      
                        context.getResponse().setBody({
                            companyRecord: newFirstDocument,
                            taxRecord: newSecondDocument
                        });
                    }
                );
                if (!secondAccepted) return;    
            }
        );
        if (!firstAccepted) return;    
    }
    ```

    > This stored procedure uses nested callbacks to create two seperate documents. You may have scenarios where your data is split across multiple JSON documents and you will need to add or modify multiple documents in a single stored procedure.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``abcairways``.
    
    1. Click the **Add New Param** button three times.

    1. In the first field that appears, enter the value: ``abcairways``.

    1. In the second field that appears, enter the value: ``travel``.

    1. In the third field that appears, enter the value: ``1.05``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see a new document in your collection. Azure Cosmos DB has assigned additional fields to the document such as ``id`` and ``_etag``.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createTwoDocuments(companyName, industry, taxRate) {
        var context = getContext();
        var collection = context.getCollection();
        var firstDocument = {
            company: companyName,
            industry: industry
        };
        var secondDocument = {
            company: companyName + "_taxprofile",
            tax: {
                exempt: false,
                rate: taxRate
            }
        };
        var firstAccepted = collection.createDocument(collection.getSelfLink(), firstDocument, 
            function (firstError, newFirstDocument) {
                if (firstError) throw new Error('Error' + firstError.message);
                console.log('Created: ' + newFirstDocument.id);
                var secondAccepted = collection.createDocument(collection.getSelfLink(), secondDocument,
                    function (secondError, newSecondDocument) {
                        if (secondError) throw new Error('Error' + secondError.message); 
                        console.log('Created: ' + newSecondDocument.id);                   
                        context.getResponse().setBody({
                            companyRecord: newFirstDocument,
                            taxRecord: newSecondDocument
                        });
                    }
                );
                if (!secondAccepted) return;    
            }
        );
        if (!firstAccepted) return;    
    }
    ```

    > We are going to change the stored procedure to put in a different company name for the second document. This should cause the stored procedure to fail since the second document uses a different partition key. The first document will create successfully but will be rolled back when the transaction is rolled back.

1. Click the **Update** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``jetsonairways``.
    
    1. Click the **Add New Param** button three times.

    1. In the first field that appears, enter the value: ``jetsonairways``.

    1. In the second field that appears, enter the value: ``travel``.

    1. In the third field that appears, enter the value: ``1.15``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe that the stored procedure execution has failed.

    > This stored procedure failed to create the second document so the entire transaction was rolled back.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM investors WHERE investors.company = "jetsonairways"
    ```

    > This query won't retrieve any documents since the transaction was rolled back.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. Close the **Query** tab.