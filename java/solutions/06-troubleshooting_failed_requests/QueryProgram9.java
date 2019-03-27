package testpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.base.Stopwatch;
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
import com.microsoft.azure.cosmosdb.QueryMetrics;
import com.microsoft.azure.cosmosdb.RequestOptions;
import com.microsoft.azure.cosmosdb.ResourceResponse;
import com.microsoft.azure.cosmosdb.internal.HttpConstants;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class QueryProgram9 {
        private final ExecutorService executorService;
        private final Scheduler scheduler;
        private AsyncDocumentClient client;

        private final String databaseName = "FinancialDatabase";
        private final String collectionId = "TransactionCollection";

        private int numberOfDocuments;

        public QueryProgram9() {
                // public constructor
                executorService = Executors.newFixedThreadPool(100);
                scheduler = Schedulers.from(executorService);
                client = new AsyncDocumentClient.Builder()
                .withServiceEndpoint("uri")
                .withMasterKeyOrResourceToken("key")
                .withConnectionPolicy(ConnectionPolicy.GetDefault())
                .withConsistencyLevel(ConsistencyLevel.Eventual).build();

        }

        public static void main(String[] args) throws InterruptedException {
                // as this is a multi collection enable cross partition query

                long startTime = System.nanoTime(); 
                FeedOptions options = new FeedOptions();
                options.setEnableCrossPartitionQuery(true);
                options.setMaxItemCount(1000);
                options.setMaxDegreeOfParallelism(-1);
                options.setMaxBufferedItemCount(50000); 

                System.out.println(options.getEnableCrossPartitionQuery());
                System.out.println(options.getMaxItemCount());
                System.out.println(options.getMaxDegreeOfParallelism());
                System.out.println(options.getMaxBufferedItemCount());
                
                String sql = "SELECT * FROM c WHERE c.processed = true ORDER BY c.amount DESC";
                QueryProgram9 p = new QueryProgram9();
                Observable<FeedResponse<Document>> documentQueryObservable = p.client
                .queryDocuments("dbs/" + p.databaseName + "/colls/" + p.collectionId, sql, options);
                // observable to an iterator
                Iterator<FeedResponse<Document>> it = documentQueryObservable.toBlocking().getIterator();

                while (it.hasNext()) {
                        FeedResponse<Document> page = it.next();
                        List<Document> results = page.getResults();
                }
                long estimatedTime = System.nanoTime() - startTime;
                System.out.println(estimatedTime);
        }
}