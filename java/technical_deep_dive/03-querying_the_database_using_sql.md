# Querying An Azure Cosmos DB Database using the SQL API

In this lab, you will query an Azure Cosmos DB database instance using the SQL language. You will use features common in SQL such as projection using SELECT statements and filtering using WHERE clauses. You will also get to use features unique to Azure Cosmos DB's SQL API such as projection into JSON, intra-document JOIN and filtering to a range of partition keys.

## Setup

> Before you start this lab, you will need to create an Azure Cosmos DB database and collection that you will use throughout the lab. You will also use the **Azure Data Factory (ADF)** to import existing data into your collection.

### Create Azure Cosmos DB Database and Collection

*You will now create a database and collection within your Azure Cosmos DB account.*

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/03-resource_groups.jpg)

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

    ![Lab resource group](../media/03-lab_resource_group.jpg)

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

    ![Cosmos resource](../media/03-cosmos_resource.jpg)

1. In the **Azure Cosmos DB** blade, locate and click the **Overview** link on the left side of the blade.

    ![Overview pane](../media/03-overview.jpg)

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

    ![Add collection](../media/03-add_collection.jpg)

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, select the **Create new** option and enter the value **UniversityDatabase**.

    1. Ensure the **Provision database throughput** option is not selected.

        > Provisioning throughput for a database allows you to share the throughput among all the containers that belong to that database. Within an Azure Cosmos DB database, you can have a set of containers which shares the throughput as well as containers, which have dedicated throughput.

    1. In the **Collection Id** field, enter the value **StudentCollection**.

    1. In the **Storage capacity** section, select the **Unlimited** option.

    1. In the **Partition key** field, enter the value ``/enrollmentYear``.

    1. In the **Throughput** field, enter the value ``11000``.

    1. Click the **+ Add Unique Key** link.

    1. In the new **Unique Keys** field, enter the value ``/studentAlias``.

    1. Click the **OK** button.

    ![Add collection](../media/04-add_collection_settings.jpg)

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

### Retrieve Account Credentials

*The .NET SDK requires credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.*

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

    ![Keys pane](../media/03-keys_pane.jpg)

1. In the **Keys** pane, record the values in the **CONNECTION STRING**, **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

    ![Credentials](../media/03-keys.jpg)

### Import Lab Data Into Collection

You will use **Azure Data Factory (ADF)** to import the JSON array stored in the **students.json** file from Azure Blob Storage.

