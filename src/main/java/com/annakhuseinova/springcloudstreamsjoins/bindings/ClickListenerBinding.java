package com.annakhuseinova.springcloudstreamsjoins.bindings;

import com.annakhuseinova.springcloudstreamsjoins.model.AdClick;
import com.annakhuseinova.springcloudstreamsjoins.model.AdInventories;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface ClickListenerBinding {

    @Input("inventories-channel")
    GlobalKTable<String, AdInventories> inventoryInputStream();

    @Input("clicks-channel")
    KStream<String, AdClick> clickInputStream();
}
