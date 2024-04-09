package com.jhonny.petstoreapp.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 * Azure Functions with Service Bus Trigger.
 */
public class MessageQueueTrigger {
    /**
     * This function will be invoked when a new message is received at the Service Bus Queue.
     */
    @FunctionName("putOrderByMessageQueue")
    public void run(
            @ServiceBusQueueTrigger(name = "message", queueName = "jhonnypetqueue", connection = "MessageBusStringConnection") String message,
            final ExecutionContext context
    ) {
        context.getLogger().info("Java Service Bus Queue trigger function executed.");
        context.getLogger().info(message);

        if (!message.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(message);
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
                blockBlob.uploadText(message);
                context.getLogger().info("Object saved to Blob Storage as " + blobName);
            } catch (Exception e) {
                context.getLogger().warning("Error saving object to Blob Storage: " + e.getMessage());
            }
        } else {
            context.getLogger().warning("Request body is empty.");
        }
    }
}
