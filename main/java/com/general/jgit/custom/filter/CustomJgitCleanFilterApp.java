package com.general.jgit.custom.filter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class CustomJgitCleanFilter.
 */
@SpringBootApplication
public class CustomJgitCleanFilterApp implements CommandLineRunner {

    @Autowired
    private GitService gitService;

    /**
	 * @param args
	 */
	public static void main(String[] args) {
        SpringApplication.run(CustomJgitCleanFilterApp.class, args);
    }

    @Override
    public void run(String... args) {
        // Example usage: clone and push
        // You can edit this logic as needed
        try {
            String localPath = "./cloned-repo"; // Change as needed
            gitService.cloneRepository(localPath);
            // Add logic to modify repo or call push as needed
            gitService.push(localPath);
        } catch (Exception e) {
            System.err.println("Error in run: " + e.getMessage());
            e.printStackTrace();
        }
    }
}