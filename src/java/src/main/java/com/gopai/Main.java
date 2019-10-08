package com.gopai;

import com.gopai.pair.sdk.v1.PAIClient;

public class Main {
    public static void main(String[] args) {
        try (PAIClient client = new PAIClient()) {
            client.connect("michael.drake", "let Mick in 2");

            System.out.println("Start");

//            for (TableDefinition tables : client.runQuery("show tables", TableDefinition[].class)) {
//                System.out.println(tables);
//            }

            System.out.println(client.retrieveReportConfig("DC3A361E-7C96-E411-8519-D4AE52896C05"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class TableDefinition {
        String Table;
        String IsAllowedInFrom;

        @Override
        public String toString() {
            return "TableDefinition{" +
                    "Table='" + Table + '\'' +
                    ", IsAllowedInFrom='" + IsAllowedInFrom + '\'' +
                    '}';
        }
    }
}
