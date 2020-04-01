package com.simoncariou.pepperhome;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileParser {
    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getApiKey (InputStream fileIS) throws Exception {
        InputStream fin = fileIS;
        String api_key = convertStreamToString(fin);
        fin.close();
        return api_key;
    }
}
