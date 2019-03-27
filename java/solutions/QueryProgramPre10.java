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
import com.microsoft.azure.cosmosdb.AccessCondition;
import com.microsoft.azure.cosmosdb.AccessConditionType;
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

public class QueryProgramPre10 {
        private final ExecutorService executorService;
        private final Scheduler scheduler;
        private AsyncDocumentClient client;

        private final String databaseName = "FinancialDatabase";
        private final String collectionId = "TransactionCollection";

        private int numberOfDocuments;

        public QueryProgramPre10() {
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

                String sql = "SELECT TOP 1 * FROM c";

                FeedOptions options = new FeedOptions();
                options.setEnableCrossPartitionQuery(true);
                QueryProgramPre10 p = new QueryProgramPre10();
                Observable<FeedResponse<Document>> documentQueryObservable = p.client
                .queryDocuments("dbs/" + p.databaseName + "/colls/" + p.collectionId, sql, options);

                Iterator<FeedResponse<Document>> it = documentQueryObservable.toBlocking().getIterator();
                while (it.hasNext()) {
                        FeedResponse<Document> page = it.next();
                        List<Document> results = page.getResults();
                        for (Document doc : results) { 
                                System.out.println(doc.getETag());
                        }
                }                
        }
}