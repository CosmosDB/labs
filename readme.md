# Workshop Powerpoint Deck

- [Workshop Deck- Last Updated April 2019](./decks/L400-WorkshopApril2019.pptx)
- [Sample Schedule](./decks/CosmosDBWorkshopSchedule2019.docx)

In addition to the above workshop decks, we have four hands-on labs. Each lab is designed to take 60-90 minutes and can be either self-paced or instructor-led labs. We have labs available for our .NET sdk and Java sdk below:


# .NET Labs

**.NET Lab Prerequisites**

Prior to starting these labs, you must have the following operating system and software configured on your local machine:

**Operating System**

- 64-bit Windows 10 Operating System
    - [download](https://www.microsoft.com/windows/get-windows-10)
- Microsoft .NET Framework 4.5.1 or higher <sup>1</sup>
    - [download](http://go.microsoft.com/fwlink/?LinkId=863262)

**Software**

| Software | Download Link |
| --- | --- |
| Git | [/git-scm.com/downloads](https://git-scm.com/downloads) |
| .NET Core 2.1 (or greater) SDK <sup>2</sup> | [/download.microsoft.com/dotnet-sdk-2.1](https://download.microsoft.com/download/E/2/6/E266C257-F7AF-4E79-8EA2-DF26031C84E2/dotnet-sdk-2.1.103-win-gs-x64.exe) |
| Visual Studio Code | [/code.visualstudio.com/download](https://go.microsoft.com/fwlink/?Linkid=852157) |


---

**.NET Lab Guides**

*It is recommended to complete the labs in the order specified below:*

- [Pre-lab: Creating an Azure Cosmos DB account](dotnet/technical_deep_dive/01-getting_started.md)
- [Lab 1: Creating a partitioned solution using Azure Cosmos DB](dotnet/technical_deep_dive/02-creating_multi_partition_solution.md)
- [Lab 2: Querying an Azure Cosmos DB Database using the SQL API](dotnet/technical_deep_dive/03-querying_the_database_using_sql.md)
- [Lab 3: Authoring Azure Cosmos DB Stored Procedures using JavaScript ](dotnet/technical_deep_dive/04-authoring_stored_procedures.md)
- [Lab 4: Troubleshooting and Tuning Azure Cosmos DB Requests](dotnet/technical_deep_dive/05-troubleshooting_failed_requests.md)
- [Post-lab: Cleaning Up](dotnet/technical_deep_dive/06-cleaning_up.md)

---

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

- [Pre-lab: Creating an Azure Cosmos DB account](dotnet/technical_deep_dive/01-getting_started.md)
- [Lab 1: Creating a partitioned solution using Azure Cosmos DB](java/technical_deep_dive/02-creating_multi_partition_solution.md)
- [Lab 2: Querying an Azure Cosmos DB Database using the SQL API](java/technical_deep_dive/03-querying_the_database_using_sql.md)
- [Lab 3: Authoring Azure Cosmos DB Stored Procedures using JavaScript ](java/technical_deep_dive/04-authoring_stored_procedures.md)
- [Lab 4: Troubleshooting and Tuning Azure Cosmos DB Requests](java/technical_deep_dive/05-troubleshooting_failed_requests.md)
- [Post-lab: Cleaning Up](java/technical_deep_dive/06-cleaning_up.md)

---


**Notes**

1. When installing the Java 11 SDK or higher, this is bundled with a Java Runtime Environment (JRE). Make sure the JRE path (e.g: C:\Program Files\Java\jdk-11.0.2\bin\) is present at the top of your Path variable in System variables. 
2. If you already have Java installed on your local machine, you should check the version of your Java Runtime Environment (JRE) installation using the ``java -version`` command.
2. If using a version of Java greater than version 8, some projects may not compile (for example the benchmarking application).

