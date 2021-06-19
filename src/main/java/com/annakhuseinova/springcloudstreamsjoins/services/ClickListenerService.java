package com.annakhuseinova.springcloudstreamsjoins.services;

import com.annakhuseinova.springcloudstreamsjoins.bindings.ClickListenerBinding;
import com.annakhuseinova.springcloudstreamsjoins.model.AdClick;
import com.annakhuseinova.springcloudstreamsjoins.model.AdInventories;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableBinding(ClickListenerBinding.class)
public class ClickListenerService {

    @StreamListener
    private void process(@Input("inventories-channel") GlobalKTable<String, AdInventories> inventory,
                         @Input("clicks-channel") KStream<String, AdClick> click){
        click.foreach((key, value)-> log.info("Click Key: {}, Value: {}", key, value));

        click.join(inventory,
                (clickKey, clickValue)-> clickKey,
                (clickValue, inventoryValue)-> inventoryValue)
                .groupBy((joinedKey, joinedValue)-> joinedValue.getNewsType(),
                        Grouped.with(Serdes.String(),
                                new JsonSerde<>(AdInventories.class)))
                .count()
                .toStream().foreach((key, value)-> log.info("Click Key: {}, Value: {}", key, value));
    }
}
