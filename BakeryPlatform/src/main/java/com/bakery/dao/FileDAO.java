package com.bakery.dao;

import com.bakery.model.BaseEntity;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FileDAO — OOP: ABSTRACTION (abstract class), INHERITANCE (extended by all DAOs)
 * Handles all file read/write operations using txt files for persistence.
 * Thread-safe ID generation via AtomicInteger.
 */
public abstract class FileDAO<T extends BaseEntity> implements GenericDAO<T, Integer> {

    protected final String filePath;
    private final AtomicInteger idCounter = new AtomicInteger(0);

    protected FileDAO(String filePath) {
        this.filePath = filePath;
        initFile();
        syncIdCounter();
    }

    // ---- Abstract methods — subclasses define entity-specific parsing ----
    protected abstract T parseLine(String line);
    protected abstract void seedData();

    // ---- File initialisation ----
    private void initFile() {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
                seedData();
            }
        } catch (IOException e) {
            System.err.println("[FileDAO] Cannot init file " + filePath + ": " + e.getMessage());
        }
    }

    private void syncIdCounter() {
        List<T> all = findAll();
        int max = all.stream().mapToInt(BaseEntity::getId).max().orElse(0);
        idCounter.set(max);
    }

    protected int nextId() { return idCounter.incrementAndGet(); }

    // ---- CRUD Operations ----

    @Override
    public boolean save(T entity) {
        if (entity.getId() == 0) entity.setId(nextId());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(entity.toFileRecord());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("[FileDAO.save] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(T updated) {
        List<T> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == updated.getId()) {
                updated.setCreatedAt(all.get(i).getCreatedAt());
                updated.markUpdated();
                all.set(i, updated);
                found = true;
                break;
            }
        }
        return found && writeAll(all);
    }

    @Override
    public boolean delete(Integer id) {
        List<T> all = findAll();
        boolean removed = all.removeIf(e -> e.getId() == id);
        return removed && writeAll(all);
    }

    @Override
    public T findById(Integer id) {
        return findAll().stream()
                .filter(e -> e.getId() == id)
                .findFirst().orElse(null);
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    T entity = parseLine(line);
                    if (entity != null) list.add(entity);
                }
            }
        } catch (IOException e) {
            System.err.println("[FileDAO.findAll] " + e.getMessage());
        }
        return list;
    }

    protected boolean writeAll(List<T> entities) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (T e : entities) {
                bw.write(e.toFileRecord());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[FileDAO.writeAll] " + e.getMessage());
            return false;
        }
    }

    protected void appendRaw(String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("[FileDAO.appendRaw] " + e.getMessage());
        }
    }

    public int count() { return findAll().size(); }

    public String getFilePath() { return filePath; }
}
