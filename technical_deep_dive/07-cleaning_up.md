## Cleaning Up

## Removing Lab Assets



### Open Cloud Shell

**

1. At the top of the portal, click the **Cloud Shell** icon to open a new shell instance.

    > If this is your first time using the cloud shell, you may need to configure the default Storage account and SMB file share.

### Use Azure CLI to Delete Resource Group

**

1. In the **Cloud Shell** command prompt at the bottom of the portal, type in the following command and press **Enter** to list all resource groups in the subscription:

    ```
    az group list
    ```

1. Type in the following command and press **Enter** to delete the **LABMPAR** *Resource Group*:

    ```
    az group delete --name COSMOSLABS --no-wait --yes
    ```

1. Close the **Cloud Shell** prompt at the bottom of the portal.