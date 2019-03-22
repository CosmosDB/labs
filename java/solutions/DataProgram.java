package testpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.DataType;
import com.microsoft.azure.cosmosdb.Database;
import com.microsoft.azure.cosmosdb.DocumentClientException;
import com.microsoft.azure.cosmosdb.DocumentCollection;
import com.microsoft.azure.cosmosdb.IncludedPath;
import com.microsoft.azure.cosmosdb.Index;
import com.microsoft.azure.cosmosdb.IndexingPolicy;
import com.microsoft.azure.cosmosdb.PartitionKeyDefinition;
import com.microsoft.azure.cosmosdb.RequestOptions;
import com.microsoft.azure.cosmosdb.ResourceResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import com.github.javafaker.Faker;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.azure.cosmosdb.ConnectionMode;
import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.Database;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.DocumentClientException;
import com.microsoft.azure.cosmosdb.DocumentCollection;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.ResourceResponse;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import org.apache.commons.lang3.RandomUtils;
import rx.Observable;
import rx.functions.Action1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class DataProgram {

    private final ExecutorService executorService;
    private final Scheduler scheduler;
    private AsyncDocumentClient client;

    private final String databaseName = "EntertainmentDatabase";
    private final String collectionId = "CustomCollection";
    private final String partitionKeyPath = "/type";
    private final int throughPut = 400;

    private final static int TIMEOUT = 60000;
    private AsyncDocumentClient asyncClient;
    private Database createdDatabase;
    private DocumentCollection createdCollection;

    public DataProgram() {
        executorService = Executors.newFixedThreadPool(100);
        scheduler = Schedulers.from(executorService);
        // Sets up the requirements for each test
        ConnectionPolicy connectionPolicy = new ConnectionPolicy();
        connectionPolicy.setConnectionMode(ConnectionMode.Direct);
        asyncClient = new AsyncDocumentClient.Builder()
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
        DataProgram p = new DataProgram();

        try {
            p.createDocument();
            //System.out.println(String.format("Database created, please hold while resources are released"));

        } catch (Exception e) {
            System.err.println(String.format("DocumentDB GetStarted failed with %s", e));
        } finally {
            System.out.println("close the client");
            //p.close();
        }
        System.exit(0);

    }     
    public void createDocument() throws Exception {
        ArrayList<Document> documents = new WatchLiveTelevisionChannel(3).documentDefinitions;
        for (Document document: documents){
            // Create a document
            asyncClient.createDocument(getCollectionLink(), document, null, false)
            .toBlocking().single().getResource();
            System.out.println("inserting: "+document);
        }
    }

    private String getCollectionLink() {
        return "dbs/" + databaseName + "/colls/" + collectionId;
    }

    private String getDocumentLink(Document createdDocument) {
        return "dbs/" + databaseName + "/colls/" + collectionId + "/docs/"
                + createdDocument.getId();
    }

    public void close() {
        executorService.shutdown();
        client.close();
    }    
}