package com.annakhuseinova.springcloudstreamsjoins.bindings;

import com.annakhuseinova.springcloudstreamsjoins.model.PaymentConfirmation;
import com.annakhuseinova.springcloudstreamsjoins.model.PaymentRequest;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface OTPListenerBinding {

    @Input("payment-request-channel")
    KStream<String, PaymentRequest> requestInputStream();

    @Input("payment-confirmation-channel")
    KStream<String, PaymentConfirmation> confirmationInputStream();
}
