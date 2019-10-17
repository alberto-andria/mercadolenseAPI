package com.meli.mercadolense.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.mercadolense.domain.Item;
import com.meli.mercadolense.dto.ItemDTO;
import com.meli.mercadolense.service.CarritoService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private CarritoService carritoService;
    private final String USER_ID = "9852568";

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

    @PostMapping("/update")
    public void update() {
        carritoService.updateItems("9852568");
    }

    @PostMapping("/item")
    public String postItem(@RequestBody ItemDTO item){
        //Servicio Nico Albani
        //Vuelven los posibles items
        return "";
    }

    @PostMapping("/generateCart")
    public String generateCart(@RequestBody List<String> ids){
        List<Item> items = new ArrayList<>();

        for (String id:ids) {
           String[] aux = id.split("-");
           items.add(new Item(aux[0]+aux[1]));
        }

        carritoService.addItems(USER_ID, items);
        return "OKEY";
    }
}