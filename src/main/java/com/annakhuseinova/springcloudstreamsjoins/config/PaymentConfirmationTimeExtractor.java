package com.annakhuseinova.springcloudstreamsjoins.config;

import com.annakhuseinova.springcloudstreamsjoins.model.PaymentConfirmation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PaymentConfirmationTimeExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long previousRecordTime) {
        PaymentConfirmation confirmation = (PaymentConfirmation) consumerRecord.value();
        return ((confirmation.getCreatedTime() > 0) ? confirmation.getCreatedTime(): previousRecordTime);
    }

    @Bean
    public TimestampExtractor confirmationTImeExtractor(){
        return new PaymentConfirmationTimeExtractor();
    }
}
