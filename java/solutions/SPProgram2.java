package testpackage;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.DocumentCollection;
import com.microsoft.azure.cosmosdb.PartitionKey;
import com.microsoft.azure.cosmosdb.RequestOptions;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import com.microsoft.azure.cosmosdb.ConnectionMode;
import com.microsoft.azure.cosmosdb.Document;
import java.util.UUID;

public class SPProgram2 {

    private final ExecutorService executorService;
    private final Scheduler scheduler;
    private AsyncDocumentClient client;

    private final String databaseName = "FinancialDatabase";
    private final String collectionId = "InvestorCollection";


    public SPProgram2() {
        executorService = Executors.newFixedThreadPool(100);
        scheduler = Schedulers.from(executorService);
        // Sets up the requirements for each test
        ConnectionPolicy connectionPolicy = new ConnectionPolicy();
        connectionPolicy.setConnectionMode(ConnectionMode.Direct);
        client = new AsyncDocumentClient.Builder()
                .withServiceEndpoint("https://cosmostvk.documents.azure.com:443/")
                .withMasterKeyOrResourceToken("Ii9PZCoHr0LM9Cy4vABwGVWmmymmp4KeNpZUgaZaG8jhvKUSHdPKDfNETPDczjUnwpYH5NoHArZ5zeJ4xFGtNg==")
                .withConnectionPolicy(connectionPolicy)
                .withConsistencyLevel(ConsistencyLevel.Session)
                .build();

        DocumentCollection collectionDefinition = new DocumentCollection();
        collectionDefinition.setId(UUID.randomUUID().toString());
    
    }

    /**
     * Create a document with a programmatically set definition, in an Async manner
     */


    public static void main(String[] args) {
        SPProgram2 p = new SPProgram2();

        try {
            p.executeStoredProc();
            System.out.println("finished");

        } catch (Exception e) {
            System.err.println(String.format("failed with %s", e));
        } 
        System.exit(0);

    }     
    public void executeStoredProc() throws Exception {

        System.out.println("deleting documents");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.setScriptLoggingEnabled(true);
        requestOptions.setPartitionKey(new PartitionKey("contosofinancial"));
        
        final CountDownLatch successfulCompletionLatch = new CountDownLatch(1);
        String sprocLink = "dbs/" + databaseName + "/colls/" + collectionId + "/sprocs/bulkDelete"; 

        String query = "SELECT * FROM investors i WHERE i.company = 'contosofinancial'";
        // Execute the stored procedure
 
        Object[] storedProcedureArgs = new Object[]{query};
        client.executeStoredProcedure(sprocLink, requestOptions, storedProcedureArgs)
                .subscribe(storedProcedureResponse -> {
                    String storedProcResultAsString = storedProcedureResponse.getResponseAsString();
                    successfulCompletionLatch.countDown();
                    System.out.println(storedProcedureResponse.getActivityId());
                }, error -> {
                    System.err.println("an error occurred while executing the stored procedure: actual cause: "
                                               + error.getMessage());
                });

        successfulCompletionLatch.await();        

    }
}