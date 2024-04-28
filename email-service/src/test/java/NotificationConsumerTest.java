//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.dpd.EmailServiceApplication;
//import org.dpd.consumer.NotificationConsumer;
//import org.dpd.model.OrderMessage;
//import org.dpd.service.NotificationService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.kafka.receiver.KafkaReceiver;
//import reactor.kafka.receiver.ReceiverRecord;
//import reactor.test.StepVerifier;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(classes = EmailServiceApplication.class)
//@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
//@TestPropertySource(properties = {
//        "kafka.bootstrap.servers=${spring.embedded.kafka.brokers}",
//        "topic.name=test-topic",
//        "kafka.group.id=test-group"
//})
//public class NotificationConsumerTest {
//
//    @Autowired
//    private NotificationConsumer notificationConsumer;
//
//    @MockBean
//    private NotificationService notificationService;
//
//    @MockBean
//    private WebClient webClient;
//    @MockBean
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private KafkaReceiver<String, String> kafkaReceiver;
//
//    @Test
//    public void testConsume() {
//        OrderMessage orderMessage = new OrderMessage();
//
//        ReceiverRecord<String, String> record = mock(ReceiverRecord.class);
//        when(record.value()).thenReturn(orderMessage.toString());
//
//        when(kafkaReceiver.receive()).thenReturn(Flux.just(record));
//        when(notificationConsumer.hasStatusCodeChanged(orderMessage.toString())).thenReturn(Mono.just(true));
//        doNothing().when(notificationService).sendEmail(any(OrderMessage.class));
//
//        StepVerifier.create(notificationConsumer.consume())
//                .expectNextCount(1)
//                .expectNext(record)
//                .verifyComplete();
//    }
//}
