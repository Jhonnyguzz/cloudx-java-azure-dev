package io.swagger.configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageBus {

  @Value("${azure.messagebus.connection-string}")
  private String connectionString;

  @Value("${azure.messagebus.queue-name}")
  private String queueName;

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
    if(senderClient != null) {
      senderClient.close();
    }
  }

}
