package com.gopai;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jemima.nyamogo on 5/22/2017.
 */
public class PAIReportsClient {
    private static final String BASE_URL = "http://persephone.atmreports.com:8080/TestApp/";
    private RequestBuilder requestBuilder;
    private String sessionKey;
    private String userAgent = "Mozilla/5.0";

    public boolean connect(String userName, String password) throws IOException {
        String loginTokens = String.format("Username=%s&Password=%s", userName, password);
        URL url = new URL(BASE_URL + "Login.event");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setRequestMethod("POST");
        con.connect();
        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        out.write(loginTokens);
        out.close();
        InputStream stream;

        if (con.getResponseCode() == 200) {
            stream = con.getInputStream();
        } else {
            stream = con.getErrorStream();
        }

        String message = extract(stream);
        sessionKey = con.getHeaderField("Set-Cookie").split(";")[0];
        return message != null && message.contains("SuccessMessage");
    }

    public InputStream retrieveReport(RequestBuilder builder) throws IOException {
        if (sessionKey == null) {
            throw new ConnectionNotEstablished();
        }
        Request request = builder.getRequest();
        request.getColumns();
        StringBuilder requestString = new StringBuilder();
        URL url = new URL(BASE_URL + request.getReportName() + ".event");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println(BASE_URL + request.getReportName() + ".event");
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("Cookie", sessionKey);
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setRequestMethod("POST");
        con.connect();

        OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        out.write("ReportCmd=Filter&ReportCmd=CustomCommand&CustomCmdList=DownloadCSV&" + request.toString());
        System.out.println("ReportCmd=Filter&ReportCmd=CustomCommand&CustomCmdList=DownloadCSV&" + request.toString());
        out.close();

        if (con.getResponseCode() != 200) {
            throw new RuntimeException();
        }
        return con.getInputStream();
    }

    private static String extract(InputStream inputStream) throws IOException {
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;

        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            temp.write(buffer, 0, read);
        }
        temp.flush();

        return new String(temp.toByteArray(), "UTF-8");
    }

    public static class ConnectionNotEstablished extends RuntimeException {

    }

    public PAIReportsClient(RequestBuilder rBuilder) {
        requestBuilder = rBuilder;

    }

}
