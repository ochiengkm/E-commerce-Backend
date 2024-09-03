package com.microservices.inventory_service;

import com.microservices.inventory_service.model.Inventory;
import com.microservices.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("soap");
			inventory.setQuantity(40);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("phone");
			inventory1.setQuantity(0);
			Inventory inventory2 = new Inventory();

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};

	}

}
