# Validating Partition Key Choice

In this lab, you will compare various partition key choices for a large dataset using a special benchmarking tool available on GitHub.com.

## Setup

*The .NET SDK requires credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.*

### Retrieve Account Credentials

1. On the left side of the portal, click the **Resource groups** link.

    ![Resource groups](../media/02-resource_groups.png)

1. In the **Resource groups** blade, locate and select the **COSMOSLABS** *Resource Group*.

    ![Lab resource group](../media/02-lab_resource_group.png)

1. In the **COSMOSLABS** blade, select the **Azure Cosmos DB** account you recently created.

    ![Cosmos resource](../media/02-cosmos_resource.png)

1. In the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

    ![Keys pane](../media/02-keys_pane.png)

1. In the **Keys** pane, record the values in the **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

    ![Credentials](../media/02-credentials.png)

## Benchmark A Simple Collection using a .NET Core Application

*First, you will learn how to use the benchmarking tool using a simple collection and partition key.*

### Clone Existing .NET Core Project

1. On your local machine, create a new folder that will be used to contain the content of your .NET Core project.

1. In the new folder, right-click the folder and select the **Open with Code** menu option.

    > Alternatively, you can run a command prompt in your current directory and execute the ``code .`` command.

1. In the Visual Studio Code window that appears, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    git clone https://github.com/seesharprun/cosmos-benchmark.git .
    ```

    > This command will create a copy of a .NET Core project located on GitHub (<https://github.com/seesharprun/cosmos-benchmark>) in your local folder.

1. Visual Studio Code will most likely prompt you to install various extensions related to **.NET Core** or **Azure Cosmos DB** development. None of these extensions are required to complete the labs.

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

1. Observe the **Program.cs** and **benchmark.csproj** files created by the .NET Core CLI.

1. Double-click the **sample.json** link in the **Explorer** pane to open the file in the editor.

1. Observe the sample JSON file

    > This file will show you a sample of the types of JSON documents that will be uploaded to your collection. Pay close attention to the **Submit\*** fields, the **DeviceId** field and the **LocationId** field.

### Update the Application's Settings

1. Double-click the **appsettings.json** link in the **Explorer** pane to open the file in the editor.

1. Locate the **/cosmosSettings.endpointUri** JSON path:

    ```js
    "endpointUri": ""
    ```

    Update the **endPointUri** property by setting it's value to the **URI** value from your Azure Cosmos DB account that you recorded earlier in this lab: 

    > For example, if your **uri** is ``https://cosmosacct.documents.azure.com:443/``, your new property will look like this: ``"endpointUri": "https://cosmosacct.documents.azure.com:443/"``.

1. Locate the **/cosmosSettings.primaryKey** JSON path:

    ```js
    "primaryKey": ""
    ```

    Update the **primaryKey** property by setting it's value to the **PRIMARY KEY** value from your Azure Cosmos DB account that you recorded earlier in this lab: 

    > For example, if your **primary key** is ``elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ==``, your new property will look like this: ``"primaryKey": "elzirrKCnXlacvh1CRAnQdYVbVLspmYHQyYrhx0PltHi8wn5lHVHFnd1Xm3ad5cn4TUcH4U0MSeHsVykkFPHpQ=="``.

### Configure a Simple Collection for Benchmarking

1. Double-click the **appsettings.json** link in the **Explorer** pane to open the file in the editor.

1. Locate the **/collectionSettings** JSON path:

    ```js
    "collectionSettings": [],
    ```

    Update the **collectionSettings** property by setting it's value to the following array of JSON objects:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithHourKey",
            "throughput": 1000,
            "partitionKeys": [ "/SubmitHour" ]
        }
    ],
    ```

    > The object above will instruct the benchmark tool to create a single collection and set it's throughput and partition key to the specified values. For this simple demo, we will use the **hour** when an IoT device recording was submitted as our partition key.

    | Collection Name | Throughput | Partition Key |
    | --- | --- | --- |
    | CollectionWithHourKey | 1000 | /SubmitHour |

1. Save all of your open editor tabs.

### Run the Benchmark Application

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

