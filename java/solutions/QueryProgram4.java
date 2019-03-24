package testpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QueryProgram4 {
        private final ExecutorService executorService;
        private final Scheduler scheduler;
        private AsyncDocumentClient client;

        private final String databaseName = "UniversityDatabase";
        private final String collectionId = "StudentCollection";
        private static Gson gson = new Gson();

        private int numberOfDocuments;

        public QueryProgram4() {
                // public constructor
                executorService = Executors.newFixedThreadPool(100);
                scheduler = Schedulers.from(executorService);
                client = new AsyncDocumentClient.Builder().withServiceEndpoint("uri")
                .withMasterKeyOrResourceToken("key")
                                .withConnectionPolicy(ConnectionPolicy.GetDefault())
                                .withConsistencyLevel(ConsistencyLevel.Eventual).build();

        }

        public static void main(String[] args) throws InterruptedException, JSONException {
                FeedOptions options = new FeedOptions();
                // as this is a multi collection enable cross partition query
                options.setEnableCrossPartitionQuery(true);
                // note that setMaxItemCount sets the number of items to return in a single page result
                options.setMaxItemCount(5000);
                String sql = "SELECT activity FROM students s JOIN activity IN s.clubs WHERE s.enrollmentYear = 2018";
                QueryProgram4 p = new QueryProgram4();
                Observable<FeedResponse<Document>> documentQueryObservable = p.client
                                .queryDocuments("dbs/" + p.databaseName + "/colls/" + p.collectionId, sql, options);
                // observable to an iterator
                Iterator<FeedResponse<Document>> it = documentQueryObservable.toBlocking().getIterator();

                while (it.hasNext()) {
                        FeedResponse<Document> page = it.next();
                        List<Document> results = page.getResults();
                        for (Document doc : results) {
                                JSONObject obj = new JSONObject(doc.toJson());                               
                                String activity = obj.getString("activity");                              
                                System.out.println(activity);                               
                        }
                }
        }
}