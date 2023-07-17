package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import org.json.JSONArray;
import org.json.JSONObject;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the JSON file path: ");
        String filePath = scanner.nextLine();

        String jsonInput = new String(Files.readAllBytes(Paths.get(filePath)));

        JSONObject inputObj = new JSONObject(jsonInput);
        JSONObject request = inputObj.getJSONObject("request");
        JSONObject possibleValue = inputObj.getJSONObject("possibleValue");

        JSONObject body = possibleValue.has("body") ? possibleValue.getJSONObject("body") : new JSONObject();
        JSONObject pathParameters = possibleValue.has("pathParameters") ? possibleValue.getJSONObject("pathParameters") : new JSONObject();
        JSONObject queryParameters = possibleValue.has("queryParameters") ? possibleValue.getJSONObject("queryParameters") : new JSONObject();

        List<Map<String, String>> allCombinationList = main.generateAllCombinations(body, pathParameters, queryParameters, possibleValue);

        try (CSVWriter writer = new CSVWriter(new FileWriter("output.csv"))) {
            writer.writeNext(new String[]{"Curl Command"});
            for (Map<String, String> combination : allCombinationList) {
                boolean isForm = request.has("form");
                String curlCommand = generateCurlCommand(request, combination, isForm);
                writer.writeNext(new String[]{curlCommand});
            }
        }
    }


    public List<Map<String, String>> generateAllCombinations(JSONObject body, JSONObject pathParameters, JSONObject queryParameters, JSONObject possibleValues) {
        List<Map<String, String>> combinations = new ArrayList<>();

        List<Map<String, String>> bodyCombinations = generateCombinationsForSection(body, possibleValues.has("body") ? possibleValues.getJSONObject("body") : new JSONObject());
        List<Map<String, String>> pathCombinations = generateCombinationsForSection(pathParameters, possibleValues.has("pathParameters") ? possibleValues.getJSONObject("pathParameters") : new JSONObject());
        List<Map<String, String>> queryCombinations = generateCombinationsForSection(queryParameters, possibleValues.has("queryParameters") ? possibleValues.getJSONObject("queryParameters") : new JSONObject());

        for (Map<String, String> bodyCombination : bodyCombinations) {
            for (Map<String, String> pathCombination : pathCombinations) {
                for (Map<String, String> queryCombination : queryCombinations) {
                    Map<String, String> combination = new HashMap<>();
                    combination.putAll(bodyCombination);
                    combination.putAll(pathCombination);
                    combination.putAll(queryCombination);
                    combinations.add(combination);
                }
            }
        }

        return combinations;
    }


    public static List<Map<String, String>> generateCombinationsForSection(JSONObject section, JSONObject possibleValues) {
        List<Map<String, String>> combinations = new ArrayList<>();
        Deque<Map.Entry<String, JSONArray>> stack = new LinkedList<>();
        Deque<Integer> indexStack = new LinkedList<>();
        Map<String, String> currentCombination = new HashMap<>();
        int bodySize = 0;

        Iterator<String> keys = section.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!possibleValues.has(key)) {
                continue;
            }
            bodySize++;
            JSONArray values = possibleValues.getJSONArray(key);
            Map.Entry<String, JSONArray> entry = new AbstractMap.SimpleEntry<>(key, values);
            stack.push(entry);
            indexStack.push(0);
            break;
        }

        while (!stack.isEmpty()) {
            if (indexStack.peek() < stack.peek().getValue().length()) {
                currentCombination.put(stack.peek().getKey(), stack.peek().getValue().getString(indexStack.peek()));
                if (currentCombination.size() == bodySize) {
                    combinations.add(new HashMap<>(currentCombination));
                    indexStack.push(indexStack.pop() + 1);
                } else {
                    if (keys.hasNext()) {
                        String key = keys.next();
                        if (!possibleValues.has(key)) {
                            continue;
                        }
                        JSONArray values = possibleValues.getJSONArray(key);
                        Map.Entry<String, JSONArray> entry = new AbstractMap.SimpleEntry<>(key, values);
                        stack.push(entry);
                        indexStack.push(0);
                    }
                }
            } else {
                indexStack.pop();
                currentCombination.remove(stack.pop().getKey());
                if (keys.hasNext()) {
                    String key = keys.next();
                    if (!possibleValues.has(key)) {
                        continue;
                    }
                    JSONArray values = possibleValues.getJSONArray(key);
                    Map.Entry<String, JSONArray> entry = new AbstractMap.SimpleEntry<>(key, values);
                    stack.push(entry);
                    indexStack.push(0);
                }
            }
        }

        return combinations;
    }


    public static String generateCurlCommand(JSONObject request, Map<String, String> dataCombination, boolean isForm) {
        StringBuilder curlCommand = new StringBuilder("curl -X ");
        curlCommand.append(request.getString("methodType"));
        curlCommand.append(" '").append(request.getString("endPoint")).append("'");

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


}