1. On the left side of the portal, click the **Resource groups** link.

    > To learn more about copying data to Cosmos DB with ADF, please read [ADF's documentation](https://docs.microsoft.com/en-us/azure/data-factory/connector-azure-cosmos-db). 

    ![Resource groups](../media/03-resource_groups.jpg)

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.


3. Click **add** to add a new resource

    ![Add adf](../media/03-add_adf.jpg)

4. Search for **Data Factory** and select it

    ![adf-search](../media/03-adf_search.png)

5. Create a new **Data Factory**. You should name this data factory **importstudentdata** and select the relevant Azure subscription. You should ensure your existing **cosmosdblab-group** resource group is selected as well as a Version **V2**. Select **East US** as the region. Click **create**.

    ![df](../media/03-adf_selections.jpg)

6. After creation, open your newly created Data Factory. Select **Author & Monitor** and you will launch ADF. You should see a screen similar to the screenshot below. Select **Copy Data**. We will be using ADF for a one-time copy of data from a source JSON file on Azure Blob Storage to a database in Cosmos DB’s SQL API. ADF can also be used for more frequent data transfers from Cosmos DB to other data stores.
    ![](../media/03-adf_author&monitor.jpg)
    ![](../media/03-adf_copydata.jpg)

7. Edit basic properties for this data copy. You should name the task **ImportStudents** and select to **Run once now**

   ![adf-properties](../media/03-adf_properties.jpg)

8. **Create a new connection** and select **Azure Blob Storage**. We will import data from a json file on Azure Blob Storage. In addition to Blob Storage, you can use ADF to migrate from a wide variety of sources. We will not cover migration from these sources in this tutorial.

    ![](../media/03-adf_blob.jpg)

9. Name the source **StudentsJson** and select **Use SAS URI** as the Authentication method. Please use the following SAS URI for read-only access to this Blob Storage container: https://cosmosdblabs.blob.core.windows.net/?sv=2017-11-09&ss=bfqt&srt=sco&sp=rwdlacup&se=2020-03-11T08:08:39Z&st=2018-11-10T02:08:39Z&spr=https&sig=ZSwZhcBdwLVIMRj94pxxGojWwyHkLTAgnL43BkbWKyg%3D

    ![](../media/03-adf_connecttoblob.jpg)

10. Select the **students** folder

    ![](../media/03-adf_choosestudents.jpg)

11. Ensure that **Copy file recursively** and **Binary Copy** are not checked off. Also ensure that **Compression Type** is "none".

    ![](../media/03-adf_source_next.jpg)

12. ADF should auto-detect the file format to be JSON. You can also select the file format as **JSON format.** You should also make sure you select **Array of Objects**  as the File pattern.

    ![](../media/03-adf_source_dataset_format.jpg)

13. You have now successfully connected the Blob Storage container with the students.json file. You should select **StudentsJson** as the source and click **Next**.

    ![](../media/03-adf_SourceNext.JPG)

14. Add the Cosmos DB target data store by selecting **Create new connection** and selecting **Azure Cosmos DB**.

    ![](../media/03-adf_selecttarget.jpg)

15. Name the linked service **targetcosmosdb** and select your Azure subscription and Cosmos DB account. You should also select the Cosmos DB database that you created earlier.

    ![](../media/03-adf_selecttargetdb.jpg)

16. Select your newly created **targetcosmosdb** connection as the Destination date store.

    ![](../media/03-adf_destconnectionnext.jpg)

17. Select your collection from the drop-down menu. You will map your Blob storage file to the correct Cosmos DB collection.

    ![](../media/03-adf_correcttable.jpg)

18. You should have selected to skip column mappings in a previous step. Click through this screen.

    ![](../media/03-adf_destinationconnectionfinal.jpg)

19. There is no need to change any settings. Click **next**.

    ![](../media/03-adf_settings.jpg)

20. After deployment is complete, select **Monitor**.

    ![](../media/03-adf_deployment.jpg)

21. After a few minutes, refresh the page and the status for the ImportStudents pipeline should be listed as **Succeeded**.

    ![](../media/03-adf-succeeded.jpg)

22. Once the import process has completed, close the ADF. You will now proceed to execute simple queries on your imported data. 

## Executing Simple Queries

*The Azure Cosmos DB Data Explorer allows you to view documents and run queries directly within the Azure Portal. In this exercise, you will use the Data Explorer to query the data stored in our collection.*

### Validate Imported Data

*First, you will validate that the data was successfully imported into your collection using the **Documents** view in the **Data Explorer**.*

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/03-resource_groups.jpg)

1. In the **Resource groups** blade, locate and select the **cosmosgroup-lab** *Resource Group*.

    ![Lab resource group](../media/03-lab_resource_group.jpg)

1. In the **cosmosgroup-lab** blade, select the **Azure Cosmos DB** account you recently created.

    ![Cosmos resource](../media/03-cosmos_resource.jpg)

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

    ![Data Explorer pane](../media/03-data_explorer_pane.jpg)

1. In the **Data Explorer** section, expand the **UniversityDatabase** database node and then expand the **StudentCollection** collection node. 

    ![Collection node](../media/03-collection_node.jpg)

1. Within the **StudentCollection** node, click the **Documents** link to view a subset of the various documents in the collection. Select a few of the documents and observe the properties and structure of the documents.

    ![Documents](../media/03-documents.jpg)

    ![Example document](../media/03-example_document.jpg)

### Executing a Simple SELECT Queries

