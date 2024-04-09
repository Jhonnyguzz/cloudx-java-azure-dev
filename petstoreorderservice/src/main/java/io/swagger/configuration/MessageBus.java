package io.swagger.configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.stereotype.Component;

@Component
public class MessageBus {

  private final String connectionString = "";
  private final String queueName = "";
  private ServiceBusSenderClient senderClient;

  public void putOrderInServiceBus(String orderJson) {
    senderClient = new ServiceBusClientBuilder()
        .connectionString(connectionString)
        .sender()
        .queueName(queueName)
        .buildClient();

    ServiceBusMessage message = new ServiceBusMessage(orderJson);
    senderClient.sendMessage(message);
  }

  public void close() {
    senderClient.close();
  }

}
