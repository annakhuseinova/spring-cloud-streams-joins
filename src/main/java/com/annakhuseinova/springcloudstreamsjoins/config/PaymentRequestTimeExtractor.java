package com.annakhuseinova.springcloudstreamsjoins.config;

import com.annakhuseinova.springcloudstreamsjoins.model.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PaymentRequestTimeExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long previousRecordTime) {
        PaymentRequest request = (PaymentRequest) consumerRecord.value();
        return ((request.getCreatedTime() > 0) ? request.getCreatedTime(): previousRecordTime);
    }

    @Bean
    public TimestampExtractor requestTimeExtractor(){
        return new PaymentRequestTimeExtractor();
    }
}
