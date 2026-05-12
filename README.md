This application is to demonstrate custom jgit filter.
Update repo url, username and PAT in application.properties.
Replace the 'unamed_system.zip' in sr/test/resources with a zip file with the same name but size greater than 65 mb to reproduce the issue https://github.com/eclipse-jgit/jgit/issues/261. 
Run the GitServiceTest.java class .
