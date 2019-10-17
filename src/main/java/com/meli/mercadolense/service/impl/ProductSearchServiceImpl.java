package com.meli.mercadolense.service.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.meli.mercadolense.service.ProductSearchService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductSearchServiceImpl implements ProductSearchService {

private static final String PROJECT_ID = "productsearchtest-256215";
private static final String COMPUTE_REGION = "us-east1";
private static final String PRODUCT_SET_ID = "mercadolibre";
private static final String PRODUCT_CATEGORY_HG = "homegoods";
private static final String PRODUCT_CATEGORY_HG_V2 = "homegoods-v2";
private static final String PRODUCT_CATEGORY_T = "toys";
private static final String PRODUCT_CATEGORY_T_V2 = "toys-v2";
private static final String PRODUCT_CATEGORY_A = "apparel";
private static final String PRODUCT_CATEGORY_A_V2 = "apparel-v2";

    /**
     * Search similar products to image in local file.
     *
     * @param filePath - Local file path of the image to be searched
     *     color = blue) AND style = kids It will search on all products with the following labels:
     *     color:red AND style:kids color:blue AND style:kids
     * @throws IOException - on I/O errors.
     */
    public void getSimilarProductsFile(
            String filePath)
            throws IOException {
        try (ImageAnnotatorClient queryImageClient = getImageAnnotatorClient()) {

            // Get the full path of the product set.
            String productSetPath =
                    ProductSearchClient.formatProductSetName(PROJECT_ID, COMPUTE_REGION, PRODUCT_SET_ID);

            // Read the image as a stream of bytes.
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            byte[] content = inputStream.readAllBytes();

            // Create annotate image request along with product search feature.
            Feature featuresElement = Feature.newBuilder().setType(Feature.Type.PRODUCT_SEARCH).build();
            // The input image can be a HTTPS link or Raw image bytes.
            // Example:
            // To use HTTP link replace with below code
            //  ImageSource source = ImageSource.newBuilder().setImageUri(imageUri).build();
            //  Image image = Image.newBuilder().setSource(source).build();
            Image image = Image.newBuilder().setContent(ByteString.copyFrom(content)).build();
            ImageContext imageContext =
                    ImageContext.newBuilder()
                            .setProductSearchParams(
                                    ProductSearchParams.newBuilder()
                                            .setProductSet(productSetPath)
                                            .addProductCategories(PRODUCT_CATEGORY_HG)
                                            .addProductCategories(PRODUCT_CATEGORY_HG_V2)
                                            .addProductCategories(PRODUCT_CATEGORY_A)
                                            .addProductCategories(PRODUCT_CATEGORY_A_V2)
                                            .addProductCategories(PRODUCT_CATEGORY_T)
                                            .addProductCategories(PRODUCT_CATEGORY_T_V2))
                            .build();

            AnnotateImageRequest annotateImageRequest =
                    AnnotateImageRequest.newBuilder()
                            .addFeatures(featuresElement)
                            .setImage(image)
                            .setImageContext(imageContext)
                            .build();
            List<AnnotateImageRequest> requests = Arrays.asList(annotateImageRequest);

            // Search products similar to the image.
            BatchAnnotateImagesResponse response = queryImageClient.batchAnnotateImages(requests);

            List<ProductSearchResults.Result> similarProducts =
                    response.getResponses(0).getProductSearchResults().getResultsList();
            System.out.println("Similar Products: ");
            for (ProductSearchResults.Result product : similarProducts) {
                System.out.println(String.format("\nProduct name: %s", product.getProduct().getName()));
                System.out.println(
                        String.format("Product display name: %s", product.getProduct().getDisplayName()));
                System.out.println(
                        String.format("Product description: %s", product.getProduct().getDescription()));
                System.out.println(String.format("Score(Confidence): %s", product.getScore()));
                System.out.println(String.format("Image name: %s", product.getImage()));
            }
        }
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
