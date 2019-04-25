# Authoring Azure Cosmos DB Stored Procedures using JavaScript 

In this lab, you will author and execute multiple stored procedures within your Azure Cosmos DB instance. You will explore features unique to JavaScript stored procedures such as throwing errors for transaction rollback, logging using the JavaScript console and implementing a continuation model within a bounded execution enviornment.

## Setup

> Before you start this lab, you will need to create an Azure Cosmos DB database and collection that you will use throughout the lab.

### Create Azure Cosmos DB Database and Collection

*You will now create a database and collection within your Azure Cosmos DB account.*

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/04-resource_groups.jpg)

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Overview** link on the left side of the blade.

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, select the **Create new** option and enter the value **FinancialDatabase**.

    1. Ensure the **Provision database throughput** option is not selected.

    1. In the **Collection id** field, enter the value **InvestorCollection**.

    1. In the **Partition key** field, enter the value ``/company``.

    1. In the **Throughput** field, enter the value ``1000``.

    1. Click the **OK** button.

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

### Retrieve Account Credentials

*The Data Migration Tool and .NET SDKs both require credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.*

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

1. In the **Keys** pane, record the values in the **CONNECTION STRING**, **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

    ![Credentials](../media/04-keys.jpg)

## Author Simple Stored Procedures

*You will get started in this lab by authoring simple stored procedures that implement common server-side tasks such as adding one or more documents as part of a database transaction.*

### Open Data Explorer

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

*All Azure Cosmos DB operations within a stored procedure are asynchronous and depend on JavaScript function callbacks. A **callback function** is a JavaScript function that is used as a parameter to another JavaScript function. In the context of Azure Cosmos DB, the callback function has two parameters, one for the error object in case the operation fails, and one for the created object.*

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

    > Inside the JavaScript callback, users can either handle the exception or throw an error. In case a callback is not provided and there is an error, the Azure Cosmos DB runtime throws an error. This stored procedures creates a new document and uses a nested callback function to return the document as the body of the response.

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

    > This stored procedure will use the **console.log** feature that's normally used in browser-based JavaScript to write output to the console. In the context of Azure Cosmos DB, this feature can be used to capture diagnostics logging information that can be returned after the stored procedure is executed.

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

    > Stored procedures are bound to a specific partition key. In this example, tried to execute the stored procedure within the context of the **adventureworks** partition key. Within the stored procedure, we tried to create a new document using the **contosoairlines** partition key. The stored procedure was unable to create a new document (or access existing documents) in a partition key other than the one specified when the stored procedure is executed. This caused the stored procedure to fail. You are not able to create or manipulate documents across partition keys within a stored procedure. 

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

    > Transactions are deeply and natively integrated into Cosmos DB’s JavaScript programming model. Inside a JavaScript function, all operations are automatically wrapped under a single transaction. If the JavaScript completes without any exception, the operations to the database are committed. We are going to change the stored procedure to put in a different company name for the second document. This should cause the stored procedure to fail since the second document uses a different partition key. If there is any exception that’s propagated from the script, Cosmos DB’s JavaScript runtime will roll back the whole transaction. This will effectively ensure that the first or second documents are not commited to the database.

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

## Author Stored Procedures using the Continuation Model

*You will now implement stored procedures that may execute longer than the bounded execution limits on the server. You will implement the continuation model so that the stored procedures can "pick up where they left off" after they ran out of time in a previous execution.*

