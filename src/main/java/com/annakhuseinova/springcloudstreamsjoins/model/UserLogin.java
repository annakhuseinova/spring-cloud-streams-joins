package com.annakhuseinova.springcloudstreamsjoins.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserLogin {

    @JsonProperty("LoginID")
    private String loginID;
    @JsonProperty("CreatedTime")
    private Long createdTime;

}