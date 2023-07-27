# APIMON

APIMON is a project designed for testing API endpoints with multiple combinations. This README provides the necessary steps and guidelines to use this tool effectively.

## Setup & Execution

Follow these steps to set up and use the APIMON tool:

1. **Clone the Repository**
   Clone the repository using the command: `git clone {repository_url}`.

2. **Create a Request File**
   Navigate to the `resources/requests` directory. Here, create a new request file in JSON format.

   The request file should follow the below template:

   ```json
   {
     "request": {
       "endPoint": "{api_endpoint}",
       "methodType": "{http_method}",
       "header": {
         "{header_key1}": "{header_value1}",
         "{header_key2}": "{header_value2}"
       },
       "data": {
         "{key1}": "{value_type1}",
         "{key2}": "{value_type2}"
       }
     },
     "possibleValue": {
       "data": {
         "{key1}": ["{possible_value1}", "{possible_value2}"],
         "{key2}": ["{possible_value1}", "{possible_value2}"]
       }
     },
     "SchemaFiles": ["{schema_file1.json}", "{schema_file2.json}"]
   }
   ```
   Replace `{api_endpoint}`, `{http_method}`, `{header_key}`, `{header_value}`, `{key}`, `{value_type}`, `{possible_value}`, and `{schema_file.json}` with appropriate values as per your requirement.

   **Note:** `endPoint`, `methodType`, `header`, `data`, `possibleValue`, and `SchemaFiles` fields are mandatory. Also, remember to define all the keys for the `possibleValue` that you have defined in the `data` section.

3. **Generate Response Schema**
   Generate the schema for all the types of responses you are expecting and define the schema file name in the `SchemaFiles` key in the request file. use the platform like this to generate the schema file : https://www.liquid-technologies.com/online-json-to-schema-converter

4. **Define Request File in CombinationGenerator**
   Navigate to the `CombinationGenerator` class and define your request file name.

5. **Run CombinationGenerator**
   Run the `CombinationGenerator` class and wait until the process is completed.

6. **Observe the Results**
   Navigate to the `TestResult` folder to observe the output in the Excel file.

## How It Works

The `CombinationGenerator` reads the request file and generates combinations of requests based on the provided `possibleValue`. It sends these requests one by one and records the responses.

After receiving the response, it reads all the schema files and tries to compare the schema with all the possible responses. If the response matches with any one of the schemas, it marks the combination as a pass. If it does not match with any schema, it marks the combination as failed.
