package com.general.jgit.custom.filter;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GitService {

    @Value("${git.repo.url}")
    private String repoUrl;

    @Value("${git.repo.username}")
    private String username;

    @Value("${git.repo.password}")
    private String password;

    public void cloneRepository(String localPath) throws Exception {
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
        CloneCommand clone = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(new File(localPath))
                .setCredentialsProvider(cp);
        try (Git git = clone.call()) {
        	CustomJgitCleanFilter.register();
            System.out.println("Repository cloned to: " + localPath);
            // Add .gitattributes file with required content
            File gitattributes = new File(localPath, ".gitattributes");
            try (java.io.FileWriter writer = new java.io.FileWriter(gitattributes)) {
                writer.write("*.zip filter=purge -text\n");
            }
            // Stage and commit the .gitattributes file
            git.add().addFilepattern(".gitattributes").call();
            git.commit().setMessage("Add .gitattributes for zip filter").call();
            StoredConfig config = git.getRepository().getConfig();
			String filterName = "purge";
        	config.setBoolean("filter", filterName, "useJGitBuiltin", false);
			config.setString("filter", filterName, "smudge", "jgit://filter/purge/smudge");
			config.setString("filter", filterName, "clean", "jgit://filter/purge/clean");

			config.save();
        }
    }

    public void push(String localPath) throws Exception {
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
        try (Git git = Git.open(new File(localPath))) {
            PushCommand pushCommand = git.push().setCredentialsProvider(cp);
            pushCommand.call();
            System.out.println("Push completed from: " + localPath);
        }
    }
}