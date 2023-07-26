package org.example;

import okhttp3.*;

import java.io.IOException;
import java.util.*;

public class CurlCommandParser {
    private OkHttpClient client = new OkHttpClient();


    public Map executeCurlCommand(String curlCommand) throws IOException {


        String method = getMethods(curlCommand);
        String url = getURl(curlCommand);
        Map<String, String> Headers = getHeaders(curlCommand);
        Map<String, String> formData = getForm(curlCommand);
        String data = getData(curlCommand);

        Map finalResponse = new HashMap();

        finalResponse.put("method", method);
        finalResponse.put("url", url);
        finalResponse.put("headers", Headers);
        finalResponse.put("formData", formData);
        finalResponse.put("data", data);



        Request.Builder builder = new Request.Builder().url(url);

        for (Map.Entry<String, String> entry : Headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        RequestBody requestBody = null;

        if (!formData.isEmpty()) {
            MultipartBody.Builder multiPartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (Map.Entry<String, String> entry : formData.entrySet()) {
                multiPartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }

            requestBody = multiPartBuilder.build();
        } else if (!data.isEmpty()) {
            requestBody = RequestBody.create(data, MediaType.parse("application/json; charset=utf-8"));
        }

        switch (method.toUpperCase()) {
            case "GET":
                builder.get();
                break;
            case "POST":
                builder.post(requestBody);
                break;
            case "PUT":
                builder.put(requestBody);
                break;
            case "DELETE":
                if (requestBody != null) builder.delete(requestBody);
                else builder.delete();
                break;
            case "PATCH":
                builder.patch(requestBody);
                break;
            case "HEAD":
                builder.head();
                break;
            case "OPTIONS":
                builder.method("OPTIONS", requestBody);
                break;
            case "TRACE":
                builder.method("TRACE", requestBody);
                break;
            case "CONNECT":
                builder.method("CONNECT", requestBody);
                break;
            default:
                throw new IllegalArgumentException("HTTP method not supported: " + method);
        }

        Request request = builder.build();

        String responseString = null;
        String statusCode = null;
        try (Response response = client.newCall(request).execute()) {
            statusCode = String.valueOf(response.code());
            responseString = response.body().string();
        }
      //  System.out.println(responseString);

        finalResponse.put("response", responseString);
        finalResponse.put("statusCode", statusCode);

        return finalResponse;
    }


    public String getURl(String curlCommand) {
        String[] parts = curlCommand.split("'");

        StringBuffer re = new StringBuffer();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains("--location")) {
                re.append(parts[i + 1].trim());
            }
        }
        return re.toString();
    }

    public Map getHeaders(String curlCommand) {

        String[] parts = curlCommand.split("--header");

        if(parts.length == 1)
        {
            parts = curlCommand.split("-H");
        }
        Map headers = new HashMap();
        for (int i = 1; i < parts.length; i++) {
            String header = parts[i].trim().split("'")[1];
            headers.put(header.split(":")[0].trim(), header.split(":")[1].trim());
            //System.out.println(header);
        }
     //   System.out.println(headers);
        return headers;
    }


    public static String getData(String curlCommand) {

        String[] parts = curlCommand.split("--data");

        if(parts.length == 1)
        {
            parts = curlCommand.split("-d");
        }
        String data = "";
        if (parts.length > 1) {
            data = parts[1].trim().split("'")[1];
            System.out.println(data);
        }
        return data;
    }

    public static String getMethods(String curlCommand) {
        String[] parts = curlCommand.split(" ");

        String method = "";
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("-X")) {
                method = parts[i + 1].trim();
     //           System.out.println(method);
            }
        }
        return method;
    }

    public static Map getForm(String curlCommand) {
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
        for (Map.Entry<String, String> entry : formData.entrySet()) {
     //       System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        return formData;
    }
}
