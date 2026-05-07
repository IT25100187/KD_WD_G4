package com.bakery.dao;

import com.bakery.model.Admin;
import com.bakery.util.PasswordUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDAO extends FileDAO<Admin> {

    public AdminDAO(String dataDir) {
        super(dataDir + "/admins.txt");
    }

    @Override
    protected Admin parseLine(String line) {
        return Admin.fromFileRecord(line);
    }

    @Override
    protected void seedData() {
        String now  = LocalDateTime.now().format(Admin.DT_FMT);
        String hash = PasswordUtil.hash("admin123");
        String hash2 = PasswordUtil.hash("manager1");
        String[] seeds = {
            "1||admin||" + hash + "||System Administrator||admin@sweetcrumbs.lk||SUPER_ADMIN||" + now + "||" + now,
            "2||manager||" + hash2 + "||Shop Manager||manager@sweetcrumbs.lk||ADMIN||" + now + "||" + now
        };
        for (String s : seeds) appendRaw(s);
    }

    public Admin findByUsername(String username) {
        return findAll().stream()
                .filter(a -> a.getUsername().equalsIgnoreCase(username))
                .findFirst().orElse(null);
    }

    public Admin authenticate(String username, String password) {
        Admin admin = findByUsername(username);
        if (admin != null && admin.verifyPassword(password)) return admin;
        return null;
    }

    public List<Admin> search(String query) {
        String q = query.toLowerCase();
        return findAll().stream()
                .filter(a -> a.getUsername().toLowerCase().contains(q)
                          || a.getFullName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Admin> findAllSorted() {
        List<Admin> all = findAll();
        all.sort((a, b) -> a.getUsername().compareToIgnoreCase(b.getUsername()));
        return all;
    }
}
