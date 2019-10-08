package com.gopai.pair.sdk.v1;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * The PAIClient allows access to Reports and database queries in a simple way for automation purposes.
 * PAIClient contains the session information such as user information
 * and contains methods for calling reports and queries in various ways.
 * This client is not thread safe.
 */
public class PAIClient implements AutoCloseable {
    private static final String BASE_URL = "https://www.paireports.com/myreports/";

    private String username;
    private String password;
    private CloseableHttpClient httpclient = HttpClients.custom().build();

    /**
     * The start point for the server session.
     * Both username and password must be for a valid and active User.
     * @param username
     * @param password
     * @return boolean value indicating whether the connection was successful or not.
     * @throws IOException
     */
    public boolean connect(String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        return httpclient.execute(builder("Login.event", "POST")
                        .setHeader("Accept", "application/json")
                        .addParameter("Username", username)
                        .addParameter("Password", password)
                        .build(),

                response -> {
                    return StreamUtil.readIntoString(response.getEntity().getContent()).contains("Success");
                }
        );
    }

    /**
     * Retrieves a report as a CSV file using the {@link ReportRequest} to apply filters to report.
     * @param reportRequest {@link ReportRequest}
     * @return InputStream of the retrieved CSV file.
     * @throws IOException
     */
    public InputStream retrieveReportUsingBuilder(ReportRequest reportRequest) throws IOException {
        RequestBuilder r = builder("Report.event", "POST")
                .addParameter("ReportGUID", reportRequest.getReportGUID())
                .addParameter("ReportCmd", "Filter")
                .addParameter("ReportCmd", "CustomCommand")
                .addParameter("CustomCmdList", "DownloadCSV");

        for (ReportRequest.Column column : reportRequest.getColumns()) {
            r.addParameter("F_" + column.getName(), column.getFilter());
            r.addParameter("ED_" + column.getName(), String.valueOf(column.getVisible()));
        }
        return httpclient.execute(
                r.build(),
                response -> {
                    return new ByteArrayInputStream(StreamUtil.read(response.getEntity().getContent()));
                }
        );
    }

    /**
     * Retrieves a report as a CSV file using the report GUID.
     * @param guid
     * @return InputStream of the retrieved CSV file.
     * @throws IOException
     */
    public InputStream retrieveReportUsingGUID(String guid) throws IOException {
        RequestBuilder r = builder("Report.event", "POST")
                .addParameter("ReportGUID", guid)
                .addParameter("ReportCmd", "CustomCommand")
                .setHeader("Accept", "application/text");

        return httpclient.execute(
                r.build(),
                response -> {
                    return new ByteArrayInputStream(StreamUtil.read(response.getEntity().getContent()));
                }
        );
    }

    /**
     * Retrieves the {@link ReportConfig} for the given report GUID.
     * @param GUID
     * @return {@link ReportConfig}
     * @throws IOException
     */
    public ReportConfig retrieveReportConfig(String GUID) throws IOException {
        String response = httpclient.execute(builder("ReportConfigManagement.event", "FIND_CONFIG")
                        .setHeader("Accept", "application/json")
                        .addParameter("GUID", GUID)
                        .build(),

                r -> {
                    return StreamUtil.readIntoString(r.getEntity().getContent());
                }
        );
        if (response.contains("SuccessResponse"))
            return new Gson().fromJson(response, ReportConfig.class);
        return null;
    }

    /**
     * Runs a query against the database and returns the results as an InputStream.
     * @param query
     * @return InputStream of the retrieved query results.
     * @throws IOException
     */
    public InputStream runQuery(String query) throws IOException {
        return httpclient.execute(builder("Query.event", "POST")
                        .setHeader("Accept", "*/*")
                        .addParameter("query", query)
                        .build(),

                response -> {
                    return new ByteArrayInputStream(StreamUtil.read(response.getEntity().getContent()));
                }
        );
    }

    /**
     * Runs a query against the database and returns results in the specified form.
     * @param query
     * @param expectedReturnClass
     * @param <DesiredType>
     * @return
     * @throws IOException
     */
    public <DesiredType> DesiredType runQuery(String query, Class<DesiredType> expectedReturnClass) throws IOException {
        return httpclient.execute(builder("Query.event", "POST")
                        .setHeader("Accept", "*/*")
                        .addParameter("query", query)
                        .build(),

                response -> {
                    String json = new String(StreamUtil.read(response.getEntity().getContent()));
                    return new Gson().fromJson(json, expectedReturnClass);
                }
        );
    }


    /**
     * This method sends a heartbeat.event request to the server to identify if the session is still live.
     * @return A boolean based on the results of the heartbeat.
     */
    public boolean isConnected(){
        try {
            return httpclient.execute(builder("heartbeat.event", "POST").build()).getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Sends the DoLogout.event to the server to end your session.
     * @throws IOException
     */
    public void logOut() throws IOException {
        CloseableHttpResponse execute = httpclient.execute(builder("DoLogout.event", "GET")
                .setHeader("Accept", "*/*")
                .build()
        );
        execute.close();
    }

    /**
     * On close performs a logOut for your session.
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if (isConnected())
            logOut();
    }

    private RequestBuilder builder(String endpoint, String method) {
        return RequestBuilder
                .create(method)
                .setHeader("User-Agent", "Mozilla/5.0")
                .setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .setHeader("PAIClient-SDK", "1.0.0")
                .setUri(URI.create(BASE_URL + endpoint));
    }
}
