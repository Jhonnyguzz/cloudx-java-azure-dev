package io.swagger.configuration;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.CosmosException;
import com.azure.cosmos.implementation.apachecommons.lang.StringUtils;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.models.ThroughputProperties;
import com.chtrembl.petstore.order.model.Order;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CosmoDB {

  static final Logger log = LoggerFactory.getLogger(CosmoDB.class);
  private final String databaseName = "Petstoreapp";
  private final String containerName = "Orders";
  private CosmosClient client;
  private CosmosDatabase database;
  private CosmosContainer container;

  public static String MASTER_KEY =
      System.getProperty("ACCOUNT_KEY",
          StringUtils.defaultString(StringUtils.trimToNull(
                  System.getenv().get("ACCOUNT_KEY")),
              ""));

  public static String HOST =
      System.getProperty("ACCOUNT_HOST",
          StringUtils.defaultString(StringUtils.trimToNull(
                  System.getenv().get("ACCOUNT_HOST")),
              ""));

  public void putOrderInAzureCosmoDB(Order order) throws Exception {
    System.out.println("Using Azure Cosmos DB endpoint: " + HOST);

    ArrayList<String> preferredRegions = new ArrayList<>();
    preferredRegions.add("West US");

    //  Create sync client
    client = new CosmosClientBuilder()
        .endpoint(HOST)
        .key(MASTER_KEY)
        .preferredRegions(preferredRegions)
        .userAgentSuffix("CosmosDBJavaQuickstart")
        .consistencyLevel(ConsistencyLevel.EVENTUAL)
        .buildClient();

    createDatabaseIfNotExists();
    createContainerIfNotExists();
    //scaleContainer();

    createOrder(order);
  }

  private void createDatabaseIfNotExists() throws Exception {
    System.out.println("Create database " + databaseName + " if not exists.");

    //  Create database if not exists
    CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists(databaseName);
    database = client.getDatabase(databaseResponse.getProperties().getId());

    System.out.println("Checking database " + database.getId() + " completed!\n");
  }

  private void createContainerIfNotExists() throws Exception {
    System.out.println("Create container " + containerName + " if not exists.");

    //  Create container if not exists
    CosmosContainerProperties containerProperties =
        new CosmosContainerProperties(containerName, "/partitionKey");

    CosmosContainerResponse containerResponse = database.createContainerIfNotExists(containerProperties);
    container = database.getContainer(containerResponse.getProperties().getId());

    System.out.println("Checking container " + container.getId() + " completed!\n");
  }

  private void scaleContainer() throws Exception {
    System.out.println("Scaling container " + containerName + ".");

    try {
      // You can scale the throughput (RU/s) of your container up and down to meet the needs of the workload. Learn more: https://aka.ms/cosmos-request-units
      ThroughputProperties currentThroughput = container.readThroughput().getProperties();
      int newThroughput = currentThroughput.getManualThroughput() + 100;
      container.replaceThroughput(ThroughputProperties.createManualThroughput(newThroughput));
      System.out.println("Scaled container to " + newThroughput + " completed!\n");
    } catch (CosmosException e) {
      if (e.getStatusCode() == 400)
      {
        System.err.println("Cannot read container throuthput.");
        System.err.println(e.getMessage());
      }
      else
      {
        throw e;
      }
    }
  }

  private void createOrder(Order order) {
    //  Using appropriate partition key improves the performance of database operations
    try {
      log.info(order.getId() + " " + order.getEmail());
      order.setPartitionKey(order.getId());
      CosmosItemResponse<Order> item = container.createItem(order, new PartitionKey(order.getId()),
          new CosmosItemRequestOptions());
      log.info("Request charge: {}", item.getRequestCharge());
      log.info("Item duration: {}", item.getDuration());
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void close() {
    client.close();
  }
}
