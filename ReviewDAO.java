package com.bakery.dao;

import com.bakery.model.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewDAO extends FileDAO<Review> {

    public ReviewDAO(String dataDir) {
        super(dataDir + "/reviews.txt");
    }

    @Override
    protected Review parseLine(String line) {
        return Review.fromFileRecord(line);
    }

    @Override
    protected void seedData() {
        String now = LocalDateTime.now().format(Review.DT_FMT);
        String[] seeds = {
            "1||101||Amara Silva||6||Custom Birthday Cake||5||Absolutely stunning cake! Everyone loved it. The Spiderman theme was perfect for my son.||true||" + now + "||" + now,
            "2||103||Nimal Fernando||3||Red Velvet Wedding Cake||5||Our wedding cake was beyond expectations. Beautiful presentation and delicious!||true||" + now + "||" + now,
            "3||102||Dinesh Perera||2||Vanilla Cupcakes||4||Very tasty cupcakes. Delivery was on time. Will order again.||true||" + now + "||" + now,
            "4||104||Priya Rajapaksa||4||Butter Cake||3||Good taste but slightly dry. Packaging could be improved.||false||" + now + "||" + now,
            "5||106||Malini Gunasekara||1||Chocolate Fudge Cake||5||Best chocolate cake in Kandy! Rich, moist and absolutely delicious.||true||" + now + "||" + now
        };
        for (String s : seeds) appendRaw(s);
    }

    public List<Review> findByProduct(int productId) {
        return findAll().stream()
                .filter(r -> r.getProductId() == productId)
                .collect(Collectors.toList());
    }

    public List<Review> findApproved() {
        return findAll().stream().filter(Review::isApproved).collect(Collectors.toList());
    }

    public List<Review> findPending() {
        return findAll().stream().filter(r -> !r.isApproved()).collect(Collectors.toList());
    }

    public List<Review> findByCustomer(int customerId) {
        return findAll().stream()
                .filter(r -> r.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public boolean approve(int id) {
        Review r = findById(id);
        if (r == null) return false;
        r.setApproved(true);
        return update(r);
    }

    public double averageRatingForProduct(int productId) {
        return findByProduct(productId).stream()
                .mapToInt(Review::getRating).average().orElse(0.0);
    }

    public List<Review> findAllSorted() {
        List<Review> all = findAll();
        all.sort((a, b) -> b.getId() - a.getId());
        return all;
    }
}
