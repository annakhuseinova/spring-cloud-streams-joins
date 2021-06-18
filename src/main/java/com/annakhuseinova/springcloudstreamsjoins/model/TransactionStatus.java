package com.annakhuseinova.springcloudstreamsjoins.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionStatus {

    @JsonProperty("TransactionID")
    private String transactionId;
    @JsonProperty("Status")
    private String status;
}