1. Observe the results of the application's execution. Your results should look very similar to the code sample below:

    ```sh
    DocumentDBBenchmark starting...
    Database Validated:     dbs/MOEFAA==/
    Collection Validated:   dbs/MOEFAA==/colls/MOEFAN6FoQU=/
    Summary:
    ---------------------------------------------------------------------
    Endpoint:               https://cosmosacct.documents.azure.com/
    Database                IoTDeviceData
    Collection              CollectionWithHourKey
    Partition Key:          /SubmitHour
    Throughput:             1000 Request Units per Second (RU/s)
    Insert Operation:       10 Tasks Inserting 1000 Documents Total
    ---------------------------------------------------------------------

    Starting Inserts with 10 tasks
    Inserted 272 docs @ 272 writes/s, 1968 RU/s (5B max monthly 1KB reads)
    Inserted 616 docs @ 307 writes/s, 2220 RU/s (6B max monthly 1KB reads)
    Inserted 962 docs @ 319 writes/s, 2309 RU/s (6B max monthly 1KB reads)
    Inserted 1000 docs @ 249 writes/s, 1802 RU/s (5B max monthly 1KB reads)

    Summary:
    ---------------------------------------------------------------------
    Total Time Elapsed:     00:00:04.0209549
    Inserted 1000 docs @ 249 writes/s, 1801 RU/s (5B max monthly 1KB reads)
    ---------------------------------------------------------------------
    ```

    > The benchmark tool tells you how long it takes to write a specific number of documents to your collection. YOu also get useful metadata such as the amount of **RU/s** being used and the total execution time. We are not tuning our partition key choice quite yet, we are simply learning to use the tool.

1. Press the **ENTER** key to complete the execution of the console application.

### Update the Application's Settings

1. Double-click the **appsettings.json** link in the **Explorer** pane to open the file in the editor.

1. Locate the **/cosmosSettings.numberOfDocumentsToInsert** JSON path:

    ```js
    "numberOfDocumentsToInsert": 1000
    ```

    Update the **numberOfDocumentsToInsert** property by setting it's value to **10,000**:

    ```js
    "numberOfDocumentsToInsert": 10000
    ```

1. Save all of your open editor tabs.

### Run the Benchmark Application

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

1. Observe the results of the application's execution. 

    > You may notice at this point that the results don't scale linearly. In our testing, we observed that **1,000** records needed **4-7** seconds to import while **10,000** records needed **20-30** seconds to import.

1. Press the **ENTER** key to complete the execution of the console application.

## Benchmark Various Partition Key Choices using a .NET Core Application

*Now you will use multiple collections and partition key options to compare various strategies for partitioning a large dataset.*

### Configure Multiple Collections for Benchmarking

1. Double-click the **appsettings.json** link in the **Explorer** pane to open the file in the editor.

