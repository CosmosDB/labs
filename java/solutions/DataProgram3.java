package testpackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

public class DataProgram3 {

    private final ExecutorService executorService;
    private final Scheduler scheduler;
    private AsyncDocumentClient client;

    private final String databaseName = "FinancialDatabase";
    private final String collectionId = "PeopleCollection";
    private AsyncDocumentClient asyncClient;
    private final String partitionKeyPath = "/type";
    private final int throughPut = 400;

    public DataProgram3() {
        executorService = Executors.newFixedThreadPool(100);
        scheduler = Schedulers.from(executorService);
        // Sets up the requirements for each test
        ConnectionPolicy connectionPolicy = new ConnectionPolicy();
        connectionPolicy.setConnectionMode(ConnectionMode.Direct);
        asyncClient = new AsyncDocumentClient.Builder()
                .withServiceEndpoint("https://cosmostvk.documents.azure.com:443/")
                .withMasterKeyOrResourceToken(
                        "Ii9PZCoHr0LM9Cy4vABwGVWmmymmp4KeNpZUgaZaG8jhvKUSHdPKDfNETPDczjUnwpYH5NoHArZ5zeJ4xFGtNg==")
                .withConnectionPolicy(connectionPolicy).withConsistencyLevel(ConsistencyLevel.Session).build();

        DocumentCollection collectionDefinition = new DocumentCollection();
        collectionDefinition.setId(UUID.randomUUID().toString());

    }

    /**
     * Create a document with a programmatically set definition, in an Async manner
     */

    public static void main(String[] args) {
        DataProgram3 p = new DataProgram3();

        try {
            p.documentUpsert_Async();
            System.out.println("finished");

        } catch (Exception e) {
            System.err.println(String.format("failed with %s", e));
        }
        System.exit(0);

    }

    public void createDocument() throws Exception {
        ArrayList<Document> documents = new PersonDetail(1, 100).documentDefinitions;
        for (Document document : documents) {
            // Create a document
            Observable<ResourceResponse<Document>> createDocumentObservable = asyncClient
                    .createDocument("dbs/" + databaseName + "/colls/" + collectionId, document, null, false);
            Observable<Double> totalChargeObservable = createDocumentObservable.map(ResourceResponse::getRequestCharge)
                    // Map to request charge
                    .reduce((totalCharge, charge) -> totalCharge + charge);
            // Sum up all the charges
            final CountDownLatch completionLatch = new CountDownLatch(1);
            // Subscribe to the total request charge observable
            totalChargeObservable.subscribe(totalCharge -> {
                // Print the total charge
                System.out.println("RU charge: "+totalCharge);
                completionLatch.countDown();
            }, e -> completionLatch.countDown());
            completionLatch.await();
        }
    }

    /**
     * Upsert a document
     */

    public void documentUpsert_Async() throws Exception {
        // Create a document
        Document doc = new Document(String.format("{ 'id': 'example.document', 'type': 'upsertsample'}", UUID.randomUUID().toString(), 1));
        asyncClient.createDocument("dbs/" + databaseName + "/colls/" + collectionId, doc, null, false).toBlocking().single();

        // Upsert the existing document
        Document upsertingDocument = new Document(
                String.format("{ 'id': 'example.document', 'type': 'upsertsample', 'new-prop' : '2'}", doc.getId(), 1));
        Observable<ResourceResponse<Document>> upsertDocumentObservable = asyncClient
                .upsertDocument("dbs/" + databaseName + "/colls/" + collectionId, upsertingDocument, null, false);

        List<ResourceResponse<Document>> capturedResponse = Collections
                .synchronizedList(new ArrayList<>());

        upsertDocumentObservable.subscribe(resourceResponse -> {
            capturedResponse.add(resourceResponse);
        });

        Thread.sleep(4000);
    }
}