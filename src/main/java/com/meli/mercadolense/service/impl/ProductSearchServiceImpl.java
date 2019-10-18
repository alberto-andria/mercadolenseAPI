package com.meli.mercadolense.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.meli.mercadolense.domain.Item;
import com.meli.mercadolense.dto.ProductSearchDTO;
import com.meli.mercadolense.service.ItemService;
import com.meli.mercadolense.service.ProductSearchService;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.CustomVisionPredictionClient;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.CustomVisionPredictionManager;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.ImagePrediction;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.Prediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class ProductSearchServiceImpl implements ProductSearchService {

@Autowired
private ItemService itemService;

private static final String PROJECT_ID = "productsearchtest-256215";
private static final String COMPUTE_REGION = "us-east1";
private static final String PRODUCT_SET_ID = "mercadolibre";
private static final String PRODUCT_CATEGORY_HG = "homegoods";
private static final String PRODUCT_CATEGORY_HG_V2 = "homegoods-v2";
private static final String PRODUCT_CATEGORY_T = "toys";
private static final String PRODUCT_CATEGORY_T_V2 = "toys-v2";
private static final String PRODUCT_CATEGORY_A = "apparel";
private static final String PRODUCT_CATEGORY_A_V2 = "apparel-v2";

public static Map<String, String> mockedItems;
static {
    mockedItems = new HashMap<>();
    mockedItems.put("Silla-Acapulco", "MLA782874474");
    mockedItems.put("auricular", "MLA735675417");
    mockedItems.put("celular", "MLA13996822");
    mockedItems.put("lapicera roller", "MLA701685843");
    mockedItems.put("zapatilla", "MLA632537677");
}

    /**
     * Search similar products to image in local file.
     *
     * @param picture - picture
     * @throws IOException - on I/O errors.
     */
    public String getSimilarProductsFile(
            String picture)
            throws IOException {

        UUID projectId = UUID.fromString("43e399fa-ff4b-420d-8180-efb4a7263116");
        String modelName = "MercadoLensV1";
        String apiKey = "d569441adeb248ad8f3fd8f245c261f9";
        String endpoint = "https://westus2.api.cognitive.microsoft.com";

        CustomVisionPredictionClient predictClient = CustomVisionPredictionManager
                .authenticate(endpoint+"/customvision/v3.0/prediction/", apiKey).withEndpoint(endpoint);

        // Read the image as a stream of bytes.
        byte[] content = Base64.getDecoder().decode(picture);

        ImagePrediction results = predictClient.predictions().classifyImage()
                .withProjectId(projectId)
                .withPublishedName(modelName)
                .withImageData(content)
                .execute();

            // creating results list
            List<ProductSearchDTO> dto = new ArrayList<>();

            // getting first result
            for (Prediction prediction : results.predictions()) {
                if (prediction.probability() > 0.1){
                    String tagName = prediction.tagName().replaceFirst("-","");
                    String itemId = tagName.substring(0, tagName.indexOf("-"));

                    // getting first item data
                    Item item = itemService.getItem(itemId);

                    // adding first result
                    dto.add(new ProductSearchDTO(
                            itemId,
                            prediction.probability(),
                            item.getName(),
                            item.getUrl(),
                            item.getImageUrl(),
                            item.getPrice()));
                }
            }

//            // getting suggestions
//            List<Item> items = itemService.getSuggestions(item.getId(), item.getCategory());
//
//            // adding suggestions
//            for(Item suggestion :items){
//                dto.add(new ProductSearchDTO(
//                        suggestion.getId(),
//                        firstPrediction.probability(),
//                        suggestion.getId(),
//                        suggestion.getUrl(),
//                        suggestion.getImageUrl(),
//                        suggestion.getPrice()
//                ));
//            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(dto);
    }

    private ImageAnnotatorClient getImageAnnotatorClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(getClass().getClassLoader().getResourceAsStream("product_search_credentials.json"));
        //credentials.refreshIfExpired();

        ImageAnnotatorSettings imageAnnotatorSettings =
                ImageAnnotatorSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        ImageAnnotatorClient imageAnnotatorClient =
                ImageAnnotatorClient.create(imageAnnotatorSettings);
        return imageAnnotatorClient;
    }
}
