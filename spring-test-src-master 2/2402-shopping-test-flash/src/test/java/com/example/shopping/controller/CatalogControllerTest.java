package com.example.shopping.controller;

import com.example.shopping.entity.Product;
import com.example.shopping.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CatalogController.class)
class CatalogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CatalogService catalogService;

    @Test
    void test_displayList() throws Exception {
        List<Product> products = new ArrayList<Product>();
        Product product = new Product();
        product.setName("商品01");
        products.add(product);
        product = new Product();
        product.setName("商品02");
        products.add(product);

        doReturn(products).when(catalogService).findAll();

        mockMvc.perform(
                get("/catalog/display-list")
            )
            .andExpect(content().string(containsString("商品01")))
            .andExpect(content().string(containsString("商品02")))
        ;

    }

    @Test
    void test_displayDetails() throws Exception {
        Product product = new Product();
        product.setName("商品01");
        doReturn(product).when(catalogService).findById("p01");

        mockMvc.perform(
                get("/catalog/display-details")
                    .param("productId", "p01")
            )
            .andExpect(content().string(containsString("商品01")))
        ;
    }

}