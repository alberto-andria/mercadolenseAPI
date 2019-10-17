package com.meli.mercadolense.service;

import com.meli.mercadolense.domain.Item;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CarritoService {
    void addItems(String userId, List<Item> items);
    void updateItems();
    List<Item> getItems(String userId);
    String getURL();
}
