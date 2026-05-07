package com.bakery.dao;

import com.bakery.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDAO extends FileDAO<Product> {

    public ProductDAO(String dataDir) {
        super(dataDir + "/products.txt");
    }

    @Override
    protected Product parseLine(String line) {
        return Product.fromFileRecord(line);
    }

    @Override
    protected void seedData() {
        String now = LocalDateTime.now().format(Product.DT_FMT);
        String[] seeds = {
            "1||Chocolate Fudge Cake||CAKE||Rich chocolate layered cake with fudge frosting||3500.00||true||🎂||" + now + "||" + now,
            "2||Vanilla Cupcakes (per piece)||CUPCAKE||Soft vanilla cupcake with buttercream swirl||250.00||true||🧁||" + now + "||" + now,
            "3||Red Velvet Wedding Cake||CAKE||Elegant red velvet with cream cheese frosting||15000.00||true||🎂||" + now + "||" + now,
            "4||Butter Cake||CAKE||Classic Sri Lankan butter cake||1200.00||true||🍰||" + now + "||" + now,
            "5||Cheesecake Slice Box||PASTRY||Assorted cheesecake slices in a gift box||450.00||true||🥐||" + now + "||" + now,
            "6||Custom Birthday Cake||CUSTOM||Fully customisable birthday cake (theme, size, colour)||8500.00||true||⭐||" + now + "||" + now,
            "7||Brownie Box (6 pcs)||PASTRY||Fudgy dark chocolate brownies||1800.00||true||🥐||" + now + "||" + now,
            "8||Custom Anniversary Cake||CUSTOM||2-tier custom anniversary cake with photo print||12000.00||true||⭐||" + now + "||" + now,
            "9||Sourdough Loaf||BREAD||Hand-crafted sourdough bread||900.00||true||🍞||" + now + "||" + now,
            "10||Croissant Box (4 pcs)||PASTRY||Buttery fresh croissants||1100.00||false||🥐||" + now + "||" + now
        };
        for (String s : seeds) appendRaw(s);
    }

    public List<Product> findAvailable() {
        return findAll().stream().filter(Product::isAvailable).collect(Collectors.toList());
    }

    public List<Product> findByCategory(String category) {
        return findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Product> search(String query) {
        String q = query.toLowerCase();
        return findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(q)
                          || p.getDescription().toLowerCase().contains(q)
                          || p.getCategory().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Product> findAllSorted() {
        List<Product> all = findAll();
        all.sort((a, b) -> a.getCategory().compareTo(b.getCategory()));
        return all;
    }
}
