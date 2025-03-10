package com.tienda.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.model.Item;
import com.tienda.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws IOException {
        if (itemRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new ClassPathResource("store_data.json").getFile(); // ✅ FIXED
            List<Item> items = objectMapper.readValue(file, new TypeReference<List<Item>>() {});
            itemRepository.saveAll(items);
            System.out.println("✅ Datos importados a MongoDB correctamente.");
        }
    }
}