### Create Bulk Upload and Bulk Delete Stored Procedures

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **bulkUpload**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function bulkUpload(docs) {
        var collection = getContext().getCollection();
        var collectionLink = collection.getSelfLink();
        var count = 0;
        if (!docs) throw new Error("The array is undefined or null.");
        var docsLength = docs.length;
        if (docsLength == 0) {
            getContext().getResponse().setBody(0);
            return;
        }
        tryCreate(docs[count], callback);
        function tryCreate(doc, callback) {
            var isAccepted = collection.createDocument(collectionLink, doc, callback);
            if (!isAccepted) getContext().getResponse().setBody(count);
        }
        function callback(err, doc, options) {
            if (err) throw err;
            count++;
            if (count >= docsLength) {
                getContext().getResponse().setBody(count);
            } else {
                tryCreate(docs[count], callback);
            }
        }
    }
    ```

    > This stored procedure uploads an array of documents in one batch. If the entire batch is not completed, the stored procedure will set the response body to the number of documents that were imported. Your client-side code is expected to call this stored procedure multiple times until all documents are imported.

    If you are having trouble copying the stored procedure above, the full source code for this stored procedure is located here: [bulk_upload.js](../solutions/05-authoring_stored_procedures/bulk_upload.js)

1. Click the **Save** button at the top of the tab.

1. Click the **New Stored Procedure** button at the top of the **Data Explorer** section.

1. In the stored procedure tab, locate the **Stored Procedure Id** field and enter the value: **bulkDelete**.

1. Replace the contents of the *stored procedure editor* with the following JavaScript code:

    ```js
    function bulkDelete(query) {
        var collection = getContext().getCollection();
        var collectionLink = collection.getSelfLink();
        var response = getContext().getResponse();
        var responseBody = {
            deleted: 0,
            continuation: true
        };
        if (!query) throw new Error("The query is undefined or null.");
        tryQueryAndDelete();
        function tryQueryAndDelete(continuation) {
            var requestOptions = {continuation: continuation};
            var isAccepted = collection.queryDocuments(collectionLink, query, requestOptions, function (err, retrievedDocs, responseOptions) {
                if (err) throw err;
                if (retrievedDocs.length > 0) {
                    tryDelete(retrievedDocs);
                } else if (responseOptions.continuation) {
                    tryQueryAndDelete(responseOptions.continuation);
                } else {
                    responseBody.continuation = false;
                    response.setBody(responseBody);
                }
            });
            if (!isAccepted) {
                response.setBody(responseBody);
            }
        }
        function tryDelete(documents) {
            if (documents.length > 0) {
                var isAccepted = collection.deleteDocument(documents[0]._self, {}, function (err, responseOptions) {
                    if (err) throw err;
                    responseBody.deleted++;
                    documents.shift();
                    tryDelete(documents);
                });
                if (!isAccepted) {
                    response.setBody(responseBody);
                }
            } else {
                tryQueryAndDelete();
            }
        }
    }
    ```

    > This stored procedure iterates through all documents that match a specific query and deletes the documents. If the stored procedure is unable to delete all documents, it will return a continuation token. Your client-side code is expected to repeatedly call the stored procedure passing in a continuation token until the stored procedure does not return a continuation token.

    If you are having trouble copying the stored procedure above, the full source code for this stored procedure is located here: [bulk_delete.js](../solutions/05-authoring_stored_procedures/bulk_delete.js)

1. Click the **Save** button at the top of the tab.

### Create a Java Project

1. On your local machine, create a new folder that will be used to contain the content of your Java project.

1. In the new folder, right-click the folder and select the **Open with Code** menu option.

    ![Open with Visual Studio Code](../media/02-open_with_code.jpg)

    > Alternatively, you can run a command prompt in your current directory and execute the ``code .`` command.

1. In the Visual Studio Code window that appears, right-click the **Explorer** under the folder you created, and select "Generate from Maven Archetype":

    ![Open in Command Prompt](../media/maven1.jpg)

1. From the options that appear, select "maven-archetype-quickstart", and then select the directory you created for the project when prompted. Maven will then prompt you to provide values for group id, artifact id, version, package. Fill these in when prompted and then confirm:

    ![Open in Command Prompt](../media/maven2.jpg)


1. Once confirmed, Maven will create the project, and provide a sample App.java. For any Java class created in the project, VS Code's Java Extension will provide "run" and "debug" links directly in the code. Clicking "run" will compile and run your Java code:

    ![Open in Command Prompt](../media/maven3.jpg)


1. To add the Maven project dependancies required to work with Cosmos DB, you should add the following into the pom.xml file located at the bottom of your project, within the dependancies section:

    ```xml
   <dependency>
      <groupId>com.microsoft.azure</groupId>
      <artifactId>azure-cosmosdb</artifactId>
      <version>2.4.3</version>
    </dependency>
    ```

1. For this tutorial, you will also need to change the source and target compiler versions to Java 1.8, as we will use some lambda syntax which is only supported from Java 8 onwards. When finished, your pom.xml should look like the below:

    ![Open in Command Prompt](../media/maven4.jpg)


1. Once the changes are applied, ensure you click file -> save all. At this point, VS Code will recognise that you modified the pom.xml build file. Ensure that you accept the prompt to sync the dependancies:

    ![Open in Command Prompt](../media/maven6.jpg)

    > Once the dependencies are pulled down, you will be ready to start writing Java code for Cosmos DB.

### Create AsyncDocumentClient Instance and Database

*The AsyncDocumentClient class is the main "entry point" to using the SQL API in Azure Cosmos DB. We are going to create an instance of the **AsyncDocumentClient** class by passing in connection metadata as parameters of the class' constructor. We will then use this class instance throughout the lab.*

1. To help generate random data in the documents, we are going to use a java library called "javafaker", which was also used in an earlier lab. If not already done so, you will need to add the following to your pom.xml file, located at the bottom of your project, within the dependancies section (ensure you accept the "synchronize the Java classpath/configuration" warning if you have not accepted this permanently):

 ```xml
     <dependency>
         <groupId>com.github.javafaker</groupId>
         <artifactId>javafaker</artifactId>
         <version>0.17.2</version>
     </dependency>  
 ```

1. At the same level as the default "App.java" file that already exists, right click and create a new file called "Person.java" . The editor will automatically open this file. Copy in the following code (be sure that the package declaration matches the classpath you have created in the above steps):

    ```java
    package test;

    import java.text.DecimalFormat;
    import java.util.ArrayList;
    import java.util.Random;

    import com.github.javafaker.Faker;
    import com.microsoft.azure.cosmosdb.Document;

    public class Person {
        Faker faker = new Faker();
        ArrayList<Object> documentDefinitions = new ArrayList<>();  
        public Person(int number) throws NumberFormatException {
            for (int i= 0; i < number;i++){  
                Document documentDefinition = new Document();          
                documentDefinition.set("firstName", faker.name().firstName());
                documentDefinition.set("lastName", faker.name().lastName());
                documentDefinition.set("company", "contosofinancial");
                String docdef = documentDefinition.toString();
                documentDefinitions.add(docdef);
            }    
        }
    }
    ```


1. Create a new file called "Program.java" (or delete the contents of the existing file if already created)

1. Within the **Program.java** editor tab, Add the package declaration (which will need to match the path you created for your maven project, if not "test" as in the sample shown here) and the following imports to the top of the editor:

    ```java
    package test;
    import java.util.ArrayList;
    import java.util.concurrent.CountDownLatch;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;
    import com.microsoft.azure.cosmosdb.ConnectionPolicy;
    import com.microsoft.azure.cosmosdb.ConsistencyLevel;
    import com.microsoft.azure.cosmosdb.DocumentCollection;
    import com.microsoft.azure.cosmosdb.PartitionKey;
    import com.microsoft.azure.cosmosdb.RequestOptions;
    import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
    import rx.Scheduler;
    import rx.schedulers.Schedulers;

    import com.microsoft.azure.cosmosdb.ConnectionMode;
    import com.microsoft.azure.cosmosdb.Document;
    import java.util.UUID;
    
    ```

1. Create a **Program** class in the Program.java file as below, with the following class variables, a public constructor and main method:

    ```java
    public class Program 
    {

        public Program() {
            //public constructor

        }
        public static void main( String[] args )
        {
 
        }
    }
    ```

1. Within the **Program** class, add the following lines of code to create variables for your connection information:

    ```java
        private final ExecutorService executorService;
        private final Scheduler scheduler;
        private AsyncDocumentClient client;

        private final String databaseName = "FinancialDatabase";
        private final String collectionId = "InvestorCollection";
    ```



1. Locate the **Program** class constructor:

    ```java
        public Program() {
            //public constructor

        }
    ```

1. Within the constructor, add the following lines of code (replacing "uri" and "key" with the values from your cosmos db account):

    ```java
        public Program() {
            //public constructor
            executorService = Executors.newFixedThreadPool(100);
            scheduler = Schedulers.from(executorService);
            client = new AsyncDocumentClient.Builder().withServiceEndpoint("uri")
            .withMasterKeyOrResourceToken("key")
            .withConnectionPolicy(ConnectionPolicy.GetDefault()).withConsistencyLevel(ConsistencyLevel.Eventual)
            .build();
        }
    ```
    > We are now going to implement a stored procedure.

### Execute Bulk Upload Stored Procedure from Async Java SDK

1. Below the **Main** method, add the following method to execute the bulkUpload stored procedure:

    ```java
    public void executeStoredProc() throws Exception {
        System.out.println("creating documents");
        ArrayList<Object> documents = new Person(500).documentDefinitions;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setScriptLoggingEnabled(true);
        requestOptions.setPartitionKey(new PartitionKey("contosofinancial"));      
        final CountDownLatch successfulCompletionLatch = new CountDownLatch(1);
        String sprocLink = "dbs/" + databaseName + "/colls/" + collectionId + "/sprocs/bulkUpload"; 
        // Execute the stored procedure
        Object docs = documents.toArray();
        Object[] storedProcedureArgs = new Object[]{docs};
        client.executeStoredProcedure(sprocLink, requestOptions, storedProcedureArgs)
                .subscribe(storedProcedureResponse -> {
                    String storedProcResultAsString = storedProcedureResponse.getResponseAsString();
                    successfulCompletionLatch.countDown();
                    System.out.println(storedProcedureResponse.getActivityId());
                }, error -> {
                    System.err.println("an error occurred while executing the stored procedure: actual cause: "
                                               + error.getMessage());
                });
        successfulCompletionLatch.await();        

    }
    ```

1. Locate the **Main** method:

    ```java
        public static void main( String[] args )
        {

        }
    ```

1. Within the **Main** method, add the following code:


    ```java
        Program p = new Program();

        try {
            p.executeStoredProc();
            System.out.println("finished");

        } catch (Exception e) {
            System.err.println(String.format("failed with %s", e));
        } 
        System.exit(0);
    ```


1. Your ``Program`` class definition should now look like this:

    ```java
        public class Program {

            private final ExecutorService executorService;
            private final Scheduler scheduler;
            private AsyncDocumentClient client;

            private final String databaseName = "FinancialDatabase";
            private final String collectionId = "InvestorCollection";


            public Program() {
                executorService = Executors.newFixedThreadPool(100);
                scheduler = Schedulers.from(executorService);
                // Sets up the requirements for each test
                ConnectionPolicy connectionPolicy = new ConnectionPolicy();
                connectionPolicy.setConnectionMode(ConnectionMode.Direct);
                client = new AsyncDocumentClient.Builder()
                        .withServiceEndpoint("uri")
                        .withMasterKeyOrResourceToken("key")
                        .withConnectionPolicy(connectionPolicy)
                        .withConsistencyLevel(ConsistencyLevel.Session)
                        .build();

                DocumentCollection collectionDefinition = new DocumentCollection();
                collectionDefinition.setId(UUID.randomUUID().toString());
            
            }

            /**
            * Create a document with a programmatically set definition, in an Async manner
            */


            public static void main(String[] args) {
                SPProgram p = new Program();

                try {
                    p.executeStoredProc();
                    System.out.println("finished");

                } catch (Exception e) {
                    System.err.println(String.format("failed with %s", e));
                } 
                System.exit(0);

            }     
            public void executeStoredProc() throws Exception {
                System.out.println("creating documents");
                ArrayList<Object> documents = new Person(500).documentDefinitions;
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.setScriptLoggingEnabled(true);
                requestOptions.setPartitionKey(new PartitionKey("contosofinancial"));            
                final CountDownLatch successfulCompletionLatch = new CountDownLatch(1);
                String sprocLink = "dbs/" + databaseName + "/colls/" + collectionId + "/sprocs/bulkUpload"; 
                // Execute the stored procedure
                Object docs = documents.toArray();
                Object[] storedProcedureArgs = new Object[]{docs};
                client.executeStoredProcedure(sprocLink, requestOptions, storedProcedureArgs)
                        .subscribe(storedProcedureResponse -> {
                            String storedProcResultAsString = storedProcedureResponse.getResponseAsString();
                            successfulCompletionLatch.countDown();
                            System.out.println(storedProcedureResponse.getActivityId());
                        }, error -> {
                            System.err.println("an error occurred while executing the stored procedure: actual cause: "
                                                    + error.getMessage());
                        });
                successfulCompletionLatch.await();        

            }
        }
    ```


1. Save all of your open editor tabs, and click run.

1. Observe the results of the console project.

    > This stored procedure will bulk upload 500 documents to your collection within the specified partition key.

1. Click the **🗙** symbol to close the terminal pane.

### Observe the Uploaded Documents in the Azure Portal

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node and then observe select the **InvestorCollection** node.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM investors i WHERE i.company = "contosofinancial"
    ```

    > To validate that our documents were uploaded, we will issue a query to select all documents with the partition key we used earlier for the stored procedure's execution.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT COUNT(1) FROM investors i WHERE i.company = "contosofinancial"
    ```

    > This query will return a count of the documents that are in the **contosofinancial** partition key.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

### Execute Bulk Delete Stored Procedure from Java Async SDK


1. Double-click the **Program.java** link in the **Explorer** pane to open the file in the editor.

1. Locate the **executeStoredProc** method and delete any existing code:

    ```csharp
    public void executeStoredProc() throws Exception {
    {    
                        
    }
    ```

1. Replace the **executeStoredProc** method with the following:

    ```java
    public void executeStoredProc() throws Exception {

        System.out.println("deleting documents");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setScriptLoggingEnabled(true);
        requestOptions.setPartitionKey(new PartitionKey("contosofinancial"));
        
        final CountDownLatch successfulCompletionLatch = new CountDownLatch(1);
        String sprocLink = "dbs/" + databaseName + "/colls/" + collectionId + "/sprocs/bulkDelete"; 

        String query = "SELECT * FROM investors i WHERE i.company = 'contosofinancial'";
        // Execute the stored procedure
 
        Object[] storedProcedureArgs = new Object[]{query};
        client.executeStoredProcedure(sprocLink, requestOptions, storedProcedureArgs)
                .subscribe(storedProcedureResponse -> {
                    String storedProcResultAsString = storedProcedureResponse.getResponseAsString();
                    successfulCompletionLatch.countDown();
                    System.out.println(storedProcedureResponse.getActivityId());
                }, error -> {
                    System.err.println("an error occurred while executing the stored procedure: actual cause: "
                                               + error.getMessage());
                });

        successfulCompletionLatch.await();        

    }
    ```

    > This code will execute the stored procedure that deletes documents.

1. Save all of your open editor tabs, and click run.

1. Observe the results of the console project.

    > This stored procedure will delete all of the documents associated with the specified partition key. In this demo, this means we will delete the documents we batch uploaded earlier.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

1. Close the Visual Studio Code application.

### Query for Documents Within a Partition Key in the Azure Portal

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **FinancialDatabase** database node and then observe select the **InvestorCollection** node.

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT COUNT(1) FROM investors i WHERE i.company = "contosofinancial"
    ```

    > This query will return a count of the documents that are in the **contosofinancial** partition key. This count should verify that all documents were deleted.

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. Close your browser application.
