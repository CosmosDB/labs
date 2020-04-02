# Load Data Into Cosmos DB with ADF

In this lab, you will populate an Azure Cosmos DB container from an existing set of data using tools built in to Azure. After importing, you will use the Azure portal to view your imported data.

> If you have not already completed setup for the lab content see the instructions for [Account Setup](00-account_setup.md) before starting this lab.  This will create an Azure Cosmos DB database and container that you will use throughout the lab. You will also use an **Azure Data Factory (ADF)** resource to import existing data into your container.

## Create Azure Cosmos DB Database and Container

You will now create a database and container within your Azure Cosmos DB account.

1. Navigate to the [Azure Portal](https://portal.azure.com)

2. On the left side of the portal, select the **Resource groups** link.

    ![Resource groups is highlighted](../media/03-resource_groups.jpg "Select the Resource Groups")

3. In the **Resource groups** blade, locate and select the **cosmoslabs** resource group.

    ![The cosmoslabs resource group is highlighted](../media/03-lab_resource_group.jpg "Select the cosmoslabs resource group")

4. In the **cosmoslabs** blade, select the **Azure Cosmos DB** account you recently created.

    ![The Cosmos DB resource is highlighted](../media/03-cosmos_resource.jpg "Select the cosmoslabs resource")

5. In the **Azure Cosmos DB** blade, locate and select the **Overview** link on the left side of the blade. At the top select the **Add Container** button.

    ![Add container link is highlighted](../media/03-add_collection.jpg "Add a new container")

6. In the **Add Container** popup, perform the following actions:

    1. In the **Database id** field, select the **Create new** option and enter the value **ImportDatabase**.

    2. Do not check the **Provision database throughput** option.

        > Provisioning throughput for a database allows you to share the throughput among all the containers that belong to that database. Within an Azure Cosmos DB database, you can have a set of containers which shares the throughput as well as containers, which have dedicated throughput.

    3. In the **Container Id** field, enter the value **FoodCollection**.

    4. In the **Partition key** field, enter the value ``/foodGroup``.

    5. In the **Throughput** field, enter the value ``11000``.

    6. Select the **OK** button.

7. Wait for the creation of the new **database** and **container** to finish before moving on with this lab.

## Import Lab Data Into Container

You will use **Azure Data Factory (ADF)** to import the JSON array stored in the **nutrition.json** file from Azure Blob Storage.

You do not need to do Steps 1-4 in this section and can proceed to Step 5 by opening your Data Factory (named importNutritionData with a random number suffix)if you are completing the lab through Microsoft Hands-on Labs or ran the setup script, you can use the pre-created Data Factory within your resource group.

1. On the left side of the portal, select the **Resource groups** link.

    > To learn more about copying data to Cosmos DB with ADF, please read [ADF's documentation](https://docs.microsoft.com/en-us/azure/data-factory/connector-azure-cosmos-db).

    ![Resource groups link is highlighted](../media/03-resource_groups.jpg "Select Resource Groups")

2. In the **Resource groups** blade, locate and select the **cosmoslabs** resource group.

3. If you see a Data Factory resource, you can skip to step 5, otherwise select **Add** to add a new resource

    ![A data factory resource is highlighted](../media/03-adf-isntance.png "Review if you have data factory already")

    ![Select Add in the nav bar](../media/03-add_adf.jpg "Add a new resource")

   - Search for **Data Factory** and select it. 
   - Create a new **Data Factory**. You should name this data factory **importnutritiondata** with a unique number appended and select the relevant Azure subscription. You should ensure your existing **cosmoslabs** resource group is selected as well as a Version **V2**. 
   - Select **East US** as the region. Do not select **Enable GIT** (this may be checked by default). 
   - Select **Create**.

        ![The new data factory dialog is displayed](../media/03-adf_selections.jpg "Add a new Data Factory resource")

4. After creation, open your newly created Data Factory. Select **Author & Monitor** and you will launch ADF.

    ![The overview blade is displayed for ADF](../media/03-adf_author&monitor.jpg "Select Author and Monitor link")

5. Select **Copy Data**.

   - We will be using ADF for a one-time copy of data from a source JSON file on Azure Blob Storage to a database in Cosmos DB’s SQL API. ADF can also be used for more frequent data transfers from Cosmos DB to other data stores.

    ![The main workspace page is displayed for ADF](../media/03-adf_copydata.jpg "Select the Copy Data activity")

6. Edit basic properties for this data copy. You should name the task **ImportNutrition** and select to **Run once now**, then select **Next**

   ![The copy data activity properties dialog is displayed](../media/03-adf_properties.jpg "Enter a task name and the schedule")

7. **Create a new connection** and select **Azure Blob Storage**. We will import data from a json file on Azure Blob Storage. In addition to Blob Storage, you can use ADF to migrate from a wide variety of sources. We will not cover migration from these sources in this tutorial.

    ![Create new connection link is highlighted](../media/03-adf_blob.jpg "Create a new connection")

    ![Azure Blog Storage is highlighted](../media/03-adf_blob2.jpg "Select the Azure Blob Storage connection type")

8. Name the source **NutritionJson** and select **SAS URI** as the Authentication method. Please use the following SAS URI for read-only access to this Blob Storage container:

    `https://cosmosdblabsv3.blob.core.windows.net/?sv=2018-03-28&ss=bfqt&srt=sco&sp=rlp&se=2022-01-01T04:55:28Z&st=2019-08-05T20:02:28Z&spr=https&sig=%2FVbismlTQ7INplqo6WfU8o266le72o2bFdZt1Y51PZo%3D`

    ![The New linked service dialog is displayed](../media/03-adf_connecttoblob.jpg "Enter the SAS url in the dialog")

9. Select **Create**
10. Select **Next**
11. Select **Browse**, then double-click to open the **nutritiondata** folder
12. Select the **NutritionData.json** file, then select **Choose**

    ![The nutritiiondata folder is displayed](../media/03-adf_choosestudents.jpg "Select the NutritionData.json file")

13. Un-check **Copy file recursively** or **Binary Copy** if they are checked. Also ensure that other fields are empty.

    ![The input file or folder dialog is displayed](../media/03-adf_source_next.jpg "Ensure all other fields are empty, select next")

14. Select the file format as **JSON format**. Then select **Next**.

    !["The file format settings dialog is displayed"](../media/03-adf_source_dataset_format.jpg "Ensure JSON format is selected, then select Next")

15. You have now successfully connected the Blob Storage container with the nutrition.json file as the source.

16. For the **Destination data store** add the Cosmos DB target data store by selecting **Create new connection** and selecting **Azure Cosmos DB (SQL API)**.

    !["The New Linked Service dialog is displayed"](../media/03-adf_selecttarget.jpg "Select the Azure Cosmos DB service type")

17. Name the linked service **targetcosmosdb** and select your Azure subscription and Cosmos DB account. You should also select the Cosmos DB **ImportDatabase** that you created earlier.

    !["The linked service configuration dialog is displayed"](../media/03-adf_selecttargetdb.jpg "Select the ImportDatabase database")

18. Select your newly created **targetcosmosdb** connection as the Destination date store.

    !["The destination data source dialog is displayed"](../media/03-adf_destconnectionnext.jpg "Select your recently created data source")

19. Select your **FoodCollection** container from the drop-down menu. You will map your Blob storage file to the correct Cosmos DB container. Select **Next** to continue.

    !["The table mapping dialog is displayed"](../media/03-adf_correcttable.jpg "Select the FoodCollection container")

20. Select through the `Table mapping` screen.

    !["The column mapping dialog is displayed"](../media/03-adf_destinationconnectionfinal.jpg "Review the dialog, select next")

21. There is no need to change any `Settings`. Select **next**.

    !["The settings dialog is displayed"](../media/03-adf_settings.jpg "Review the dialog, select next")

22. Select **Next** to begin deployment After deployment is complete, select **Monitor**.

    !["The pipeline runs are displayed"](../media/03-adf_progress.jpg "Notice the pipeline is In progress")

23. After a few minutes, refresh the page and the status for the ImportNutrition pipeline should be listed as **Succeeded**.

    !["The pipeline runs are displayed"](../media/03-adf_progress_complete.jpg "The pipeline has succeeded")

24. Once the import process has completed, close the ADF. You will now proceed to validate your imported data.

## Validate Imported Data

The Azure Cosmos DB Data Explorer allows you to view documents and run queries directly within the Azure Portal. In this exercise, you will use the Data Explorer to view the data stored in our container.

You will validate that the data was successfully imported into your container using the **Items** view in the **Data Explorer**.

1. Return to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, select the **Resource groups** link.

    ![Resource groups link is highlighted](../media/03-resource_groups.jpg "Select your resource group")

1. In the **Resource groups** blade, locate and select the **cosmoslabs** resource group.

    ![The Lab resource group is highlighted](../media/03-lab_resource_group.jpg "Select the resource group")

1. In the **cosmoslabs** blade, select the **Azure Cosmos DB** account you recently created.

    ![The Cosmos DB resource is highlighted](../media/03-cosmos_resource.jpg "Select the Cosmos DB resource")

1. In the **Azure Cosmos DB** blade, locate and select the **Data Explorer** link on the left side of the blade.

    ![The Data Explorer link was selected and is blade is displayed](../media/03-data_explorer_pane.jpg "Select Data Explorer")

1. In the **Data Explorer** section, expand the **ImportDatabase** database node and then expand the **FoodCollection** container node.

    ![The Container node is displayed](../media/03-collection_node.jpg "Expand the ImportDatabase node")

1. Within the **FoodCollection** node, select the **Items** link to view a subset of the various documents in the container. Select a few of the documents and observe the properties and structure of the documents.

    ![Items is highlighted](../media/03-documents.jpg "Select Items")

    ![An Example document is displayed](../media/03-example_document.jpg "Select a document")

> If this is your final lab, follow the steps in [Removing Lab Assets](11-cleaning_up.md) to remove all lab resources.
