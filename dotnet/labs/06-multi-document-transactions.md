# Authoring Azure Cosmos DB Stored Procedures for Multi-Document Transactions

In this lab, you will author and execute multiple stored procedures within your Azure Cosmos DB instance. You will explore features unique to JavaScript stored procedures such as throwing errors for transaction rollback, logging using the JavaScript console and implementing a continuation model within a bounded execution environment.

> If this is your first lab and you have not already completed the setup for the lab content see the instructions for [Account Setup](00-account_setup.md) before starting this lab.

## Author Simple Stored Procedures

*You will get started in this lab by authoring simple stored procedures that implement common server-side tasks such as adding one or more items as part of a database transaction.*

### Open Data Explorer

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **NutritionDatabase** database node and then expand the **FoodCollection** container node. 

1. Within the **FoodCollection** node, click the **Items** link.

### Create Simple Stored Procedure

1. Click the **New Stored Procedure** button (two gears icon) at the top of the **Data Explorer** section.

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
    
    1. If there are no param fields listed, click the **Add New Param** button.

    1. In the param field, use Type **String** and enter the value: ``Person``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > The output should be ``"Hello Person"``.

### Create Stored Procedure with Nested Callbacks

*All Azure Cosmos DB operations within a stored procedure are asynchronous and depend on JavaScript function callbacks. A **callback function** is a JavaScript function that is used as a parameter to another JavaScript function. In the context of Azure Cosmos DB, the callback function has two parameters, one for the error object in case the operation fails, and one for the created object.*

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **createDocument**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createDocument(doc) {
        var context = getContext();
        var container = context.getCollection();
        var accepted = container.createDocument(
            container.getSelfLink(),
            doc,
            function (err, newItem) {
                if (err) throw new Error('Error' + err.message);
                context.getResponse().setBody(newItem);
            }
        );
        if (!accepted) return;
    }
    ```

    > Inside the JavaScript callback, users can either handle the exception or throw an error. In case a callback is not provided and there is an error, the Azure Cosmos DB runtime throws an error. This stored procedures creates a new item and uses a nested callback function to return the item as the body of the response.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``My Recipes``.
    
    1. If there are no param fields listed, click the **Add New Param** button.

    1. In the param field, use Type **String** and enter the value: ``{ "foodGroup": "My Recipes", "description": "Cookies" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see a new item in your container. Azure Cosmos DB has assigned additional fields to the item such as ``id`` and ``_etag``.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM foods WHERE foods.foodGroup = "My Recipes" AND foods.description = "Cookies"
    ```

    > This query will retrieve the item you have just created.

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
        var container = context.getCollection();
        console.log("metadata-retrieved ");
        var accepted = container.createDocument(
            container.getSelfLink(),
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

    > This stored procedure will use the **console.log** feature that's normally used in browser-based JavaScript to write output to the console. In the context of Azure Cosmos DB, this feature can be used to capture diagnostics logging information that can be returned after the stored procedure is executed.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``My Recipes``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``{ "foodGroup": "My Recipes" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see the unique id of a new item in your container.

1. Click the *console.log* link in the **Result** pane to view the log data for your stored procedure execution.

    > You can see that the procedural components of the stored procedure finished first and then the callback function was executed once the item was created. This can help you understand the asynchronous nature of JavaScript callbacks.

### Create Stored Procedure with Callback Functions

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **createDocumentWithFunction**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createDocumentWithFunction(document) {
        var context = getContext();
        var container = context.getCollection();
        if (!container.createDocument(container.getSelfLink(), document, itemCreated))
            return;
        function itemCreated(error, newItem) {
            if (error) throw new Error('Error' + error.message);
            context.getResponse().setBody(newItem);
        }
    }
    ```

    > This is the same stored procedure as you created previously but it is using a named function instead of an implicit callback function inline.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``Packaged Foods``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``{ "foodGroup": "My Recipes" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe that the stored procedure execution has failed.

    > Stored procedures are bound to a specific partition key. In this example, we tried to execute the stored procedure within the context of the **Packaged Foods** partition key. Within the stored procedure, we tried to create a new item using the **My Recipes** partition key. The stored procedure was unable to create a new item (or access existing items) in a partition key other than the one specified when the stored procedure is executed. This caused the stored procedure to fail. You are not able to create or manipulate items across partition keys within a stored procedure.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``Packaged Foods``.
    
    1. Click the **Add New Param** button.

    1. In the new field that appears, enter the value: ``{ "foodGroup": "Packaged Foods" }``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see a new item in your container. Azure Cosmos DB has assigned additional fields to the item such as ``id`` and ``_etag``.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM foods WHERE foods.foodGroup = "Packaged Foods"
    ```

    > This query will retrieve the item you have just created.

1. Click the **Execute Query** button in the query tab to run the query.

1. In the **Results** pane, observe the results of your query.

1. Close the **Query** tab.

### Create Stored Procedure with Error Handling

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **createTwoDocuments**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createTwoDocuments(foodGroupName, foodDescription, mealName) {
        var context = getContext();
        var container = context.getCollection();
        var firstItem = {
            foodGroup: foodGroupName,
            description: foodDescription
        };
        var secondItem = {
            foodGroup: foodGroupName,
            eaten: {
                meal: mealName
            }
        };
        var firstAccepted = container.createDocument(container.getSelfLink(), firstItem, 
            function (firstError, newFirstItem) {
                if (firstError) throw new Error('Error' + firstError.message);
                var secondAccepted = container.createDocument(container.getSelfLink(), secondItem,
                    function (secondError, newSecondItem) {
                        if (secondError) throw new Error('Error' + secondError.message);      
                        context.getResponse().setBody({
                            foodRecord: newFirstItem,
                            mealRecord: newSecondItem
                        });
                    }
                );
                if (!secondAccepted) return;    
            }
        );
        if (!firstAccepted) return;    
    }
    ```

    > This stored procedure uses nested callbacks to create two separate items. You may have scenarios where your data is split across multiple JSON documents and you will need to add or modify multiple items in a single stored procedure.

1. Click the **Save** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``Vitamins``.
    
    1. Click the **Add New Param** button three times.

    1. In the first field that appears, enter the value: ``Vitamins``.

    1. In the second field that appears, enter the value: ``Calcium``.

    1. In the third field that appears, enter the value: ``Breakfast``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe the results of the stored procedure's execution.

    > You should see new items in your container. Azure Cosmos DB has assigned additional fields to the items such as ``id`` and ``_etag``.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function createTwoDocuments(foodGroupName, foodDescription, mealName) {
        var context = getContext();
        var container = context.getCollection();
        var firstItem = {
            foodGroup: foodGroupName,
            description: foodDescription
        };
        var secondItem = {
            foodGroup: foodGroupName + "_meal",
            eaten: {
                meal: mealName
            }
        };
        var firstAccepted = container.createDocument(container.getSelfLink(), firstItem, 
            function (firstError, newFirstItem) {
                if (firstError) throw new Error('Error' + firstError.message);
                console.log('Created: ' + newFirstItem.id);
                var secondAccepted = container.createDocument(container.getSelfLink(), secondItem,
                    function (secondError, newSecondItem) {
                        if (secondError) throw new Error('Error' + secondError.message); 
                        console.log('Created: ' + newSecondItem.id);                   
                        context.getResponse().setBody({
                            foodRecord: newFirstItem,
                            mealRecord: newSecondItem
                        });
                    }
                );
                if (!secondAccepted) return;    
            }
        );
        if (!firstAccepted) return;    
    }
    ```

    > Transactions are deeply and natively integrated into Cosmos DB’s JavaScript programming model. Inside a JavaScript function, all operations are automatically wrapped under a single transaction. If the JavaScript completes without any exception, the operations to the database are committed. We are going to change the stored procedure to put in a different foodGroup name for the second item. This should cause the stored procedure to fail since the second item uses a different partition key. If there is any exception that’s propagated from the script, Cosmos DB’s JavaScript runtime will roll back the whole transaction. This will effectively ensure that the first or second items are not committed to the database.

1. Click the **Update** button at the top of the tab.

1. Click the **Execute** button at the top of the tab.

1. In the **Input parameters** popup that appears, perform the following actions:

    1. In the **Partition key value** field, enter the value: ``Junk Food``.
    
    1. Click the **Add New Param** button until three param fields are listed.

    1. In the first field that appears, enter the value: ``Junk Food``.

    1. In the second field that appears, enter the value: ``Chips``.

    1. In the third field that appears, enter the value: ``Midnight Snack``.

    1. Click the **Execute** button.

1. In the **Result** pane at the bottom of the tab, observe that the stored procedure execution has failed.

    > This stored procedure failed to create the second item so the entire transaction was rolled back.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM foods WHERE foods.foodGroup = "Junk Food"
    ```

    > This query won't retrieve any items since the transaction was rolled back.

1. Click the **Execute Query** button in the query tab to run the query. You should see only an empty array.

1. In the **Results** pane, observe the results of your query.

1. Close the **Query** tab.