1. Locate the **/collectionSettings** JSON path:

    ```js
    "collectionSettings": [],
    ```

    Update the **collectionSettings** property by setting it's value to the following array of JSON objects:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithMinuteKey",
            "throughput": 5000,
            "partitionKeys": [ "/SubmitMinute" ]
        },
        {
            "id": "CollectionWithSecondKey",
            "throughput": 5000,
            "partitionKeys": [ "/SubmitSecond" ]
        }
    ],
    ```

    > The object above will instruct the benchmark tool to create multiple collections and set their throughput and partition key to the specified values. For this demo, we will compare the results using each partition key.

    | Collection Name | Throughput | Partition Key |
    | --- | --- | --- |
    | CollectionWithMinuteKey | 5000 | /SubmitMinute |
    | CollectionWithSecondKey | 5000 | /SubmitSecond |

1. Save all of your open editor tabs.

### Run the Benchmark Application

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

1. Observe the results of the application's execution.

    > The timestamp on these IoT records is based on the time when the record was created. We submit the records as soon as they are created so there's very little latency between the client and server timestamp. Most of the records being submitted will be within the same minute so they share the same **SubmitMinute** partition key. This will cause a hot partition key and can constraint throughput. In this demo, you should expect a total time of >20 seconds.

    ```sh
    ---------------------------------------------------------------------
    Collection              CollectionWithMinuteKey
    Partition Key:          /SubmitMinute
    Total Time Elapsed:     00:00:22.1209042
    Inserted 10000 docs @ 452 writes/s, 3273 RU/s (8B max monthly 1KB reads)
    ---------------------------------------------------------------------
    ```

    > The **SubmitMinute** partition key will most likely take longer to execute than the **SubmitSecond** partition key. The **SubmitSecond** partition key "spreads" the records across more partition keys because the records are being submitted at different seconds within the minute. If you are ingesting documents at enough volume, you could potentially see a bottleneck with the second-based partition key too. In this demo, you should expect a total time of >10 seconds.

    ```sh
    ---------------------------------------------------------------------
    Collection              CollectionWithSecondKey
    Partition Key:          /SubmitSecond
    Total Time Elapsed:     00:00:13.1317735
    Inserted 10000 docs @ 761 writes/s, 5515 RU/s (14B max monthly 1KB reads)
    ---------------------------------------------------------------------
    ```

1. Compare the RU/s and total time for both collections.

1. Press the **ENTER** key to complete the execution of the console application.

### Configure A New Collection for Benchmarking

1. Double-click the **appsettings.json** link in the **Explorer** pane to open the file in the editor.

1. Locate the **/collectionSettings** JSON path:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithMinuteKey",
            "throughput": 5000,
            "partitionKeys": [ "/SubmitMinute" ]
        },
        {
            "id": "CollectionWithSecondKey",
            "throughput": 5000,
            "partitionKeys": [ "/SubmitSecond" ]
        }
    ],
    ```

    Update the **collectionSettings** property by setting it's value to the following array of JSON objects:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithDeviceKey",
            "throughput": 5000,
            "partitionKeys": [ "/DeviceId" ]
        }
    ],
    ```

    > The object above will instruct the benchmark tool to create a new collection and set it's throughput and partition key to the specified values. For this demo, we are adding a container using a new partition key that partitions based on the device sending the request.

    | Collection Name | Throughput | Partition Key |
    | --- | --- | --- |
    | CollectionWithDeviceKey | 5000 | /DeviceId |

1. Save all of your open editor tabs.

### Run the Benchmark Application

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

1. Observe the results of the application's execution.

    > Using the **DeviceId** partition key creates a "more even" distribution of requests across your various partition keys. Because of this behavior, you should notice drastically improved performance. In this demo, you should expect a total time of <10 seconds.

    ```sh
    ---------------------------------------------------------------------
    Collection              CollectionWithDeviceKey
    Partition Key:          /DeviceId
    Total Time Elapsed:     00:00:07.0502822
    Inserted 10000 docs @ 1418 writes/s, 10270 RU/s (27B max monthly 1KB reads)
    ---------------------------------------------------------------------
    ```

1. Press the **ENTER** key to complete the execution of the console application.

### Configure A Collection With a Composite Key for Benchmarking

1. Double-click the **appsettings.json** link in the **Explorer** pane to open the file in the editor.

1. Locate the **/collectionSettings** JSON path:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithDeviceKey",
            "throughput": 5000,
            "partitionKeys": [ "/DeviceId" ]
        }
    ],
    ```

    Update the **collectionSettings** property by setting it's value to the following array of JSON objects:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithDeviceKey",
            "throughput": 5000,
            "partitionKeys": [ "/DeviceId" ]
        },    
        {
            "id": "CollectionWithCompositeDeviceAndLocationKey",
            "throughput": 5000,
            "partitionKeys": [ "/DeviceLocationComposite" ]
        }
    ],
    ```

    > The object above will instruct the benchmark tool to create multiple collections and set their throughput and partition key to the specified values. For this demo, we are adding another container using a new partition key that is based on a composite of two existing values.

    | Collection Name | Throughput | Partition Key |
    | --- | --- | --- |
    | CollectionWithDeviceKey | 5000 | /DeviceId |
    | CollectionWithCompositeDeviceAndLocationKey | 5000 | /DeviceLocationComposite |

1. Save all of your open editor tabs.

### Observe How the Composite Key is Created

1. Double-click the **Benchmark.cs** link in the **Explorer** pane to open the file in the editor.

1. Locate **Line 29** of the C# code in the **Benchmark.cs** file:

    ```csharp
    recording.DeviceLocationComposite = $"{recording.DeviceId}_{recording.LocationId}";
    ```

    > This line of code concatenates the **DeviceId** and **LocationId** fields before setting the result as the value of the **DeviceLocationComposite** property.

### Run the Benchmark Application

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

1. Observe the results of the application's execution.

    > You will quickly notice that there's little difference between the two partition key choices in this example. This is most likely because the sample data set is too small.

1. Press the **ENTER** key to complete the execution of the console application.

### Increase the Amount of Documents Used in the Test

1. Double-click the **appsettings.json** link in the **Explorer** pane to open the file in the editor.

1. Locate the **/cosmosSettings.degreeOfParallelism** JSON path:

    ```js
    "degreeOfParallelism": -1
    ```

    Update the **degreeOfParallelism** property by setting it's value to **200**:

    ```js
    "degreeOfParallelism": 200
    ```

1. Locate the **/cosmosSettings.numberOfDocumentsToInsert** JSON path:

    ```js
    "numberOfDocumentsToInsert": 10000
    ```

    Update the **numberOfDocumentsToInsert** property by setting it's value to **200,000**:

    ```js
    "numberOfDocumentsToInsert": 200000
    ```

1. Locate the **/collectionSettings** JSON path:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithDeviceKey",
            "throughput": 5000,
            "partitionKeys": [ "/DeviceId" ]
        },    
        {
            "id": "CollectionWithCompositeDeviceAndLocationKey",
            "throughput": 5000,
            "partitionKeys": [ "/DeviceLocationComposite" ]
        }
    ],
    ```

    Update the **collectionSettings** property by setting it's value to the following array of JSON objects:

    ```js
    "collectionSettings": [
        {
            "id": "CollectionWithDeviceKey",
            "throughput": 10000,
            "partitionKeys": [ "/DeviceId" ]
        },    
        {
            "id": "CollectionWithCompositeDeviceAndLocationKey",
            "throughput": 10000,
            "partitionKeys": [ "/DeviceLocationComposite" ]
        }
    ],
    ```

    > For this demo, we are increasing the number of documents being created and increasing the assigned RU/s throughput.

    | Collection Name | Throughput | Partition Key |
    | --- | --- | --- |
    | CollectionWithDeviceKey | 10000 | /DeviceId |
    | CollectionWithCompositeDeviceAndLocationKey | 10000 | /DeviceLocationComposite |

