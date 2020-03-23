# Workshop PowerPoint Deck





**Two Day Suggested Schedule**

- [Sample Schedule](./decks/CosmosDBWorkshopSchedule2019.docx)

**Deep-Dive Powerpoint Decks**
- [Overview, Value Proposition & Use Cases](./decks/Overview-Value-Proposition-Use-Cases.pptx)
- [Resource Model](./decks/Resource-Model.pptx)
- [Request Units & Billing](./decks/Request-Units-Billing.pptx)
- [Data Modeling](./decks/Data-Modeling.pptx)
- [Partitioning](./decks/Partitioning.pptx)
- [SQL API Query](./decks/SQL-API-Query.pptx)
- [Server Side Programming](./decks/Server-Side-Programming.pptx)
- [Troubleshooting](./decks/Troubleshooting.pptx)
- [Concurrency](./decks/Concurrency.pptx)
- [Change Feed](./decks/Change-Feed.pptx)
- [Global Distribution](./decks/Global-Distribution.pptx)

**References**
- [Use-Case cheat sheet (1-pager)](./decks/1Pager-Use-Cases.pptx)

In addition to the above workshop decks, we have hands-on labs. We have labs available for our .NET sdk and Java sdk below:
# .NET (V3) Labs

**.NET Lab Prerequisites**

Prior to starting these labs, you must have the following operating system and software configured on your local machine:

**Operating System**

- 64-bit Windows 10 Operating System
  - [download](https://www.microsoft.com/windows/get-windows-10)
- Microsoft .NET Framework 4.5.1 or higher <sup>1</sup>
  - [download](http://go.microsoft.com/fwlink/?LinkId=863262)

**Software**

| Software                                    | Download Link                                                |
| ------------------------------------------- | ------------------------------------------------------------ |
| Git                                         | [/git-scm.com/downloads](https://git-scm.com/downloads)      |
| .NET Core 2.1 (or greater) SDK <sup>2</sup> | [/download.microsoft.com/dotnet-sdk-2.1](https://download.microsoft.com/download/E/2/6/E266C257-F7AF-4E79-8EA2-DF26031C84E2/dotnet-sdk-2.1.103-win-gs-x64.exe) |
| Visual Studio Code                          | [/code.visualstudio.com/download](https://go.microsoft.com/fwlink/?Linkid=852157) |

------

**.NET Lab Guides**

*It is recommended to complete the labs in the order specified below:*

- [Pre-lab: Creating an Azure Cosmos DB account](dotnet/labs/00-account_setup.md)
- [Lab 1: Creating a container in Azure Cosmos DB](dotnet/labs/01-creating_partitioned_collection.md)
- [Lab 2: Importing Data into Azure Cosmos DB with Azure Data Factory](dotnet/labs/02-load_data_with_adf.md)
- [Lab 3: Querying in Azure Cosmos DB](dotnet/labs/03-querying_in_azure_cosmosdb.md)
- [Lab 4: Indexing in Azure Cosmos DB](dotnet/labs/04-indexing_in_cosmosdb.md)
- [Lab 5: Building a .NET Console App on Azure Cosmos DB](dotnet/labs/05-build_net_app.md)
- [Lab 6: Multi-Document Transactions in Azure Cosmos DB](dotnet/labs/06-multi-document-transactions.md)
- [Lab 7: Transactional Continuation in Azure Cosmos DB](dotnet/labs/07-transactions-with-continuation.md)
- [Lab 8: Intro to Azure Cosmos DB Change Feed](dotnet/labs/08-change_feed_with_azure_functions.md)
- [Lab 9: Troubleshooting Performance in Azure Cosmos DB](dotnet/labs/09-troubleshooting-performance.md)
- [Lab 10: Optimistic Concurrency Control in Azure Cosmos DB](dotnet/labs/10-concurrency-control.md)
- [Post-lab: Cleaning Up](dotnet/labs/11-cleaning_up.md)

------

**Notes**

1. If you are unsure of what version of the .NET Framework you have installed on your local machine, you can visit the following link to view instructions on determining your installed version: <https://docs.microsoft.com/dotnet/framework/migration-guide/how-to-determine-which-versions-are-installed>.
2. If you already have .NET Core installed on your local machine, you should check the version of your .NET Core installation using the ``dotnet --version`` command.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Java Labs

**Java Lab Prerequisites**

Prior to starting these labs, you must have the following operating system and software configured on your local machine:

**Operating System**

- 64-bit Windows 10 Operating System
    - [download](https://www.microsoft.com/windows/get-windows-10)

**Software**

| Software | Download Link |
| --- | --- |
| Git | [/git-scm.com/downloads](https://git-scm.com/downloads) 
Java 8 JDK (or greater) | [/jdk8-downloads](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) |
Java 8 JRE (or greater) | [/jre8-downloads](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) |
| Visual Studio Code | [/code.visualstudio.com/download](https://go.microsoft.com/fwlink/?Linkid=852157) |
| Java Extension Pack (if using VS Code) | [/vscode-java-pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) |
| Maven | [/maven.apache.org/](https://maven.apache.org/) |

---

**Java Lab Guides**

*It is recommended to complete the labs in the order specified below:*

- [Pre-lab: Creating an Azure Cosmos DB account](java/labs/00-account_setup.md)
- [Lab 1: Creating a container in Azure Cosmos DB](java/labs/01-creating_partitioned_collection.md)

---


**Notes**

1. When installing the Java 11 SDK or higher, this is bundled with a Java Runtime Environment (JRE). Make sure the JRE path (e.g: C:\Program Files\Java\jdk-11.0.2\bin\) is present at the top of your Path variable in System variables. 
2. If you already have Java installed on your local machine, you should check the version of your Java Runtime Environment (JRE) installation using the ``java -version`` command.
2. If using a version of Java greater than version 8, some projects may not compile (for example the benchmarking application).

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Appendix: Stickers

Adobe Illustrator files for printing cosmic stickers (e.g. stickermule)
- [2x2 inch black circle](./stickers/2x2-circle-template-CosmosBlack.ai)
- [2x2 inch clear circle](./stickers/2x2-clear-sticker-template-CosmosClear.ai)
- [Die-cut color logo](./stickers/cosmos-die-cut-sticker-template-v2.ai)
