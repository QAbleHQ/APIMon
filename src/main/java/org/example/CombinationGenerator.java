package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CombinationGenerator {


    private static ObjectMapper objectMapper = new ObjectMapper();

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
        String jsonString = readJsonFile("src/main/resources/requests/test.json");
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject request = jsonObject.getJSONObject("request");
        JSONObject possibleValues = jsonObject.getJSONObject("possibleValue");

        JSONObject formData = request.has("form") ? request.getJSONObject("form") : new JSONObject();
        JSONObject body = request.has("data") ? request.getJSONObject("data") : new JSONObject();
        JSONObject pathParameters = request.has("pathParameters") ? request.getJSONObject("pathParameters") : new JSONObject();
        JSONObject queryParameters = request.has("queryParameters") ? request.getJSONObject("queryParameters") : new JSONObject();

        List<Map<String, String>> combinations;

        Workbook workbook = new XSSFWorkbook(); // Use XSSF for xlsx format, HSSF for xls format
        Sheet sheet = workbook.createSheet("Combinations");

        int rowCount = 0;

        if (!formData.isEmpty()) {
            combinations = generateCombinations(formData, possibleValues.getJSONObject("form"));
            int i = 0;
            for (Map<String, String> combination : combinations) {
                try {
                    String curlCommand = generateCurlCommand(request, combination, true);
                    CurlCommandParser curl = new CurlCommandParser();

                    System.out.println("---------------");
                    System.out.println("Requesting this : " + curlCommand);
                    Map response = curl.executeCurlCommand(curlCommand);
                    System.out.println("Response : " + response);
                    System.out.println("---------------");

                    // Add a row to your Excel sheet
                    Row row = sheet.createRow(++rowCount);
                    row.createCell(0).setCellValue("Combination_" + i);
                    row.createCell(1).setCellValue(curlCommand);
                    row.createCell(2).setCellValue(response.get("url").toString());
                    row.createCell(3).setCellValue(response.get("method").toString());
                    row.createCell(4).setCellValue(response.get("headers").toString());
                    row.createCell(5).setCellValue(response.get("formData").toString());
                    row.createCell(6).setCellValue(response.get("data").toString());
                    row.createCell(7).setCellValue(response.get("statusCode").toString());
                    row.createCell(8).setCellValue(response.get("response").toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        } else if (!body.isEmpty()) {
            combinations = generateCombinations(body, possibleValues.getJSONObject("data"));
            int i = 0;
            for (Map<String, String> combination : combinations) {
                try {
                    String curlCommand = generateCurlCommand(request, combination, false);
                    System.out.println("Requesting this : " + curlCommand);
                    System.out.println("---------------");
                    CurlCommandParser curl = new CurlCommandParser();
                    Map response = curl.executeCurlCommand(curlCommand);

                    System.out.println("Response : " + response);

                    // Add a row to your Excel sheet
                    Row row = sheet.createRow(++rowCount);
                    row.createCell(0).setCellValue("Combination_" + i);
                    row.createCell(1).setCellValue(curlCommand);
                    row.createCell(2).setCellValue(response.get("url").toString());
                    row.createCell(3).setCellValue(response.get("method").toString());
                    row.createCell(4).setCellValue(response.get("headers").toString());
                    row.createCell(5).setCellValue(response.get("formData").toString());
                    row.createCell(6).setCellValue(response.get("data").toString());
                    row.createCell(7).setCellValue(response.get("statusCode").toString());
                    row.createCell(8).setCellValue(response.get("response").toString());


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("combinations.xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static final OkHttpClient client = new OkHttpClient();

    public static String generateCurlCommand(JSONObject request, Map<String, String> dataCombination, boolean isForm) {


        StringBuilder curlCommand = new StringBuilder("curl -X ");
        curlCommand.append(request.getString("methodType"));

        curlCommand.append(" --location '").append(request.getString("endPoint")).append("'");

        // Append headers
        JSONObject headers = request.getJSONObject("header");
        for (String key : headers.keySet()) {
            curlCommand.append(" -H '").append(key).append(": ").append(headers.getString(key)).append("'");
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
