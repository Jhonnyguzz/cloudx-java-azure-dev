package com.jhonny.petstoreapp.functions.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhonny.petstoreapp.functions.settings.LocalProperties;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

public class BlobStorage {

  public static void sendMessageToBlobStorage(String message, ExecutionContext context) {
    if (!message.isEmpty()) {
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);
        System.err.println(jsonNode);
        String id = jsonNode.get("id").asText();

        CloudStorageAccount storageAccount = CloudStorageAccount.parse(LocalProperties.STORAGE_CONNECTION);
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference(LocalProperties.CONTAINER_NAME);

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
