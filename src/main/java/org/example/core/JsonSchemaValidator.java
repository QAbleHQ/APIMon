package org.example.core;


import com.github.erosb.jsonsKema.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.tinylog.Logger;

public class JsonSchemaValidator {


    public JSONObject validateSchema(JSONArray schemaFileList, String jsonString) {

        Logger.info("Starting the Schema Validator");
        StringBuffer finalResultMessage = new StringBuffer();

        JSONObject result = new JSONObject();


        int passCount = 0;
        for (Object fileName : schemaFileList) {
            File file = new File("src/main/resources/Schema/" + fileName.toString());
            String schemaString = null;
            try {
                schemaString = FileUtils.readFileToString(file, "UTF-8");

                Logger.debug("Schema String" + schemaString);

                Logger.debug("Response String" + jsonString);


                JSONObject schemaJson = new JSONObject(new JSONTokener(schemaString));


                JSONObject jsonToValidate = new JSONObject(new JSONTokener(jsonString));
                Schema schema = SchemaLoader.load(schemaJson);


                JsonValue instance = new JsonParser(jsonString).parse();


                try {
                    schema.validate(jsonToValidate);
                    Logger.info("Validation Passed");

                    finalResultMessage.append("----------------------------------");
                    finalResultMessage.append("Schema File Name: " + fileName.toString() + "\n");
                    finalResultMessage.append("Validation Passed");
                    finalResultMessage.append("\n");
                    finalResultMessage.append("----------------------------------");
                    passCount++;
                } catch (ValidationException e) {
                    Logger.error("Validation Failed");

                    finalResultMessage.append("----------------------------------");
                    finalResultMessage.append("\n");
                    finalResultMessage.append("Schema File Name: " + fileName.toString());
                    finalResultMessage.append("Validation Failed: " + e.getAllMessages());
                    finalResultMessage.append("\n");
                    finalResultMessage.append("----------------------------------");

                    e.getAllMessages().forEach(Logger::error);

                }


                // print the validation failures (if any)


            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        if (passCount > 0) {
            result.put("result", "passed");
        } else {
            result.put("result", "failed");
        }


        result.put("message", finalResultMessage.toString());
        return result;

    }


}
