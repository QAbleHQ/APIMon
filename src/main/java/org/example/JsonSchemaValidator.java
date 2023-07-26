package org.example;


import com.github.erosb.jsonsKema.*;

public class JsonSchemaValidator {


    public void validateSchema(){





    }
    public static void main(String[] args) {
        // Load the schema from a JSON string





    JsonValue schemaJson = new JsonParser("{\n" +
                "    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "    \"type\": \"object\",\n" +
                "    \"properties\": {\n" +
                "        \"auth\": { \"type\": \"boolean\" },\n" +
                "        \"message\": { \"type\": \"string\" },\n" +
                "        \"organization\": {\n" +
                "            \"type\": \"object\",\n" +
                "            \"properties\": {\n" +
                "                \"id\": { \"type\": \"integer\" },\n" +
                "                \"name\": { \"type\": \"string\" },\n" +
                "                \"display_name\": { \"type\": \"null\" },\n" +
                "                \"partner_id\": { \"type\": \"null\" },\n" +
                "                \"location_mandatory\": { \"type\": \"integer\" },\n" +
                "                \"discipline_mandatory\": { \"type\": \"integer\" },\n" +
                "                \"hide_purchased_hours\": { \"type\": \"integer\" },\n" +
                "                \"image\": { \"type\": \"null\" }\n" +
                "            },\n" +
                "            \"required\": [\"id\", \"name\", \"location_mandatory\", \"discipline_mandatory\", \"hide_purchased_hours\"]\n" +
                "        },\n" +
                "        \"user\": {\n" +
                "            \"type\": \"object\",\n" +
                "            \"properties\": {\n" +
                "                \"id\": { \"type\": \"integer\" },\n" +
                "                \"email\": { \"type\": \"string\" },\n" +
                "                \"view\": { \"type\": \"string\" },\n" +
                "                \"name\": { \"type\": \"string\" },\n" +
                "                \"organization_roles\": {\n" +
                "                    \"type\": \"array\",\n" +
                "                    \"items\": {\n" +
                "                        \"type\": \"object\",\n" +
                "                        \"properties\": {\n" +
                "                            \"id\": { \"type\": \"integer\" },\n" +
                "                            \"name\": { \"type\": \"string\" },\n" +
                "                            \"display_name\": { \"type\": \"string\" }\n" +
                "                        },\n" +
                "                        \"required\": [\"id\", \"name\", \"display_name\"]\n" +
                "                    }\n" +
                "                },\n" +
                "                \"meta_actions\": {\n" +
                "                    \"type\": \"array\",\n" +
                "                    \"items\": { \"type\": \"string\" }\n" +
                "                },\n" +
                "                \"download_token\": { \"type\": \"string\" },\n" +
                "                \"image\": { \"type\": \"null\" },\n" +
                "                \"img64\": { \"type\": \"null\" },\n" +
                "                \"organization\": { \"type\": \"string\" },\n" +
                "                \"organization_display_name\": { \"type\": \"null\" },\n" +
                "                \"country_code\": { \"type\": \"string\" },\n" +
                "                \"is_admin\": { \"type\": \"boolean\" },\n" +
                "                \"default_landing_page\": {\n" +
                "                    \"type\": \"object\",\n" +
                "                    \"properties\": {\n" +
                "                        \"id\": { \"type\": \"integer\" },\n" +
                "                        \"key\": { \"type\": \"string\" },\n" +
                "                        \"name\": { \"type\": \"string\" },\n" +
                "                        \"description\": { \"type\": \"string\" },\n" +
                "                        \"order\": { \"type\": \"integer\" }\n" +
                "                    },\n" +
                "                    \"required\": [\"id\", \"key\", \"name\", \"description\", \"order\"]\n" +
                "                }\n" +
                "            },\n" +
                "            \"required\": [\"id\", \"email\", \"view\", \"name\", \"organization_roles\", \"meta_actions\", \"download_token\", \"organization\", \"country_code\", \"is_admin\", \"default_landing_page\"]\n" +
                "        },\n" +
                "        \"access_token\": { \"type\": \"string\" },\n" +
                "        \"expires_at\": { \"type\": \"string\" }\n" +
                "    },\n" +
                "    \"required\": [\"auth\", \"message\", \"organization\", \"user\", \"access_token\", \"expires_at\"]\n" +
                "}\n").parse();

        Schema schema = new SchemaLoader(schemaJson).load();

        JsonValue instance = new JsonParser("{\n" +
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
                "            \"description\": 1,\n" +
                "            \"order\": 1\n" +
                "        }\n" +
                "    },\n" +
                "    \"access_token\": \"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjNjNjc4MTcyMzc4OTIxMGVlNmUxM2NjNjMyZjY1YWEzY2E1ZWJkYWZkOTRkYzdlNTRhNWIwNjRjYTVkMmJlZGQ3NDQ5NzYwZGM3MzVkNzIyIn0.eyJhdWQiOiI2NjE3IiwianRpIjoiM2M2NzgxNzIzNzg5MjEwZWU2ZTEzY2M2MzJmNjVhYTNjYTVlYmRhZmQ5NGRjN2U1NGE1YjA2NGNhNWQyYmVkZDc0NDk3NjBkYzczNWQ3MjIiLCJpYXQiOjE2ODkyNjEwMTAsIm5iZiI6MTY4OTI2MTAxMCwiZXhwIjoxNzIwODgzNDEwLCJzdWIiOiIyNjk0Iiwic2NvcGVzIjpbXX0.bNc_UmfxljHKW3FCCsPVJOiJmbibyGISFgcWzlbcOBTtNrTHDQeVfSBzlJwDBQjxfUnF8KkR2s9WJHfTYwd3k2hgWiogbZtlwTiVyRoyMcivaLoIYHvKWiD1VKCTiU0F1NK4lm54bTW2JHwQWYbHlTY57g2AYnaMMAsFbbEawNsM-1OFW-I8V3K8MDdx2ji356lrB2_4cKr1rzbUKrU7-Y_Wla3dGbUTN2Ai4bh7AdPFJdBL536Nlz3KkDWRktSWeRbcNUuUetMgdYIw0vv1W-jSLOJehjkIQQdgVne9bkCgoX7NSNvcGRniCvY-AvLv2lvm9MEDZ7toXhRYiYRbkgkm3_ilVuOCa50duN4Fm5DOTTgb-kbY51uDpyI90hCd68HZnhjGj8l84opPjPieB_eLisZfA-QvXvdj1LPZWzQt8AjOjSMW963CfreyqtECgWz02YBRQ3dpGyH6dmsC2Pi5hf4SGWkpuTWCuaKzDngzLUpEgVctSy7fgUb7O0snE_DNLjrJCETnDFTG0g_fUp6XebR7-Mcpg8KDxxy40FRoL5CAVVZXyHUVmO6D147GQh5ma5uFJU-PQjQ7FTgrBAEptHVBROY2kV49FMW0WAYruQ5baYSDgAnLhp5-RKj0YBOAlxe6YreBo2gZTbDBeJ9ro8d6gkmyfNLLzYdfrsg\",\n" +
                "    \"expires_at\": \"2024-07-13 15:10:10\"\n" +
                "}" ).parse();

        Validator validator = Validator.forSchema(schema);
        ValidationFailure failure = validator.validate(instance);

        // print the validation failures (if any)
        if(failure != null) {
            System.out.println("Validation Failed: " + failure);
        } else {
            System.out.println("Validation Successful");
        }
    }
}
