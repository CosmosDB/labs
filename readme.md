# Prerequisites

Prior to starting these labs, you must have the following operating system and software configured on your local machine:

**Operating System**

- 64-bit Windows 10 Operating System | [download](https://www.microsoft.com/windows/get-windows-10)
- Microsoft .NET Framework 4.5.1 or higher [1] |[download](http://go.microsoft.com/fwlink/?LinkId=863262)

**Software to Install**

| Software | Download Link |
| --- | --- |
| .NET Core 2.1 (or greater) SDK [2] | [/download.microsoft.com/dotnet-sdk-2.1](https://download.microsoft.com/download/E/2/6/E266C257-F7AF-4E79-8EA2-DF26031C84E2/dotnet-sdk-2.1.103-win-gs-x64.exe)
| Visual Studio Code | [/code.visualstudio.com/download](https://go.microsoft.com/fwlink/?Linkid=852157) |
| Azure Cosmos DB Data Migration Tool [3] | [/cosmosdb-data-migration-tool](https://cosmosdbportalstorage.blob.core.windows.net/datamigrationtool/2018.02.28-1.8.1/dt-1.8.1.zip) |

# Labs

- [Getting Started](technical_deep_dive/01-getting_started.md)
- [Creating a Multi-Partition Solution using Azure Cosmos DB](technical_deep_dive/02-creating_multi_partition_solution.md)
- [Querying An Azure Cosmos DB Database using the SQL API](technical_deep_dive/04-querying_the_database_using_sql.md)
- [Authoring Azure Cosmos DB Stored Procedures using JavaScript ](technical_deep_dive/05-authoring_stored_procedures.md)
- [Troubleshooting and Tuning Azure Cosmos DB Requests](technical_deep_dive/06-troubleshooting_failed_requests.md)
- [Cleaning Up](technical_deep_dive/07-cleaning_up.md)

---

[1]: If you are unsure of what version of the .NET Framework you have installed on your local machine, you can visit the following link to view instructions on determining your installed version: <https://docs.microsoft.com/dotnet/framework/migration-guide/how-to-determine-which-versions-are-installed>.
[2]: If you already have .NET Core installed on your local machine, you should check the version of your .NET Core installation using the ``dotnet --version`` command.
[3]: Once you have downloaded the compressed (zip) folder for the Azure Cosmos DB Data Migration Tool, you should extract the contents of the folder to your local machine and run the ``dtui.exe`` executable to use the tool.