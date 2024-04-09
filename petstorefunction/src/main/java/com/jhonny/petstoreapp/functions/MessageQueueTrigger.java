package com.jhonny.petstoreapp.functions;

import com.jhonny.petstoreapp.functions.service.BlobStorage;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusQueueTrigger;

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
        BlobStorage.sendMessageToBlobStorage(message, context);
    }
}
