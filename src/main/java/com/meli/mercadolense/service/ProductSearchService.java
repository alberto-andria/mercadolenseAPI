package com.meli.mercadolense.service;

import java.io.IOException;

public interface ProductSearchService {
    String getSimilarProductsFile(String picture) throws IOException;
}
