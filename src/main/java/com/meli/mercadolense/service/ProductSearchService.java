package com.meli.mercadolense.service;

import java.io.IOException;

public interface ProductSearchService {
    void getSimilarProductsFile(String filePath) throws IOException;
}
