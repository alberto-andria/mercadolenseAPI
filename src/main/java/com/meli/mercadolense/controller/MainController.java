package com.meli.mercadolense.controller;

import com.meli.mercadolense.domain.Item;
import com.meli.mercadolense.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    CarritoService carritoService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/test")
    public void test() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("MLA784103074"));
        carritoService.addItems("9852568",items);
    }
}