*You will now use the query editor in the **Data Explorer** to execute a few simple SELECT queries using SQL syntax.*

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

    ![New SQL query](../media/03-new_query.jpg)

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT * FROM students s WHERE s.enrollmentYear = 2017
    ```

    > This first query will select all properties from all documents in the collection where the students where enrolled in 2017. You will notice that we are using the alias ``s`` to refer to the collection.

    ![Query editor](../media/03-query_editor.jpg)

1. Click the **Execute Query** button in the query tab to run the query. 

    ![Execute query](../media/03-execute_query.jpg)

1. In the **Results** pane, observe the results of your query.

    ![Query results](../media/03-query_results.jpg)

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

### Implicitly Executing a Cross-Partition Query

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

### Use Built-In Functions

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

### Projecting Query Results

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

## Use Java Async SDK to Query Azure Cosmos DB

*After using the Azure Portal's **Data Explorer** to query an Azure Cosmos DB collection, you are now going to use the Java Async SDK to issue similar queries.*

### Create a Java Core Project

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

1. At the same level as the default "App.java" file that already exists, right click and create a new file called "Program.java":

    ![Open in Command Prompt](../media/maven5.jpg)

1. Within the **Program.java** editor tab, Add the package declaration (which will need to match the path you created for your maven project, if not "test" as in the sample shown here) and the following imports to the top of the editor:

    ```java
    package test;
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.Collections;
    import java.util.Iterator;
    import java.util.List;
    import java.util.concurrent.CountDownLatch;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;
    import com.microsoft.azure.cosmosdb.ConnectionPolicy;
    import com.microsoft.azure.cosmosdb.ConsistencyLevel;
    import com.microsoft.azure.cosmosdb.DataType;
    import com.microsoft.azure.cosmosdb.Database;
    import com.microsoft.azure.cosmosdb.Document;
    import com.microsoft.azure.cosmosdb.DocumentClientException;
    import com.microsoft.azure.cosmosdb.DocumentCollection;
    import com.microsoft.azure.cosmosdb.FeedOptions;
    import com.microsoft.azure.cosmosdb.FeedResponse;
    import com.microsoft.azure.cosmosdb.IncludedPath;
    import com.microsoft.azure.cosmosdb.Index;
    import com.microsoft.azure.cosmosdb.IndexingPolicy;
    import com.microsoft.azure.cosmosdb.PartitionKeyDefinition;
    import com.microsoft.azure.cosmosdb.RequestOptions;
    import com.microsoft.azure.cosmosdb.ResourceResponse;
    import com.microsoft.azure.cosmosdb.internal.HttpConstants;
    import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
    import rx.Observable;
    import rx.Scheduler;
    import rx.schedulers.Schedulers;
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

        private final String databaseName = "UniversityDatabase";
        private final String collectionId = "StudentCollection";
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
    > We are now going to implement a simple query to make sure our client connection code works.

1. Locate the **Main** method:

    ```java
        public static void main( String[] args )
        {

        }
    ```

1. Add the following code within the main method:

    ```java
            FeedOptions options = new FeedOptions();
            // as this is a multi collection enable cross partition query
            options.setEnableCrossPartitionQuery(true);
            // note that setMaxItemCount sets the number of items to return in a single page
            // result
            options.setMaxItemCount(5);
            String sql = "SELECT TOP 5 s.studentAlias FROM coll s WHERE s.enrollmentYear = 2018 ORDER BY s.studentAlias";
            Program p = new Program();
            Observable<FeedResponse<Document>> documentQueryObservable = p.client
                            .queryDocuments("dbs/" + p.databaseName + "/colls/" + p.collectionId, sql, options);
            // observable to an iterator
            Iterator<FeedResponse<Document>> it = documentQueryObservable.toBlocking().getIterator();

            while (it.hasNext()) {
                    FeedResponse<Document> page = it.next();
                    List<Document> results = page.getResults();
                    // here we iterate over all the items in the page result
                    for (Object doc : results) {
                            System.out.println(doc);
                    }
            }
    ```

