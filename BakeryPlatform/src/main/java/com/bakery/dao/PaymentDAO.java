package com.bakery.dao;

import com.bakery.model.Payment;
import com.bakery.model.Payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentDAO extends FileDAO<Payment> {

    public PaymentDAO(String dataDir) {
        super(dataDir + "/payments.txt");
    }

    @Override
    protected Payment parseLine(String line) {
        return Payment.fromFileRecord(line);
    }

    @Override
    protected void seedData() {
        String now = LocalDateTime.now().format(Payment.DT_FMT);
        String[] seeds = {
            "1||6||105||Kasun Wijesinghe||8500.00||CASH||PAID||TXN-2026-001||" + now + "||" + now,
            "2||1||101||Amara Silva||7000.00||CARD||PENDING||TXN-2026-002||" + now + "||" + now,
            "3||3||103||Nimal Fernando||15000.00||BANK_TRANSFER||PAID||TXN-2026-003||" + now + "||" + now,
            "4||4||104||Priya Rajapaksa||3600.00||CASH||PAID||TXN-2026-004||" + now + "||" + now,
            "5||7||106||Malini Gunasekara||3600.00||CARD||REFUNDED||TXN-2026-005||" + now + "||" + now
        };
        for (String s : seeds) appendRaw(s);
    }

    public List<Payment> findByOrder(int orderId) {
        return findAll().stream()
                .filter(p -> p.getOrderId() == orderId)
                .collect(Collectors.toList());
    }

    public List<Payment> findByStatus(PaymentStatus status) {
        return findAll().stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Payment> findByCustomer(int customerId) {
        return findAll().stream()
                .filter(p -> p.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    public double totalRevenue() {
        return findAll().stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .mapToDouble(Payment::getAmount).sum();
    }

    public boolean updateStatus(int id, PaymentStatus status) {
        Payment p = findById(id);
        if (p == null) return false;
        p.setStatus(status);
        return update(p);
    }

    public List<Payment> findAllSorted() {
        List<Payment> all = findAll();
        all.sort((a, b) -> b.getId() - a.getId());
        return all;
    }

    public String generateRef() {
        return "TXN-" + System.currentTimeMillis();
    }
}
