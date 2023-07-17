package org.example;

import java.util.HashMap;
import java.util.Map;

public class CurlTester {

    public static void main(String[] args) {
        String curlCommand = "curl -X POST 'https://goldenfantasy11-api.staging-server.in/api-front/user/add_contest' -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NDk0MGExNDc3YWRmNWE2NWRhMTRjOGMiLCJpYXQiOjE2ODgxMDkxNjl9.qoT8aIdjLPPfyrEOjvbVW85KPeGhyMt0hmNJP7tmdnA' -H 'Content-Type: multipart/form-data' --form 'price=\"[{\"from\": 1,\"to\": 1,\"price\":100},{\"from\": 2,\"to\": 3,\"price\":50}]\"' --form 'match_id=\"64883bcc42497f48b9bc7500\"' --form 'game_type=\"cricket\"' --form 'entry_fee=\"30\"' --form 'title=\"testing private contest 1\"' --form 'total_team=\"20\"' --form 'friend_join_multi_team=\"false\"' --form 'prize_pool=\"200\"'\n";

        getHeader(curlCommand);
        getData(curlCommand);
        getMethods(curlCommand);
        getForm(curlCommand);
    }

    public static Map getHeader(String curlCommand) {

        String[] parts = curlCommand.split("--header");

        if(parts.length == 1)
        {
             parts = curlCommand.split("-H");
        }

        Map headers = new HashMap();
        for (int i = 1; i < parts.length; i++) {
            String header = parts[i].trim().split("'")[1];
            headers.put(header.split(":")[0].trim(), header.split(":")[1].trim());
            System.out.println(header);
        }
        System.out.println("headers" +headers);
        return headers;
    }

    public static String getData(String curlCommand) {

        String[] parts = curlCommand.split("--data");
        String data = "";
        if (parts.length > 1) {
            data = parts[1].trim().split("'")[1];
            System.out.println("data" +data);
        }
        return data;
    }

    public static String getMethods(String curlCommand) {
        String[] parts = curlCommand.split(" ");

        String method = "";
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("-X")) {
                method = parts[i + 1].trim();
                System.out.println("method" +method);
            }
        }
        return method;
    }

    public static Map getForm(String curlCommand)
    {
        String[] parts = curlCommand.split("--form");

        Map<String, String> formData = new HashMap<>();

        for (int i = 1; i < parts.length; i++) {
            String formParam = parts[i].trim().split("'")[1];
            String[] keyValue = formParam.split("=", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim().substring(1, keyValue[1].trim().length() - 1);
            formData.put(key, value);
        }

        // Print the form data map
        for (Map.Entry<String,String> entry : formData.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        return formData;
    }






}