1. Your **Program** class definition now look like this:

    ```java
    public class Program {
        private final ExecutorService executorService;
        private final Scheduler scheduler;
        private AsyncDocumentClient client;

        private final String databaseName = "UniversityDatabase";
        private final String collectionId = "StudentCollection";

        private int numberOfDocuments;

        public Program() {
            // public constructor
            executorService = Executors.newFixedThreadPool(100);
            scheduler = Schedulers.from(executorService);
            client = new AsyncDocumentClient.Builder().withServiceEndpoint("uri")
                    .withMasterKeyOrResourceToken("key")
                    .withConnectionPolicy(ConnectionPolicy.GetDefault()).withConsistencyLevel(ConsistencyLevel.Eventual)
                    .build();

        }

        public static void main(String[] args) throws InterruptedException {
            FeedOptions options = new FeedOptions();
            // as this is a multi collection enable cross partition query
            options.setEnableCrossPartitionQuery(true);
            // note that setMaxItemCount sets the number of items to return in a single page
            // result
            options.setMaxItemCount(5);
            String sql = "SELECT TOP 5 s.studentAlias FROM coll s WHERE s.enrollmentYear = 2018 ORDER BY s.studentAlias";
            Program p = new Program();
            Observable<FeedResponse<Document>> documentQueryObservable = p.client
                            .queryDocuments("dbs/" + p.databaseName + "/colls/" + p.collectionId, sql, options);
            // observable to an iterator
            Iterator<FeedResponse<Document>> it = documentQueryObservable.toBlocking().getIterator();

            while (it.hasNext()) {
                    FeedResponse<Document> page = it.next();
                    List<Document> results = page.getResults();
                    // here we iterate over all the items in the page result
                    for (Object doc : results) {
                            System.out.println(doc);
                    }
            }
                        
        }
    }
    ```

1. Save all of your open editor tabs and run the code.

1. Observe the results of the console project.

    > You should see five aliases printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.


### Query Intra-document Array


