package com.annakhuseinova.springcloudstreamsjoins.services;

import com.annakhuseinova.springcloudstreamsjoins.model.PaymentConfirmation;
import com.annakhuseinova.springcloudstreamsjoins.model.PaymentRequest;
import com.annakhuseinova.springcloudstreamsjoins.model.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecordBuilder {

    public TransactionStatus getTransactionStatus(PaymentRequest request, PaymentConfirmation confirmation){
        String status = "Failure";
        if (request.getOTP().equalsIgnoreCase(confirmation.getOTP())){
            status = "Success";
        }

        TransactionStatus transactionStatus = new TransactionStatus();
        transactionStatus.setTransactionId(request.getTransactionID());
        transactionStatus.setStatus(status);
        return transactionStatus;
    }
}
