package com.bakery.dao;

import com.bakery.model.Order;
import com.bakery.model.Order.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderDAO — OOP: INHERITANCE (extends FileDAO), POLYMORPHISM (overrides parseLine/seedData)
 * Persists orders to orders.txt
 */
public class OrderDAO extends FileDAO<Order> {

    public OrderDAO(String dataDir) {
        super(dataDir + "/orders.txt");
    }

    @Override
    protected Order parseLine(String line) {
        return Order.fromFileRecord(line);
    }

    @Override
    protected void seedData() {
        String now = LocalDateTime.now().format(Order.DT_FMT);
        String[] seeds = {
            "1||101||Amara Silva||Chocolate Fudge Cake||2||3500.00||7000.00||2026-05-15||CONFIRMED||No nuts please||" + now + "||" + now,
            "2||102||Dinesh Perera||Vanilla Cupcakes||12||250.00||3000.00||2026-05-10||PENDING||Extra frosting||" + now + "||" + now,
            "3||103||Nimal Fernando||Red Velvet Wedding Cake||1||15000.00||15000.00||2026-06-01||CONFIRMED||3 tiers with fondant roses||" + now + "||" + now,
            "4||104||Priya Rajapaksa||Butter Cake||3||1200.00||3600.00||2026-05-08||READY||||" + now + "||" + now,
            "5||101||Amara Silva||Cheesecake Slice Box||6||450.00||2700.00||2026-05-20||PENDING||Mixed berry topping||" + now + "||" + now,
            "6||105||Kasun Wijesinghe||Birthday Cake - Spiderman||1||8500.00||8500.00||2026-05-12||COMPLETED||Spiderman theme, age 7||" + now + "||" + now,
            "7||106||Malini Gunasekara||Brownie Box||2||1800.00||3600.00||2026-05-07||CANCELLED||||" + now + "||" + now,
            "8||107||Sanduni Jayawardena||Custom Anniversary Cake||1||12000.00||12000.00||2026-05-25||IN_PROGRESS||Gold drip, 2 tiers, photo print||" + now + "||" + now
        };
        for (String s : seeds) appendRaw(s);
    }

    // ---- Business Methods ----

    public List<Order> findByStatus(Status status) {
        return findAll().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Order> findByCustomerName(String query) {
        String q = query.toLowerCase();
        return findAll().stream()
                .filter(o -> o.getCustomerName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Order> findByCustomerId(int customerId) {
        return findAll().stream()
                .filter(o -> o.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public List<Order> search(String query) {
        try {
            int id = Integer.parseInt(query.trim());
            Order found = findById(id);
            return found != null ? List.of(found) : List.of();
        } catch (NumberFormatException e) {
            return findByCustomerName(query);
        }
    }

    public List<Order> findAllSorted() {
        List<Order> all = findAll();
        all.sort((a, b) -> b.getId() - a.getId());
        return all;
    }

    public long countByStatus(Status status) {
        return findAll().stream().filter(o -> o.getStatus() == status).count();
    }

    public double totalRevenue() {
        return findAll().stream()
                .filter(o -> o.getStatus() == Status.COMPLETED)
                .mapToDouble(Order::getTotalAmount).sum();
    }

    public boolean updateStatus(int orderId, Status newStatus) {
        Order o = findById(orderId);
        if (o == null) return false;
        o.setStatus(newStatus);
        return update(o);
    }
}
