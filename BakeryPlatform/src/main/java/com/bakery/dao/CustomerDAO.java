package com.bakery.dao;

import com.bakery.model.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDAO extends FileDAO<Customer> {

    public CustomerDAO(String dataDir) {
        super(dataDir + "/customers.txt");
    }

    @Override
    protected Customer parseLine(String line) {
        return Customer.fromFileRecord(line);
    }

    @Override
    protected void seedData() {
        String now = LocalDateTime.now().format(Customer.DT_FMT);
        String[] seeds = {
            "101||Amara Silva||amara@email.com||0771234567||No. 5, Kandy Road, Peradeniya||PREMIUM||" + now + "||" + now,
            "102||Dinesh Perera||dinesh@email.com||0712345678||No. 12, Galle Road, Colombo 3||REGULAR||" + now + "||" + now,
            "103||Nimal Fernando||nimal@email.com||0763456789||No. 8, High Level Road, Nugegoda||PREMIUM||" + now + "||" + now,
            "104||Priya Rajapaksa||priya@email.com||0754567890||No. 3, Hospital Road, Kurunegala||REGULAR||" + now + "||" + now,
            "105||Kasun Wijesinghe||kasun@email.com||0745678901||No. 21, Temple Road, Gampaha||REGULAR||" + now + "||" + now,
            "106||Malini Gunasekara||malini@email.com||0736789012||No. 7, Lake Drive, Kandy||PREMIUM||" + now + "||" + now,
            "107||Sanduni Jayawardena||sanduni@email.com||0727890123||No. 14, Main Street, Matara||REGULAR||" + now + "||" + now
        };
        for (String s : seeds) appendRaw(s);
    }

    public List<Customer> search(String query) {
        String q = query.toLowerCase();
        return findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(q)
                          || c.getPhone().contains(q)
                          || c.getEmail().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Customer> findByMembership(String type) {
        return findAll().stream()
                .filter(c -> c.getMembershipType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Customer> findAllSorted() {
        List<Customer> all = findAll();
        all.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return all;
    }
}
