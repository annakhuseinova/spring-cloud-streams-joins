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

        click.join(inventory, // the GlobalKTable we are joining
                (clickKey, clickValue)-> clickKey, // Returns a new join key (which can be different than the current
                // KStream key). This feature is to join GlobalKTable with a foreign key. Here we don't need it
                // and we return the same key
                (clickValue, inventoryValue)-> inventoryValue) // value joiner lambda (we are creating new values here)
                // and for that we use the value both from KStream and GlobalKTable. This will be triggered
                // only if the keys match
                .groupBy((joinedKey, joinedValue)-> joinedValue.getNewsType(), // we are grouping by news type and count
                        Grouped.with(Serdes.String(),
                                new JsonSerde<>(AdInventories.class))) // explicitly setting serdes to avoid serde errors
                .count()
                .toStream().foreach((key, value)-> log.info("Click Key: {}, Value: {}", key, value));
    }
}
