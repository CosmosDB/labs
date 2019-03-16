# Prerequisites

Prior to starting these labs, you must have the following operating system and software configured on your local machine:

**Operating System**

- 64-bit Windows 10 Operating System
    - [download](https://www.microsoft.com/windows/get-windows-10)

**Software**

| Software | Download Link |
| --- | --- |
| Git | [/git-scm.com/downloads](https://git-scm.com/downloads) 
Java 11 SDK (or greater) | [https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) |
| Visual Studio Code | [/code.visualstudio.com/download](https://go.microsoft.com/fwlink/?Linkid=852157) |
| Maven | [https://maven.apache.org/](https://maven.apache.org/) |


---

# Labs

*It is recommended to complete the labs in the order specified below:*

- [Pre-lab: Creating an Azure Cosmos DB account](technical_deep_dive/01-getting_started.md)
- [Lab 1: Creating a partitioned solution using Azure Cosmos DB](technical_deep_dive/02-creating_multi_partition_solution.md)
- [Lab 2: Querying an Azure Cosmos DB Database using the SQL API](technical_deep_dive/03-querying_the_database_using_sql.md)
- [Lab 3: Authoring Azure Cosmos DB Stored Procedures using JavaScript ](technical_deep_dive/04-authoring_stored_procedures.md)
- [Lab 4: Troubleshooting and Tuning Azure Cosmos DB Requests](technical_deep_dive/05-troubleshooting_failed_requests.md)
- [Post-lab: Cleaning Up](technical_deep_dive/06-cleaning_up.md)

---

# Accompanying Powerpoint Decks

- [Workshop Deck](./decks/cosmos-db-l400.pptx)
- [Workshop Deck (alternative theme)](./decks/cosmos-db-workshop.pptx)

---

# Notes

1. When installing the Java 11 SDK or higher, this is bundled with a Java Runtime Environment (JRE). Make sure the JRE path (e.g: C:\Program Files\Java\jdk-11.0.2\bin\) is present at the top of your Path variable in System variables. 
2. If you already have Java installed on your local machine, you should check the version of your Java Runtime Environment (JRE) installation using the ``java -version`` command.

