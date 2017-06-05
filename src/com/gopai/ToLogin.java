package com.gopai;

import java.io.*;

/**
 * Created by jemima.nyamogo on 5/18/2017.
 */
public class ToLogin {
    public static void main(String[] args) throws Exception {
        PAIReportsClient client = new PAIReportsClient(new RequestBuilder());
        client.connect("MyTestUser", "LetMeIn1");
        InputStream stream = client.retrieveReport(RequestBuilder.report("GetNewUserReport").column("City").filter("Billings").visible(false).build());
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String consoleInput;

        while ((consoleInput = input.readLine()) != null)
            System.out.println(consoleInput);
        input.close();

    }
}


