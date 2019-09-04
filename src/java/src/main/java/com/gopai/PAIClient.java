package com.gopai;

import com.google.gson.Gson;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PAIClient {
    private static final String BASE_URL = "https://www.paireports.com/myreports/";

    BasicCookieStore cookieStore = new BasicCookieStore();
    CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore)
            .build();

    public boolean connect(String userName, String password) throws IOException {
        return httpclient.execute(builder("Login.event", "POST")
                        .setHeader("Accept", "application/json")
                        .addParameter("Username", userName)
                        .addParameter("Password", password)
                        .build(),

                response -> {
                    return StreamUtil.readIntoString(response.getEntity().getContent()).contains("Success");
                }
        );
    }

    public InputStream retrieveReportUsingBuilder(RequestBuilder requestBuilder) throws IOException {

        org.apache.http.client.methods.RequestBuilder r = builder(requestBuilder.getRequest().getReportName() + ".event", "POST")
                .addParameter("ReportCmd", "Filter")
                .addParameter("ReportCmd", "CustomCommand")
                .addParameter("CustomCmdList", "DownloadCSV");
        for (ColumnBuilder.Column column : requestBuilder.getRequest().getColumns()) {
            r.addParameter("F_" + column.getName(), column.getFilter());
            r.addParameter("ED_" + column.getName(), String.valueOf(column.isVisible()));
        }
        return httpclient.execute(
                r.build(),
                response -> {
                    return new ByteArrayInputStream(StreamUtil.read(response.getEntity().getContent()));
                }
        );
    }

    public InputStream retrieveReportUsingGUID(String guid) throws IOException {

        org.apache.http.client.methods.RequestBuilder r = builder("Report.event", "POST")
                .addParameter("ReportGUID", guid)
                .addParameter("ReportCmd", "CustomCommand")
                .addParameter("CustomCmdList", "DownloadCSV")
                .setHeader("Accept", "apple");

        return httpclient.execute(
                r.build(),
                response -> {
                    return new ByteArrayInputStream(StreamUtil.read(response.getEntity().getContent()));
                }
        );
    }

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

    private org.apache.http.client.methods.RequestBuilder builder(String endpoint, String method) {
        return org.apache.http.client.methods.RequestBuilder
                .create(method)
                .setHeader("User-Agent", "Mozilla/5.0")
                .setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .setUri(URI.create(BASE_URL + endpoint));
    }

    public boolean isConnected() {
        return !cookieStore.getCookies().isEmpty();
    }

    public void saveReportToFile(String name, String path, InputStream stream) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
            File file = new File( path + name + "_" + LocalDateTime.now().format(formatter) + ".csv");
            java.nio.file.Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            stream.close();
            System.out.println("Saved as " + file.getName() + " in " + file.getPath());
        } catch (IOException e) {
            System.out.println("Could not save file.");
        }
    }

    public void saveReportToFile(String name, InputStream stream) {
        String home = System.getProperty("user.home") + "/Downloads/";
        saveReportToFile(name, home, stream);
    }
}
