package com.meli.mercadolense.service.impl;

import com.meli.mercadolense.domain.Item;
import com.meli.mercadolense.service.ItemService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemServiceImpl implements ItemService {

    @Override
    public String getItemImageUrl(String itemId) {
        List<Item> result = new ArrayList<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://api.internal.ml.com/items/"+itemId);

        String itemImageUrl = "";
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            JSONObject strResponse = new JSONObject(EntityUtils.toString(response.getEntity()));
            JSONArray pictures = (JSONArray)strResponse.get("pictures");
            itemImageUrl = (String)pictures.getJSONObject(0).get("url");
        }catch (Exception e){
            System.out.println("Fallo para el carajo: "+e.getMessage());
        }

        return itemImageUrl;
    }
}
