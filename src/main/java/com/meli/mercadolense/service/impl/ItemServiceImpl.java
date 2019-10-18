package com.meli.mercadolense.service.impl;

import com.meli.mercadolense.domain.Item;
import com.meli.mercadolense.service.ItemService;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemServiceImpl implements ItemService {

    @Override
    public Item getItem(String itemId) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://api.internal.ml.com/items/"+itemId);

        Item item = new Item(itemId);
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            JSONObject strResponse = new JSONObject(EntityUtils.toString(response.getEntity()));
            // setting item URL
            item.setUrl((String)strResponse.get("permalink"));
            item.setCategory((String)strResponse.get("category_id"));
            item.setName((String)strResponse.get("title"));
            item.setPrice((Integer)strResponse.get("price"));
            // setting item picture url
            JSONArray pictures = (JSONArray)strResponse.get("pictures");
            item.setImageUrl((String)pictures.getJSONObject(0).get("url"));
        } catch (Exception e){
            System.out.println("Fallo para el carajo: "+e.getMessage());
        }

        return item;
    }

    @Override
    public List<Item> getSuggestions(String itemId, String categoryId) {
        List<Item> result = new ArrayList<>();

        String suggestionsUrl = (new StringBuilder())
                .append("https://www.mercadolibre.com.ar/recommendations/")
                .append("?client=vip-v2p")
                .append("&limit=20")
                .append("&site_id=MLA")
                .append("&category_id="+categoryId)
                .append("&item_id="+itemId)
                .append("&is_bot=false")
                .toString();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        System.out.println(suggestionsUrl);
        HttpGet request = new HttpGet(suggestionsUrl);
        request.setHeader("Cookie", "_d2id=aea1c5a1-1480-4391-9c2c-42939308d600-n");

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            JSONObject strResponse = new JSONObject(EntityUtils.toString(response.getEntity()));

            System.out.println(strResponse);

            JSONArray recommendations = (JSONArray)strResponse.getJSONObject("recommendation_info").get("recommendations");
            for(int i=0; i<recommendations.length(); i++){
                // setting item URL
                Item item = new Item((String)recommendations.getJSONObject(i).get("id"));
                item.setUrl((String)recommendations.getJSONObject(i).get("permalink"));
                item.setCategory((String)recommendations.getJSONObject(i).get("category_id"));
                item.setName((String)recommendations.getJSONObject(i).get("title"));

                // parsing price
                String price = (String)recommendations.getJSONObject(0).get("price");
                item.setPrice(Integer.parseInt(price
                        .replace("$","")
                        .replace(".","")
                        .strip()));

                // setting item picture url
                JSONArray pictures = (JSONArray)recommendations.getJSONObject(i).get("pictures");
                item.setImageUrl((String)pictures.getJSONObject(0).get("url"));

                // adding item to list
                result.add(item);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }
}
