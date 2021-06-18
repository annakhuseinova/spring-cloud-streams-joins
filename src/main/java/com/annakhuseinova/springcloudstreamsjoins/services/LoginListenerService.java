package com.annakhuseinova.springcloudstreamsjoins.services;

import com.annakhuseinova.springcloudstreamsjoins.bindings.UserListenerBinding;
import com.annakhuseinova.springcloudstreamsjoins.model.UserDetails;
import com.annakhuseinova.springcloudstreamsjoins.model.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;

/**
 * KTable to KTable example
 * */
@Service
@Slf4j
@EnableBinding(UserListenerBinding.class)
public class LoginListenerService {

    @StreamListener
    public void process(@Input("user-master-channel") KTable<String, UserDetails> users,
                        @Input("user-login-channel") KTable<String, UserLogin> logins){
        users.toStream().foreach((key, value)-> log.info("User Key: {}, Last Login: {}, Value {}", key,
                Instant.ofEpochMilli(value.getLastLogin()).atOffset(ZoneOffset.UTC), value));
        logins.toStream().foreach((key, value)-> log.info("Login Key: {}, Last Login: {}, Value {}", key,
                Instant.ofEpochMilli(value.getCreatedTime()).atOffset(ZoneOffset.UTC), value));

        logins.join(users, (l, u)-> {
            u.setLastLogin(l.getCreatedTime());
            return u;
        }).toStream().foreach((key, value)-> log.info("Updated Last Login Key: {}, Last Login: {}", key,
                Instant.ofEpochMilli(value.getLastLogin()).atOffset(ZoneOffset.UTC)));
    }
}
