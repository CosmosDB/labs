## Creating a Multi-Partition Solution using Azure Cosmos DB



### Setup

> Prior to starting this lab, we will create an Azure Cosmos DB account.

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

    1. In the **Resource group** section, enter the value **LABMPAR**  into the empty field.

    1. In the **Location** field, select the **East US** location.

    1. Click the **Create** button.

1. Wait for the creation task to complete before moving on with this lab.  

1. On the left side of the portal, click the **Resource groups** link.

1. In the **Resource groups** blade, locate and select the **LABMPAR** *Resource Group* link.

1. In the **LABMPAR** blade, select the **Azure Cosmos DB** account you recently created.

1. In the **Azure Cosmos DB** blade, locate and click the **Overview** link on the left side of the blade.

1. At the top of the **Azure Cosmos DB** blade, click the **Add Collection** button.

1. In the **Add Collection** popup, perform the following actions:

    1. In the **Database id** field, enter the value **FinancialClubDatabase**.

    1. In the **Collection id** field, enter the value **MemberCollection**.

    1. In the **Storage capacity** section, select the **Fixed (10 GB)** option.

    1. In the **Throughput** field, enter thev alue **400**.

    1. Click the **OK** button.

1. Wait for the creation of the new **database** and **collection** to finish before moving on with this lab.

1. On the left side of the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

1. In the **Keys** pane, record the values in the **URI** and **Primary Key** fields. You will use these values later in this lab.

### Task: Create Unlimited Container

1.

### Task: Execute Cross-Partition Queries

1.

### Task: Deploy Multi-Partition Web Application

1.

### Cleanup

1. At the top of the portal, click the **Cloud Shell** icon to open a new shell instance.

1. In the **Cloud Shell** command prompt at the bottom of the portal, type in the following command and press **Enter** to list all resource groups in the subscription:

    ```
    az group list
    ```

1. Type in the following command and press **Enter** to delete the **LABMPAR** *Resource Group*:

    ```
    az group delete --name LABMPAR --no-wait --yes
    ```

1. Close the **Cloud Shell** prompt at the bottom of the portal.

### Review

In this lab, you