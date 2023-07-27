package org.example.userRunner;

import org.json.*;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class JsonToSchemaGenerator {

    static String file_name = "nevvonAdminInCorrect";
    static String json = "{\n" +
            "    \"message\": \"Wrong email/password. Please try again\"\n" +
            "}";

    public static void main(String[] args) {

        JSONObject object = new JSONObject(json);
        JSONObject schema = generateSchema(object);

        try {
            File myObj = new File(file_name + ".json");
            if (myObj.createNewFile()) {
                Logger.info("File created: " + myObj.getName());

            } else {
                Logger.info("File already exists.");

            }
        } catch (IOException e) {
            Logger.error("An error occurred.");

            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(new File("src/main/resources/Schema/" + file_name + ".json").getAbsoluteFile());
            myWriter.write(schema.toString(2));
            myWriter.close();
            Logger.info("Successfully wrote to the file.");

        } catch (IOException e) {
            Logger.error("An error occurred.");

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