1. Here you will use org.json to parse JSON in Java, so you will need to add the library as a dependency in your pom.xml file (don't forget to accept sync prompt) :

    ```xml
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20090211</version>
    </dependency> 
    ```

1. In the Visual Studio Code window, double-click the **Program.java** file to open an editor tab for the file.

1. Within the **Program.java** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```java
    String sql = "SELECT TOP 5 s.studentAlias FROM coll s WHERE s.enrollmentYear = 2018 ORDER BY s.studentAlias";
    ```

    Replace that line of code with the following code:

    ```java
    String sql = "SELECT s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    > This new query will select the **clubs** property for each student in the result set. The value of the **clubs** property is a string array.

1. Locate the following lines of code: 

    ```java
        while (it.hasNext()) {
                FeedResponse<Document> page = it.next();
                List<Document> results = page.getResults();
                // here we iterate over all the items in the page result
                for (Object doc : results) {
                        System.out.println(doc);
                }
        }
    ```

    Replace them with the following code:

    ```java
        while (it.hasNext()) {
                FeedResponse<Document> page = it.next();
                List<Document> results = page.getResults();
                for (Document doc : results) {
                        JSONObject obj = new JSONObject(doc.toJson());
                        JSONArray  student = obj.getJSONArray("clubs");
                        for (int i = 0; i < student.length(); i++) {
                                Object club = student.getString(i);
                                System.out.println(club);
                        }
                        
                }
        }
    ```

    > Our new query will need to iterate twice. First, we will iterate the collection of students and then we will iterate the collection of clubs for each student.

1. Finally, within the **Main** method, locate the following line of code: 

    ```java
    options.setMaxItemCount(5);
    ```

1. As you will be returning many more results, you will want to increase the allowed number of items in each page: 

    ```java
    options.setMaxItemCount(5000);
    ```

1. Save all of your open editor tabs, and click run. 

1. Observe the results of the console project.

    > You should see multiple club names printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Program.java** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```java
    String sql = "SELECT s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    Replace that line of code with the following code:

    ```java
    String sql = "SELECT s.firstName, s.lastName, s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    > We are now including the **firstName** and **lastName** fields in our query.

1. Locate the following block of code: 

    ```java
        while (it.hasNext()) {
                FeedResponse<Document> page = it.next();
                List<Document> results = page.getResults();
                for (Document doc : results) {
                        JSONObject obj = new JSONObject(doc.toJson());
                        JSONArray  student = obj.getJSONArray("clubs");
                        for (int i = 0; i < student.length(); i++) {
                                Object club = student.getString(i);
                                System.out.println(club);
                        }
                        
                }
        }
    ```

    Replace that block of code with the following code:

    ```java
        while (it.hasNext()) {
                FeedResponse<Document> page = it.next();
                List<Document> results = page.getResults();
                for (Document doc : results) {
                        JSONObject obj = new JSONObject(doc.toJson());
                        JSONArray  student = obj.getJSONArray("clubs");
                        String firstName = obj.getString("firstName");                          
                        String lastName = obj.getString("lastName");
                        System.out.println("***Student name***");
                        System.out.println(firstName +" "+lastName);
                        System.out.println("***Student name***");
                        for (int i = 0; i < student.length(); i++) {
                                Object club = student.getString(i);
                                System.out.println(club);
                        }
                        
                }
        }
    ```

    > This modification simply prints out more information to the console.

1. Save all of your open editor tabs, and click run. 

1. Observe the results of the console project.

1. Click the **🗙** symbol to close the terminal pane.

    > Since we only really care about the list of clubs, we want to peform a self-join that applies a cross product across the **club** properties of each student in the result set.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **New File** menu option.

1. Name the new file **StudentActivity.cs** . The editor tab will automatically open for the new file.

1. Paste in the following code for the ``StudentActivity`` class:

    ```csharp
    public class StudentActivity
    {
        public string Activity { get; set; }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT s.firstName, s.lastName, s.clubs FROM students s WHERE s.enrollmentYear = 2018";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";
    ```

    > Here we are performing an intra-document JOIN to get a projection of all clubs across all matching students.

1. Locate the following line of code: 

    ```csharp
    IQueryable<Student> query = client.CreateDocumentQuery<Student>(collectionLink, new SqlQuerySpec(sql));
    ```

    Replace that line of code with the following code:

    ```csharp
    IQueryable<StudentActivity> query = client.CreateDocumentQuery<StudentActivity>(collectionLink, new SqlQuerySpec(sql));
    ```

1. Locate the following line of code: 

    ```csharp
    foreach(Student student in query)
    {
        await Console.Out.WriteLineAsync($"{student.FirstName} {student.LastName}");
        foreach(string club in student.Clubs)
        {
            await Console.Out.WriteLineAsync($"\t{club}");
        }
        await Console.Out.WriteLineAsync();
    }
    ```

    Replace that line of code with the following code:

    ```csharp
    foreach(StudentActivity studentActivity in query)
    {
        await Console.Out.WriteLineAsync(studentActivity.Activity);
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

    > You should see multiple club names printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

    > While we did get very useful information with our JOIN query, it would be more useful to get the raw array values instead of a wrapped value. It would also make our query easier to read if we could simply create an array of strings.

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";  
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT VALUE activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";
    ```

    > Here we are using the ``VALUE`` keyword to flatten our query.

1. Locate the following line of code: 

    ```csharp
    IQueryable<StudentActivity> query = client.CreateDocumentQuery<StudentActivity>(collectionLink, new SqlQuerySpec(sql));
    ```

    Replace that line of code with the following code:

    ```csharp
    IQueryable<string> query = client.CreateDocumentQuery<string>(collectionLink, new SqlQuerySpec(sql));
    ```

1. Locate the following line of code: 

    ```csharp
    foreach(StudentActivity studentActivity in query)
    {
        await Console.Out.WriteLineAsync(studentActivity.Activity);
    }
    ```

    Replace that line of code with the following code:

    ```csharp
    foreach(string activity in query)
    {
        await Console.Out.WriteLineAsync(activity);
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

    > You should see multiple club names printed to the console window.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

### Projecting Query Results

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **New File** menu option.

1. Name the new file **StudentProfile.cs** . The editor tab will automatically open for the new file.

1. Paste in the following code for the ``StudentProfile`` and ``StudentProfileEmailInformation`` classes:

    ```csharp
    public class StudentProfile
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public StudentProfileEmailInformation Email { get; set; }
    }

    public class StudentProfileEmailInformation
    {
        public string Home { get; set; }
        public string School { get; set; }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT VALUE activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";
    ```

    Replace that line of code with the following code:

    ```csharp
    string sql = "SELECT VALUE { 'id': s.id, 'name': CONCAT(s.firstName, ' ', s.lastName), 'email': { 'home': s.homeEmailAddress, 'school': CONCAT(s.studentAlias, '@contoso.edu') } } FROM students s WHERE s.enrollmentYear = 2018"; 
    ```

    > This query will get relevant information about a student and format it to a specific JSON structure that our application expects. For your information, here's the query that we are using:

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

1. Locate the following line of code: 

    ```csharp
    IQueryable<string> query = client.CreateDocumentQuery<string>(collectionLink, new SqlQuerySpec(sql));
    ```

    Replace that code with the following code:

    ```csharp
    IQueryable<StudentProfile> query = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sql));   
    ```

1. Locate the following lines of code: 

    ```csharp
    foreach(string activity in query)
    {
        await Console.Out.WriteLineAsync(activity);
    }
    ```

    Replace that code with the following code:

    ```csharp
    foreach(StudentProfile profile in query)
    {
        await Console.Out.WriteLineAsync($"[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-50}\t{profile.Email.Home}");
    }
    ```

    > This code uses the special alignment features of C# string formatting so you can see all properties of the ``StudentProfile`` instances.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

## Implement Pagination using the .NET SDK

*You will use the **HasMoreResults** boolean property and **ExecuteNextAsync** method of the **ResourceResponse** class to implement paging of your query results. Behind the scenes, these properties use a continuation token. A continuation token enables a client to retrieve the ‘next’ set of data in a follow-up query.*

### Use the **HasMoreResults** and **ExecuteNextAsync** Members to Implement Pagination

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    IQueryable<StudentProfile> query = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sql));  
    ```

    Replace that code with the following code:

    ```csharp
    IDocumentQuery<StudentProfile> query = client.CreateDocumentQuery<StudentProfile>(collectionLink, new SqlQuerySpec(sql), new FeedOptions { MaxItemCount = 100 }).AsDocumentQuery();
    ```

    > The DocumentQuery class will allow us to determine if there are more results available and page through results.

1. Locate the following lines of code:

    ```csharp
    foreach(StudentProfile profile in query)
    {
        await Console.Out.WriteLineAsync($"[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-50}\t{profile.Email.Home}");
    }  
    ```

    Replace that code with the following code:

    ```csharp
    int pageCount = 0;
    while(query.HasMoreResults)
    {
        await Console.Out.WriteLineAsync($"---Page #{++pageCount:0000}---");
        foreach(StudentProfile profile in await query.ExecuteNextAsync())
        {
            await Console.Out.WriteLineAsync($"\t[{profile.Id}]\t{profile.Name,-20}\t{profile.Email.School,-50}\t{profile.Email.Home}");
        }
    }
    ```

    > First we check if there are more results using the ``HasMoreResults`` property of the ``IDocumentQuery<>`` interface. If this value is set to true, we invoke the ``ExecuteNextAsync`` method to get the next batch of results and enumerate them using a ``foreach`` block.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You can view the current page count by looking at the headers in the console output.

1. Click the **🗙** symbol to close the terminal pane.


## Implement Cross-Partition Queries

*With an unlimited container, you may wish to perform queries that are filtered to a partition key or perform queries across multiple partition keys. You will now implement both types of queries using the various options available in the **FeedOptions** class.*

### Execute Single-Partition Query

1. In the Visual Studio Code window, double-click the **Student.cs** file to open an editor tab for the file.

1. Replace the existing **Student** class implementation with the following code:

    ```csharp
    public class Student
    {
        public string studentAlias { get; set; }
        public int age { get; set; }
        public int enrollmentYear { get; set; }
        public int projectedGraduationYear { get; set; }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

1. Within the **Program.cs** editor tab, locate the **Main** method and delete any existing code:

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

1. Within the new *using* block, add the following line of code to asynchronously open a connection:

    ```csharp
    await client.OpenAsync();
    ```
    
1. Add the following line of code to create a variable named ``collectionLink`` that is a reference (self-link) to an existing collection:

    ```csharp
    Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);
    ```

1. Add the following block of code to create a query that is filtered to a single partition key:

    ```csharp
    IEnumerable<Student> query = client
        .CreateDocumentQuery<Student>(collectionLink, new FeedOptions { PartitionKey = new PartitionKey(2016) })
        .Where(student => student.projectedGraduationYear == 2020);
    ```

    > First we will restrict our query to a single partition key using the ``PartitionKey`` property of the ``FeedOptions`` class. One of our partition key values for the ``/enrollmentYear`` path is ``2016``. We will filter our query to only return documents that uses this partition key. Remember, partition key paths are case sensitive. Since our property is named ``enrollmentYear``, it will match on the partition key path of ``/enrollmentYear``.

1. Add the following block of code to print out the results of your query:

    ```csharp
    foreach(Student student in query)
    {
        Console.Out.WriteLine($"Enrolled: {student.enrollmentYear}\tGraduation: {student.projectedGraduationYear}\t{student.studentAlias}");
    }      
    ```

    > We are using the C# string formatting features to print out two properties of our student instances.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You should only see records from a single partition.

1. Click the **🗙** symbol to close the terminal pane.

### Execute Cross-Partition Query

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    IEnumerable<Student> query = client
        .CreateDocumentQuery<Student>(collectionLink, new FeedOptions { PartitionKey = new PartitionKey(2016) })
        .Where(student => student.projectedGraduationYear == 2020);
    ```

    Replace that code with the following code:

    ```csharp
    IEnumerable<Student> query = client
        .CreateDocumentQuery<Student>(collectionLink, new FeedOptions { EnableCrossPartitionQuery = true })
        .Where(student => student.projectedGraduationYear == 2020);
    ```

    > We could ignore the partition keys and simply enable cross-partition queries using the ``EnableCrossPartitionQuery`` property of the ``FeedOptions`` class. You must explicitly opt-in using the SDK classes if you wish to perform a cross-partition query from the SDK.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You will notice that results are coming from more than one partition. You can observe this by looking at the values for ``type`` on the left-hand side of the output.

1. Click the **🗙** symbol to close the terminal pane.

### Implement Continuation Token

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
        using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
        {
            await client.OpenAsync();

            Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);

            string continuationToken = String.Empty;
            do
            {
                FeedOptions options = new FeedOptions 
                { 
                    EnableCrossPartitionQuery = true, 
                    RequestContinuation = continuationToken 
                };
                IDocumentQuery<Student> query = client
                    .CreateDocumentQuery<Student>(collectionLink, options)
                    .Where(student => student.age < 18)
                    .AsDocumentQuery();

                FeedResponse<Student> results = await query.ExecuteNextAsync<Student>();                
                continuationToken = results.ResponseContinuation;

                await Console.Out.WriteLineAsync($"ContinuationToken:\t{continuationToken}");
                foreach(Student result in results)
                {
                    await Console.Out.WriteLineAsync($"[Age: {result.age}]\t{result.studentAlias}@consoto.edu");
                }
                await Console.Out.WriteLineAsync(); 
            } 
            while (!String.IsNullOrEmpty(continuationToken));          
        }
    }
    ```

    > A continuation token allows us to resume a paginated query either immediately or later. When creating a query, the results are automatically paged. If there are more results, the returned page of results will also include a continuation token. This token should be passed back in    This implementation creates a **do-while** loop that will continue to get pages of results as long as the continuation token is not null.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of documents grouped by "pages" of results. You should also see a continuation token associated with each page of results. This token can be used if you are in a client-server scenario where you need to continue a query that was executed earlier.

1. Click the **🗙** symbol to close the terminal pane.

### Observe How Partitions Are Accessed in a Cross-Partition Query

1. In the Visual Studio Code window, double-click the **Student.cs** file to open an editor tab for the file.

1. Replace the existing **Student** class implementation with the following code:

    ```csharp
    public class Student
    {
        public string studentAlias { get; set; }
        public int age { get; set; }
        public int enrollmentYear { get; set; }
        public int projectedGraduationYear { get; set; }

        public FinancialInfo financialData { get; set; }

        public class FinancialInfo
        {
            public double tuitionBalance { get; set; }
        }
    }
    ```

1. In the Visual Studio Code window, double-click the **Program.cs** file to open an editor tab for the file.

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
            using (DocumentClient client = new DocumentClient(_endpointUri, _primaryKey))
            {
                await client.OpenAsync();

                Uri collectionLink = UriFactory.CreateDocumentCollectionUri(_databaseId, _collectionId);

                FeedOptions options = new FeedOptions 
                { 
                    EnableCrossPartitionQuery = true
                };

                string sql = "SELECT * FROM students s WHERE s.academicStatus.suspension = true";

                IDocumentQuery<Student> query = client
                    .CreateDocumentQuery<Student>(collectionLink, sql, options)
                    .AsDocumentQuery();

                int pageCount = 0;
                while(query.HasMoreResults)
                {
                    await Console.Out.WriteLineAsync($"---Page #{++pageCount:0000}---");
                    foreach(Student result in await query.ExecuteNextAsync())
                    {
                        await Console.Out.WriteLineAsync($"Enrollment: {result.enrollmentYear}\tBalance: {result.financialData.tuitionBalance}\t{result.studentAlias}@consoto.edu");
                    }
                }        
            }
        }
    ```

    > We are creating a cross-partition query here that may (or may not) have results for each partition key. Since this is a server-side fan-out and we are not filtering on a partition key, the search will be forced to check each partition. You can potentially have pages returned that have no results for partition keys that do not have any matching data.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the output of the console application.

    > You should see a list of documents grouped by "pages" of results. Scroll up and look at the results for **every page**. You should also notice that there is at least one page that does not have any results. This page occurs because the server-side fan-out is forced to check every partition since you are not filtering by partition keys. The next few examples will illustrate this even more.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.academicStatus.suspension = true";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14000";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > You will notice in the results that one page exists that does not have any relevant data. This occurs because there's at least one partition that does not have any data that matches the query specified above. Since we are not filtering on partition keys, all partitions much be checked as part of the server-side fan-out.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14000";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14950";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > Now only 54 records will match your query. They are pretty evenly distributed across the partition keys, so you will only see one page without results.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14950";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14996";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > Only 3 records match this query. You should see more empty pages.

1. Click the **🗙** symbol to close the terminal pane.

1. Within the **Main** method, locate the following line of code: 

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14996";
    ```

    Replace that code with the following code:

    ```csharp
    string sql = "SELECT * FROM students s WHERE s.financialData.tuitionBalance > 14998";
    ```

    > This new query should return results for most partition keys.

1. Save all of your open editor tabs.

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

    > This command will build and execute the console project.

1. Observe the results of the execution.

    > Only 1 record matches this query. You should see every multiple empty pages.

1. Click the **🗙** symbol to close the terminal pane.

1. Close all open editor tabs.

1. Close the Visual Studio Code application.

1. Close your browser application.
