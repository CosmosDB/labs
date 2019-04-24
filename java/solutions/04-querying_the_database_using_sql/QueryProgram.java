package testpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.DataType;
import com.microsoft.azure.cosmosdb.Database;
import com.microsoft.azure.cosmosdb.Document;
import com.microsoft.azure.cosmosdb.DocumentClientException;
import com.microsoft.azure.cosmosdb.DocumentCollection;
import com.microsoft.azure.cosmosdb.FeedOptions;
import com.microsoft.azure.cosmosdb.FeedResponse;
import com.microsoft.azure.cosmosdb.IncludedPath;
import com.microsoft.azure.cosmosdb.Index;
import com.microsoft.azure.cosmosdb.IndexingPolicy;
import com.microsoft.azure.cosmosdb.PartitionKeyDefinition;
import com.microsoft.azure.cosmosdb.RequestOptions;
import com.microsoft.azure.cosmosdb.ResourceResponse;
import com.microsoft.azure.cosmosdb.internal.HttpConstants;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class QueryProgram {
    private final ExecutorService executorService;
    private final Scheduler scheduler;
    private AsyncDocumentClient client;

    private final String databaseName = "UniversityDatabase";
    private final String collectionId = "StudentCollection";

    private int numberOfDocuments;

    public QueryProgram() {
        // public constructor
        executorService = Executors.newFixedThreadPool(100);
        scheduler = Schedulers.from(executorService);
        client = new AsyncDocumentClient.Builder().withServiceEndpoint("uri")
                .withMasterKeyOrResourceToken("key")
                .withConnectionPolicy(ConnectionPolicy.GetDefault()).withConsistencyLevel(ConsistencyLevel.Eventual)
                .build();

    }

    public static void main(String[] args) throws InterruptedException {
        // as this is a multi collection enable cross partition query
        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);
        options.setMaxItemCount(5);
        options.setMaxDegreeOfParallelism(2);
        String sql = "SELECT TOP 5 s.studentAlias FROM coll s WHERE s.enrollmentYear = 2018 ORDER BY s.studentAlias";
        QueryProgram p = new QueryProgram();
        Observable<FeedResponse<Document>> documentQueryObservable = p.client
                .queryDocuments("dbs/" + p.databaseName + "/colls/" + p.collectionId, sql, options);
        // observable to an iterator

        Iterator<FeedResponse<Document>> it = documentQueryObservable.toBlocking().getIterator();

        while (it.hasNext()) {
                FeedResponse<Document> page = it.next();
                List<Document> results = page.getResults();
                // here we iterate over all the items in the page result
                for (Object doc : results) {
                        System.out.println(doc);
                }
        }
        // List<String> resultList = Collections.synchronizedList(new ArrayList<>());
        // documentQueryObservable.map(FeedResponse::getResults)
        //         // Map the logical page to the list of documents in the page
        //         .concatMap(Observable::from) // Flatten the list of documents
        //         .map(doc -> doc.toString()) // Map to the document Id
        //         //.forEach(doc -> resultList.add(doc)) // Add each document Id to the
        //         .forEach(System.out::println);
                    
    }
}