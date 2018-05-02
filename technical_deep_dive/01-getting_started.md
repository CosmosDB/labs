# Getting Started

In this lab, you will create an Azure Cosmos DB account, database, and multiple collections. You will then populate the collections with sample data that will be used in various labs.

### Create Azure Cosmos DB Assets

*You will now create an Azure Cosmos DB account to use in the remaining labs.*

1. In a new window, sign in to the **Azure Portal** (<http://portal.azure.com>).

1. On the left side of the portal, click the **Create a resource** link.

    ![Create a resource](../media/02-create_a_resource.png)

1. At the top of the **New** blade, locate the **Search the Marketplace** field.

    ![Search the Marketplace](../media/02-search_the_marketplace.png)

1. Enter the text **Cosmos** into the search field and press **Enter**.

1. In the **Everything** search results blade, select the **Azure Cosmos DB** result.

    ![Cosmos search results](../media/02-cosmos_search_result.png)

1. In the **Azure Cosmos DB** blade, click the **Create** button.

    ![Create Cosmos instance](../media/02-create_cosmos.png)

1. In the new **Azure Cosmos DB** blade, perform the following actions:

    1. In the **ID** field, enter a globally unique value.

    1. In the **API** list, select the **SQL** option.

    1. Leave the **Subscription** field set to its default value.

    1. In the **Resource group** section, select the **Create new** option.

    1. In the **Resource group** section, enter the value **COSMOSLABS**  into the empty field.

    1. In the **Location** field, select the **West US** location.

    1. Click the **Create** button.

    ![Create Cosmos instance](../media/02-create_cosmos_settings.png)

1. Wait for the creation task to complete before completing this lab.