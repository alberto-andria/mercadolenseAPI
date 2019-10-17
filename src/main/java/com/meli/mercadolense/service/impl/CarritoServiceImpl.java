package com.meli.mercadolense.service.impl;

import com.meli.mercadolense.domain.Item;
import com.meli.mercadolense.service.CarritoService;
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
public class CarritoServiceImpl implements CarritoService {

    @Override
    public void addItems(String userId, List<Item> items) {

    }

    @Override
    public void updateItems() {

    }

    @Override
    public List<Item> getItems(String userId){
        List<Item> result = new ArrayList<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://api.internal.ml.com/carts/?client.id=6636335586086312&caller.id="+userId+"&user_id="+userId);
        // add request headers
        //request.addHeader("custom-key", "mkyong");
        //request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            JSONObject strResponse = new JSONObject(EntityUtils.toString(response.getEntity()));
            JSONObject items = (JSONObject)strResponse.get("items");
            JSONArray active = (JSONArray)items.get("active");
            for (int i = 0 ; i < active.length(); i++) {
                JSONObject obj = active.getJSONObject(i);
                String id = (String)obj.get("id");
                result.add(new Item(id));
            }
        }catch (Exception e){
            System.out.println("Fallo para el carajo: "+e.getMessage());
        }
        return result;
    }

    @Override
    public String getURL() {
        return null;
    }
}
