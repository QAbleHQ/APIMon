package org.example.userRunner;

import org.json.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class JsonToSchemaGenerator {

    static String file_name = "Sequesce";
    static String json = "{\n" +
            "    \"auth\": true,\n" +
            "    \"message\": \"Loggin ok\",\n" +
            "    \"organization\": {\n" +
            "        \"id\": 395,\n" +
            "        \"name\": \"API Agency\",\n" +
            "        \"display_name\": null,\n" +
            "        \"partner_id\": null,\n" +
            "        \"location_mandatory\": 1,\n" +
            "        \"discipline_mandatory\": 1,\n" +
            "        \"hide_purchased_hours\": 0,\n" +
            "        \"image\": null\n" +
            "    },\n" +
            "    \"user\": {\n" +
            "        \"id\": 2694,\n" +
            "        \"email\": \"apicoordinator@mailinator.com\",\n" +
            "        \"view\": \"organization\",\n" +
            "        \"name\": \"API agency\",\n" +
            "        \"organization_roles\": [\n" +
            "            {\n" +
            "                \"id\": 2782,\n" +
            "                \"name\": \"coordinator\",\n" +
            "                \"display_name\": \"Coordinator\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"meta_actions\": [\n" +
            "            \"organization_generic\",\n" +
            "            \"edit_provider_details_including_roles\",\n" +
            "            \"change_language\",\n" +
            "            \"deactivate_activate_provider_from_a_program\",\n" +
            "            \"deactivate_activate_provider_from_program_bulk\",\n" +
            "            \"deactivate_activate_provider_from_the_agency\",\n" +
            "            \"deactivate_activate_provider_from_the_agency_bulk\",\n" +
            "            \"download_certificate\",\n" +
            "            \"download_certificate_bulk\",\n" +
            "            \"edit_provider_due_date\",\n" +
            "            \"edit_provider_due_date_bulk\",\n" +
            "            \"add_provider_to_a_program\",\n" +
            "            \"add_provider_to_a_program_bulk\",\n" +
            "            \"send_reset_pw_email\",\n" +
            "            \"send_a_notification\",\n" +
            "            \"send_a_notification_bulk\",\n" +
            "            \"send_certificate\",\n" +
            "            \"send_certificate_bulk\",\n" +
            "            \"view_all_provider_details_including_roles\",\n" +
            "            \"export\",\n" +
            "            \"run_reports\",\n" +
            "            \"can_view_program_catalog\",\n" +
            "            \"edit_provider_start_date\",\n" +
            "            \"edit_provider_start_date_bulk\",\n" +
            "            \"session_management\",\n" +
            "            \"list_provider\",\n" +
            "            \"scheduled_report_management\",\n" +
            "            \"view_schedule_reports\",\n" +
            "            \"view_user_filters\",\n" +
            "            \"user_filters_management\",\n" +
            "            \"can_view_user_emails\"\n" +
            "        ],\n" +
            "        \"download_token\": \"RrJ31g5nHD9qYCq37UQx1RzvOBVRS4zMjvYphG345uWUNPOW5v\",\n" +
            "        \"image\": null,\n" +
            "        \"img64\": null,\n" +
            "        \"organization\": \"API Agency\",\n" +
            "        \"organization_display_name\": null,\n" +
            "        \"country_code\": \"+1\",\n" +
            "        \"is_admin\": false,\n" +
            "        \"default_landing_page\": {\n" +
            "            \"id\": 1,\n" +
            "            \"key\": \"dashboard\",\n" +
            "            \"name\": \"Dashboard\",\n" +
            "            \"description\": \"Dashboard page is set as default landing page\",\n" +
            "            \"order\": 1\n" +
            "        }\n" +
            "    },\n" +
            "    \"access_token\": \"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjY0YTY0MTQ4MzlkY2YyODIxYTcwZWVjZWEwZjc5NzcxMjBlYTNlODlmNDM1ODFlZDZmMzAwMGE2MzY4NWZjYTQwODgxN2M5MjQzYjQ0Mzc0In0.eyJhdWQiOiI2NjE3IiwianRpIjoiNjRhNjQxNDgzOWRjZjI4MjFhNzBlZWNlYTBmNzk3NzEyMGVhM2U4OWY0MzU4MWVkNmYzMDAwYTYzNjg1ZmNhNDA4ODE3YzkyNDNiNDQzNzQiLCJpYXQiOjE2OTAzNjY2MzMsIm5iZiI6MTY5MDM2NjYzMywiZXhwIjoxNzIxOTg5MDMzLCJzdWIiOiIyNjk0Iiwic2NvcGVzIjpbXX0.XcxcoqdHKU6YNhQV0bK37B1ykjsSf365kZeKOInDoy85jm7VuPo6DjIBFpLj0DAdIaamSRCRhRgidcHh3G0Bi2pfZwleec0EUxloT9fjSSU-vy9XHjER9wDsX4gU77te-cGJX_CBMTwwZ83tcxtVhiA_8d9zU6meMV8O6eMOchpZZ896biz0j9gwbvV0zWTezX6aeOI15GnMRk4qgILv4kvm2U1C4VM0lyxMHOaU8xNSReFuFSTwor6Yu7rsHpfqjiHypk23NFZMM7-a91OZPcFstM7NhbbsB4iDd5Fp1dKqfb-_7y0_UNtG4ASG6Oa5siS-Q5sweXMJ_mVJMQT-FrY9dZbK7cgMECy1mlvHeas5Dy-qa0sFPz7bhY5tpL6JMReIJhw8ne6Tcuuu47A1lPkK2SBHBcq-RmZEpsYOaCc0_E2WBAkT_4jztLiHj46FnROhPfKWbaZ6Qf0KnTkh5fLKS_s2SGUxfNNQ5rHAq-Uawg2hPi1ie7VFsYoFpo0yDj0jnMnWiyxWOaiYAs60AnDEerNjpuHHcC9hNxjC7IE1Hw5xFqgRErMkDkMgp6AeO2bcKqQkiRAWKm9QYmqLNA3pBceviJf9Gk5yE7Ds7b6PlgI3ph60uVXF0UOlDeX2DADhB99rUt98iYGBQzJn-qMZ_AMoCQtFKa59AgmB__I\",\n" +
            "    \"expires_at\": \"2024-07-26 10:17:13\"\n" +
            "}";
    public static void main(String[] args) {

       JSONObject object = new JSONObject(json);
        JSONObject schema = generateSchema(object);

        try {
            File myObj = new File(file_name + ".json");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(new File("src/main/resources/Schema/"+file_name + ".json").getAbsoluteFile());
            myWriter.write(schema.toString(2));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    private static JSONObject generateSchema(JSONObject jsonObject) {
        JSONObject schema = new JSONObject();
        schema.put("type", "object");
        JSONObject properties = new JSONObject();

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof String) {
                properties.put(key, new JSONObject().put("type", "string"));
            } else if (value instanceof Integer) {
                properties.put(key, new JSONObject().put("type", "integer"));
            } else if (value instanceof Boolean) {
                properties.put(key, new JSONObject().put("type", "boolean"));
            } else if (value instanceof JSONObject) {
                properties.put(key, generateSchema((JSONObject) value));
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                if (array.length() > 0) {
                    Object firstItem = array.get(0);
                    if (firstItem instanceof JSONObject) {
                        JSONObject itemsSchema = generateSchema((JSONObject) firstItem);
                        properties.put(key, new JSONObject().put("type", "array").put("items", itemsSchema));
                    } else {
                        String itemType = firstItem.getClass().getSimpleName().toLowerCase();
                        JSONObject itemsSchema = new JSONObject().put("type", itemType);
                        properties.put(key, new JSONObject().put("type", "array").put("items", itemsSchema));
                    }
                } else {
                    properties.put(key, new JSONObject().put("type", "array").put("items", new JSONObject()));
                }
            } else if (value == JSONObject.NULL) {
                properties.put(key, new JSONObject().put("type", "null"));
            } else if (value instanceof Double) {
                properties.put(key, new JSONObject().put("type", "number"));
            } else if (value instanceof Long) {
                properties.put(key, new JSONObject().put("type", "integer"));
            }
            // Add more types as needed...
        }
        schema.put("properties", properties);


        return schema;
    }
}
