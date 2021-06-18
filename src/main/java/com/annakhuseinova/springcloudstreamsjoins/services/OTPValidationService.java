package com.annakhuseinova.springcloudstreamsjoins.services;

import com.annakhuseinova.springcloudstreamsjoins.bindings.OTPListenerBinding;
import com.annakhuseinova.springcloudstreamsjoins.model.PaymentConfirmation;
import com.annakhuseinova.springcloudstreamsjoins.model.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

@Service
@Slf4j
@EnableBinding(OTPListenerBinding.class)
@RequiredArgsConstructor
public class OTPValidationService {

    private final RecordBuilder recordBuilder;

    @StreamListener
    public void process(@Input("payment-request-channel") KStream<String, PaymentRequest> request,
                        @Input("payment-confirmation-channel") KStream<String, PaymentConfirmation> confirmation){
        request.foreach((key, value)-> log.info("Request Key = " + key + " Created Time = " + Instant.ofEpochMilli(
                value.getCreatedTime()).atOffset(ZoneOffset.UTC)));
        confirmation.foreach((key, value)-> log.info("Configuration Key = " + key + "Created Time = " +
                Instant.ofEpochMilli(value.getCreatedTime()).atOffset(ZoneOffset.UTC)));

        request.join(confirmation, // the stream that we want to join
                recordBuilder::getTransactionStatus, // Value joiner lambda of 2 arguments (left side and right side records)
                JoinWindows.of(Duration.ofMinutes(5)), // Definition of time constraint. Setting 5 minute window for the
                // join operation. Value joiner lambda will be triggered only if the both records arrive within that time
                // window
                StreamJoined.with(Serdes.String(), // the common serde for the key of 2 records
                        new JsonSerde<>(PaymentRequest.class), new JsonSerde<>(PaymentConfirmation.class)))
        .foreach((key, value)-> log.info("Transaction ID = " + key + " Status = " + value.getStatus())); // the
        // serdes for the 2 records from both streams
    }
}
