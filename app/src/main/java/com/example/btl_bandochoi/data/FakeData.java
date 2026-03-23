package com.example.btl_bandochoi.data;

import com.example.btl_bandochoi.model.Product;
import java.util.ArrayList;
import java.util.List;

public class FakeData {

    public static List<Product> getProducts() {
        List<Product> list = new ArrayList<>();

        list.add(new Product(1, "Súng nước đồ chơi", "", 30000, 50, 10, "conhang"));
        list.add(new Product(2, "Đồ chơi mèo", "", 36900, 20, 5, "conhang"));
        list.add(new Product(3, "Xe đồ chơi", "", 50000, 0, 15, "hethang"));

        return list;
    }
}