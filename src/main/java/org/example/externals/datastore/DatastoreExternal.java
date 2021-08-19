package org.example.externals.datastore;

import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.QueryConverter;
import com.newrelic.api.agent.Trace;

public class DatastoreExternal {
    public static final String PRODUCT = "sqlite";
    public static final String COLLECTION = "test.db";
    public static final String OPERATION = "select";
    public static final String HOST = "localhost";
    public static final Integer PORT = 8080;
    public static final String DB_NAME = "test.db";
    public static final String RAW_QUERY = "SELECT * FROM test.db WHERE license = 123xyz;";
    private static final QueryConverter<String> QUERY_CONVERTER = new QueryConverter<String>() {
        @Override
        public String toRawQueryString(String statement) {
            // Do work to transform raw query object to string
            return statement;
        }

        @Override
        public String toObfuscatedQueryString(String statement) {
            // Do work to remove any sensitive information here
            return obfuscateQuery(statement);
        }
    };

    // start a transaction when this method is called
    @Trace(dispatcher = true)
    public static void reportDatastoreQueryTraced() {
        NewRelic.setTransactionName("Custom", "DatastoreExternal/reportDatastoreQueryTraced");
        reportDatastoreQuery();
    }

    // Do not start a transaction when this method is called
    public static void reportDatastoreQueryUntraced() {
        reportDatastoreQuery();
    }

    // start a transaction when this method is called
    @Trace(dispatcher = true)
    public static void reportDatastoreSlowQueryTraced() {
        NewRelic.setTransactionName("Custom", "DatastoreExternal/reportDatastoreSlowQueryTraced");
        reportDatastoreSlowQuery();
    }

    // Do not start a transaction when this method is called
    public static void reportDatastoreSlowQueryUntraced() {
        reportDatastoreSlowQuery();
    }

    @Trace
    private static void reportDatastoreQuery() {
        DatastoreParameters datastoreParameters = DatastoreParameters
                .product(PRODUCT) // the datastore vendor
                .collection(COLLECTION) // the name of the collection (or table for SQL databases)
                .operation(OPERATION) // the operation being performed, e.g. "SELECT" or "UPDATE" for SQL databases
                .instance(HOST, PORT) // the datastore instance information - generally can be found as part of the connection
                .databaseName(DB_NAME) // may be null, indicating no keyspace for the command
                .build();

        NewRelic.getAgent().getTracedMethod().reportAsExternal(datastoreParameters);
    }

    @Trace
    private static void reportDatastoreSlowQuery() {
        ExternalParameters datastoreParameters = DatastoreParameters
                .product(PRODUCT) // the datastore vendor
                .collection(COLLECTION) // the name of the collection (or table for SQL databases)
                .operation(OPERATION) // the operation being performed, e.g. "SELECT" or "UPDATE" for SQL databases
                .instance(HOST, PORT) // the datastore instance information - generally can be found as part of the connection
                .databaseName(DB_NAME) // may be null, indicating no keyspace for the command
                .slowQuery(RAW_QUERY, QUERY_CONVERTER) // report slow raw query, obfuscating if transaction_tracer.record_sql=obfuscated
                .build();

        NewRelic.getAgent().getTracedMethod().reportAsExternal(datastoreParameters);
    }

    private static String obfuscateQuery(String rawQuery) {
        String sanitizedLicense = "license = ?";
        String regexTarget = "license = [1-9a-zA-Z]*";
        return rawQuery.replaceAll(regexTarget, sanitizedLicense);
    }

}
