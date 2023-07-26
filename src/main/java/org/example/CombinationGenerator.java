package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CombinationGenerator {

    public static String readJsonFile(String filePath) {
        String jsonStr = "";
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
            jsonStr = new String(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public static void main(String[] args) {
        String jsonString = readJsonFile("/Users/viralpatel/Downloads/APIMon/src/main/resources/requests/test.json");
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject request = jsonObject.getJSONObject("request");
        JSONObject possibleValues = jsonObject.getJSONObject("possibleValue");

        JSONObject formData = request.has("form") ? request.getJSONObject("form") : new JSONObject();
        JSONObject body = request.has("data") ? request.getJSONObject("data") : new JSONObject();
        JSONObject pathParameters = request.has("pathParameters") ? request.getJSONObject("pathParameters") : new JSONObject();
        JSONObject queryParameters = request.has("queryParameters") ? request.getJSONObject("queryParameters") : new JSONObject();

        List<Map<String, String>> combinations;
        List<Map<String, String>> results;

        boolean isForm = !formData.isEmpty();
        if (!formData.isEmpty()) {
            results = generateAndTestCombinations(formData, possibleValues.getJSONObject("form"), request, true);
        } else if (!body.isEmpty()) {
            results = generateAndTestCombinations(body, possibleValues.getJSONObject("data"), request, false);
        } else {
            throw new IllegalArgumentException("No form or body data provided");
        }

        // Create a new Workbook and a Sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Results");

            // Create a CellStyle for the header
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Create the header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Unique ID for the combination", "Curl Command", "Header", "Body", "Form", "Response", "Status Code"};
            for (int i = 0; i < headers.length; i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(headers[i]);
                headerCell.setCellStyle(headerStyle);
            }

            // Populate the data rows
            int rowIndex = 1;
            for (Map combination : results) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue("Combination_" + (rowIndex - 1)); // Unique ID for the combination

                // Modify the row cells based on the actual combination data
                System.out.println("Curl" + combination.get("Curl Command").toString());
                row.createCell(1).setCellValue(combination.get("Curl Command").toString()); // Curl Command

                // You can set the header, body, and form cells using the values from the request JSON
                row.createCell(2).setCellValue(request.getJSONObject("header").toString()); // Header
                row.createCell(3).setCellValue(body.toString()); // Body
                row.createCell(4).setCellValue(formData.toString()); // Form

                // Retrieve the response and status code from the finalResponse map and set them in the cells

                row.createCell(5).setCellValue(combination.get("response").toString()); // Response
                row.createCell(6).setCellValue(combination.get("statusCode").toString()); // Status Code


                System.out.println("Combination " + (rowIndex - 1) + ": " + combination);
            }
            try (FileOutputStream fileOut = new FileOutputStream("output.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<Map<String, String>> generateAndTestCombinations(JSONObject data, JSONObject possibleValues, JSONObject request, boolean isForm) {
        List<Map<String, String>> combinations = generateCombinations(data, possibleValues);

        int i = 0;
        for (Map<String, String> combination : combinations) {
            try {
                String curlCommand = generateCurlCommand(request, combination, isForm);
                CurlCommandParser curl = new CurlCommandParser();
                Map<String, String> response = curl.executeCurlCommand(curlCommand);

                // Add the curl command and response to the combination so we can include it in the result
                combination.put("Curl Command", curlCommand);
                combination.putAll(response);

            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }

        return combinations;
    }


    public static String generateCurlCommand(JSONObject request, Map<String, String> dataCombination, boolean isForm) {
        StringBuilder curlCommand = new StringBuilder("curl -X ");
        curlCommand.append(request.getString("methodType"));

        curlCommand.append(" --location '").append(request.getString("endPoint")).append("'");

        // Append headers
        JSONObject headers = request.getJSONObject("header");
        for (String key : headers.keySet()) {
            curlCommand.append(" -H '").append(key).append(" : ").append(headers.getString(key)).append("'");
        }

        // Append data
        if (isForm) {
            for (Map.Entry<String, String> entry : dataCombination.entrySet()) {
                curlCommand.append(" --form '").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"'");
            }
        } else {
            curlCommand.append(" -d '");
            JSONObject newBody = new JSONObject(dataCombination);
            curlCommand.append(newBody.toString()).append("'");
        }
        return curlCommand.toString();
    }

    public static List<Map<String, String>> generateCombinations(JSONObject formData, JSONObject possibleValues) {
        List<Map<String, String>> combinations = new ArrayList<>();
        generateCombinationsRecursive(new LinkedHashMap<>(), new ArrayList<>(formData.keySet()), formData, possibleValues, combinations);
        return combinations;
    }

    private static void generateCombinationsRecursive(Map<String, String> currentCombination, List<String> remainingKeys, JSONObject formData, JSONObject possibleValues, List<Map<String, String>> combinations) {
        if (remainingKeys.isEmpty()) {
            combinations.add(new HashMap<>(currentCombination));
            return;
        }

        String key = remainingKeys.get(0);
        List<String> remainingKeysCopy = new ArrayList<>(remainingKeys);
        remainingKeysCopy.remove(0);

        JSONArray values = possibleValues.getJSONArray(key);
        for (int i = 0; i < values.length(); i++) {
            currentCombination.put(key, values.get(i).toString());
            generateCombinationsRecursive(currentCombination, remainingKeysCopy, formData, possibleValues, combinations);
            currentCombination.remove(key);
        }
    }
}
