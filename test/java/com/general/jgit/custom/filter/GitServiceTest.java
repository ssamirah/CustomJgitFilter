package com.general.jgit.custom.filter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GitServiceTest {

    @Autowired
    private GitService gitService;

    @Test
    void testCloneAndPush(@TempDir Path tempDir) throws Exception {
        String localPath = tempDir.resolve("cloned-repo").toString();
        java.nio.file.Path localPathDir = java.nio.file.Paths.get(localPath);
        try {
            // Ensure the directory does not exist before cloning
            if (java.nio.file.Files.exists(localPathDir)) {
                java.nio.file.Files.walk(localPathDir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(java.nio.file.Path::toFile)
                    .forEach(java.io.File::delete);
            }
            // Clone repo
            assertDoesNotThrow(() -> gitService.cloneRepository(localPath));
            // Copy a zip file from test/resources to the repo
            String dummyZipFileName = "unamed_system.zip"; // Edit this as needed
            java.nio.file.Path sourceZip = java.nio.file.Paths.get("src", "test", "resources", dummyZipFileName);
            java.nio.file.Path targetZip = java.nio.file.Paths.get(localPath, dummyZipFileName);
            java.nio.file.Files.copy(sourceZip, targetZip, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            // Stage and push the zip file
            assertDoesNotThrow(() -> {
                org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(new File(localPath));
                git.add().addFilepattern(dummyZipFileName).call();
                git.commit().setMessage("Add " + dummyZipFileName).call();
                git.close();
                gitService.push(localPath);
            });
        } finally {
            // Clean up the temp directory recursively
            if (java.nio.file.Files.exists(localPathDir)) {
                java.nio.file.Files.walk(localPathDir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(java.nio.file.Path::toFile)
                    .forEach(java.io.File::delete);
            }
        }
    }
}