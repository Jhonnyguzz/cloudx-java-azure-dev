package com.jhonny.petstoreapp.functions;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhonny.petstoreapp.functions.model.Order;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerJava {
    /**
     * This function listens at endpoint "/api/HttpTriggerJava". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerJava
     * 2. curl {your host}/api/HttpTriggerJava?name=HTTP%20Query
     */

    @FunctionName("putorder")
    public void run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        context.getLogger().info("Java HTTP trigger processed a request.");

        // Obtener el cuerpo de la solicitud HTTP
        String requestBody = request.getBody().orElse(null);

        if (requestBody != null && !requestBody.isEmpty()) {
            try {
                // Convertir el cuerpo de la solicitud a un objeto JSON
                //ObjectMapper objectMapper = new ObjectMapper();
                //Order order = objectMapper.readValue(requestBody, Order.class);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(requestBody);
                System.err.println(jsonNode);
                String id = jsonNode.get("id").asText();

                // Obtener una referencia al contenedor de Blob Storage
                String storageConnectionString = "";
                String containerName = "petstoreblobstorage";

                CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                CloudBlobContainer container = blobClient.getContainerReference(containerName);

                // Generar un nombre de archivo único para el blob
                String blobName = id + ".json";

                // Convertir el objeto JSON a formato de bytes
                byte[] data = objectMapper.writeValueAsBytes(jsonNode);

                // Guardar el objeto en el Blob Storage
                CloudBlockBlob blockBlob = container.getBlockBlobReference(blobName);
                blockBlob.uploadText(requestBody);
                //blockBlob.uploadFromByteArray(data, 0, data.length);

                context.getLogger().info("Object saved to Blob Storage as " + blobName);
            } catch (Exception e) {
                context.getLogger().warning("Error saving object to Blob Storage: " + e.getMessage());
            }
        } else {
            context.getLogger().warning("Request body is empty.");
        }
    }
}
