package com.meli.mercadolense.service;

import com.meli.mercadolense.domain.Item;

import java.util.List;

public interface ItemService {
    Item getItem(String itemId);
    List<Item> getSuggestions(String itemId, String categoryId);
}
