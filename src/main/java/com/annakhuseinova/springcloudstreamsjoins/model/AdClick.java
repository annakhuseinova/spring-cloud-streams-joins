package com.annakhuseinova.springcloudstreamsjoins.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdClick {

    @JsonProperty("InventoryID")
    private String inventoryID;
}
