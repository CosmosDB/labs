# Account Setup

## Lab Content Setup

> Before you start these labs, you will need to create an Azure Cosmos DB account that you will use throughout the labs.

1. To begin setup, Git clone or download the repo containing these instructions from [Github](https://github.com/CosmosDB/labs).

   > To automatically set up the resources needed for each lab, you will need to run Powershell scripts for Azure resource setup and to create local code files. Azure setup uses the Azure Powershell module. If you do not already have it installed, go to <https://docs.microsoft.com/en-us/powershell/azure/install-az-ps> for setup instructions before continuing.

1. Open a Powershell session and navigate to the folder containing your downloaded copy of the repo. Inside the repo, navigate to the **dotnet\setup** folder:

   ```powershell
   cd .\dotnet\setup\
   ```

1. To enable the setup scripts to run in your current Powershell session, enter the following:

   ```powershell
   Set-ExecutionPolicy Unrestricted -Scope Process
   ```

   > This setting will only apply within your current Powershell window.

1. Many of the labs refer to pre-built code to use as a starting point for the lab instructions. To automatically copy this starter code for the labs into a **CosmosLabs** folder in your **Documents** folder run the labCodeSetup.ps1 script:

   ```powershell
   .\labCodeSetup.ps1
   ```

   > The starter code for each lab is located inside the **templates** folder. To use a folder other that **Documents\CosmosLabs** for your lab code, the files can be copied manually instead.

1. To begin Azure resource setup, first connect to your Azure account:

   ```powershell
   Connect-AzAccount
   ```

   or

   ```powershell
   Connect-AzAccount -subscription <subscription id>
   ```

1. To create the Azure resources for the labs run the labSetup.ps1 script:

   ```powershell
   .\labSetup.ps1
   ```

   > This script creates resources in the _West US_ region by default. To use another region add **-location 'region name'** to the above command.

   > By default this script uses _cosmoslabs_ as the name of the resource group. You can use another name by adding **-resourceGroupName 'name'** to the above command

   > This script will fail if the specified resource group already exists. To bypass this failure and create the resources anyway, add **-overwriteGroup** to the above command.

1. Some Azure resources can take 10 minutes or more to complete setup so expect the script to run for a while before completing. After the script completes, your account should contain a **cosmoslabs** resource group with pre-configured resources.

## Log-in to the Azure Portal

1. In a new window, sign in to the **Azure Portal** (<http://portal.azure.com>).

1. Once you have logged in, you may be prompted to start a tour of the Azure portal. You can safely skip this step.

> The .NET SDK requires credentials to connect to your Azure Cosmos DB account. You will collect and store these credentials for use throughout the lab.

### Retrieve Account Credentials

1. On the left side of the portal, click the **Resource groups** link.

   ![Resource groups](../media/02-resource_groups.jpg)

1. In the **Resource groups** blade, locate and select the **cosmoslabs** _Resource Group_.

   ![Lab resource group](../media/02-lab_resource_group.jpg)

1. In the **cosmoslabs** blade, select the **Azure Cosmos DB** account you recently created.

   ![Cosmos resource](../media/02-cosmos_resource.jpg)

1. In the **Azure Cosmos DB** blade, locate the **Settings** section and click the **Keys** link.

   ![Keys pane](../media/02-keys_pane.jpg)

1. In the **Keys** pane, record the values in the **CONNECTION STRING**, **URI** and **PRIMARY KEY** fields. You will use these values later in this lab.

   ![Credentials](../media/02-keys.jpg)
