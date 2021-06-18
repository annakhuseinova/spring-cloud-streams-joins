package com.annakhuseinova.springcloudstreamsjoins.bindings;

import com.annakhuseinova.springcloudstreamsjoins.model.UserDetails;
import com.annakhuseinova.springcloudstreamsjoins.model.UserLogin;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;

public interface UserListenerBinding {

    @Input("user-master-channel")
    KTable<String, UserDetails> userInputStream();

    @Input("user-login-channel")
    KTable<String, UserLogin> loginInputStream();
}

