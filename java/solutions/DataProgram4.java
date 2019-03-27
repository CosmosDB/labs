package testpackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.DocumentCollection;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.ResourceResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import com.microsoft.azure.cosmosdb.ConnectionMode;
import com.microsoft.azure.cosmosdb.Document;
import java.util.UUID;

public class DataProgram4 {

    private final ExecutorService executorService;
    private final Scheduler scheduler;
    private AsyncDocumentClient client;

    private final String databaseName = "FinancialDatabase";
    private final String collectionId = "PeopleCollection";
    private AsyncDocumentClient asyncClient;

    public DataProgram4() {
        executorService = Executors.newFixedThreadPool(100);
        scheduler = Schedulers.from(executorService);
        // Sets up the requirements for each test
        ConnectionPolicy connectionPolicy = new ConnectionPolicy();
        connectionPolicy.setConnectionMode(ConnectionMode.Direct);
        asyncClient = new AsyncDocumentClient.Builder()
                .withServiceEndpoint("uri")
                .withMasterKeyOrResourceToken("key")
                .withConnectionPolicy(connectionPolicy).withConsistencyLevel(ConsistencyLevel.Session).build();

        DocumentCollection collectionDefinition = new DocumentCollection();
        collectionDefinition.setId(UUID.randomUUID().toString());

    }

    /**
     * Create a document with a programmatically set definition, in an Async manner
     */

    public static void main(String[] args) {
        DataProgram4 p = new DataProgram4();

        try {
            p.createDocument();
            System.out.println("finished");

        } catch (Exception e) {
            System.err.println(String.format("failed with %s", e));
        }
        System.exit(0);

    }

    public void createDocument() throws Exception {
        ArrayList<Document> documents = new Transaction(5000).documentDefinitions;
        final ExecutorService executor = Executors.newFixedThreadPool(500);
        final List<Future<?>> futures = new ArrayList<>();
        for (Document document: documents){
            // Create a document
            Future<?> future = executor.submit(() -> {
            Observable<ResourceResponse<Document>> createDocumentObservable = asyncClient
            .createDocument("dbs/" + databaseName + "/colls/" + collectionId, document, null, false);
            System.out.println(createDocumentObservable.toBlocking().single().getResource().getId());  
            });   
            futures.add(future);             
        }
        try {
            for (Future<?> future : futures) {
                future.get(); // do anything you need, e.g. isDone(), ...
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}