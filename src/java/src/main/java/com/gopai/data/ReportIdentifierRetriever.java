package com.gopai.data;

import com.gopai.pair.sdk.v1.PAIClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReportIdentifierRetriever {
    PAIClient client;

    public ReportIdentifierRetriever(PAIClient client) {
        this.client = client;
    }

    public List<ReportIdentifier> findAllConfigs() throws IOException {
        String query = "SELECT * FROM ReportConfigs r ORDER BY r.Name";
        return getConfigs(query);
    }

    public List<ReportIdentifier> findConfigByGUID(String guid) throws IOException {
        String query = "SELECT * FROM ReportConfigs r WHERE r.ReportGUID = \'" + guid + "\' ORDER BY r.Name";
        return getConfigs(query);
    }

    public List<ReportIdentifier> findConfigByName(String name) throws IOException {
        String query = "SELECT * FROM ReportConfigs r WHERE r.Name = \'" + name + "\' ORDER BY r.Name";
        return getConfigs(query);
    }

    public List<ReportIdentifier> findConfigByExternalName(String extName) throws IOException {
        String query = "SELECT * FROM ReportConfigs r WHERE r.ExternalName = \'" + extName + "\' ORDER BY r.Name";
        return getConfigs(query);
    }

    private List<ReportIdentifier> getConfigs(String query) throws IOException {
        ReportIdentifier[] config = client.runQuery(query, ReportIdentifier[].class);
        return Arrays.asList(config);
    }
}