1. Save all of your open editor tabs.

### Run the Benchmark Application

1. In the Visual Studio Code window, right-click the **Explorer** pane and select the **Open in Command Prompt** menu option.

1. In the open terminal pane, enter and execute the following command:

    ```sh
    dotnet run
    ```

1. Observe the results of the application's execution. This execution may take a couple of minutes to complete.

    > Even though we are at greater scale, you will still see that there are few differences between the key choices here. This is most likely due to physical constraints of the local machine you are using to do the tests. In testing, our four-core processor (Intel i7 6700k) didn't get too many benefits from increasing the **Maximum Degree of Parallelism** and **RU/s** beyond these values.

    ```sh
    ---------------------------------------------------------------------
    Collection              CollectionWithDeviceKey
    Partition Key:          /DeviceId
    Total Time Elapsed:     00:00:52.5756688
    Inserted 200000 docs @ 3804 writes/s, 27542 RU/s (71B max monthly 1KB reads)
    ---------------------------------------------------------------------

    ---------------------------------------------------------------------
    Collection              CollectionWithCompositeDeviceAndLocationKey
    Partition Key:          /DeviceLocationComposite
    Total Time Elapsed:     00:01:00.7347998
    Inserted 200000 docs @ 3293 writes/s, 23842 RU/s (62B max monthly 1KB reads)
    ---------------------------------------------------------------------
    ```

1. Press the **ENTER** key to complete the execution of the console application.

1. Close all open editor tabs.

1. Close the Visual Studio Code application.

### Observe the New Collections and Database in the Azure Portal

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **LABQURY** *Resource Group*.

1. In the **LABQURY** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Data Explorer** link on the left side of the blade.

1. In the **Data Explorer** section, expand the **IoTDeviceData** database node and then observe the various collection nodes.

1. Expand the **CollectionWithDeviceKey** node. Within the node, click the **Scale & Settings** link.

1. Observe the following properties of the collection:

    - Storage Capacity

    - Assigned Throughput

    - Indexing Policy

1. Back in the **Data Explorer** section, expand and select the **CollectionWithCompositeKey** node. 

1. Click the **New SQL Query** button at the top of the **Data Explorer** section.

1. In the query tab, replace the contents of the *query editor* with the following SQL query:

    ```sql
    SELECT VALUE COUNT(1) FROM recordings
    ```

1. Click the **Execute Query** button in the query tab to run the query. 

1. In the **Results** pane, observe the results of your query.

1. Back in the **Data Explorer** section, right-click the **IoTDeviceData** database node and select the **Delete Database** option.

    > Since you created multiple collections in this database with high throughput, it makes sense to dispose of the database immediately to minimize your Azure subscription consumption.

1. In the **Delete Database** popup enter the name of the database (**IoTDeviceData**) in the field and then press the **OK** button.

1. Close your browser application.