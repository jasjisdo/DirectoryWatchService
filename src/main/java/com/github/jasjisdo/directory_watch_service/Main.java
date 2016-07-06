package com.github.jasjisdo.directory_watch_service;

import com.github.jasjisdo.directory_watch_service.service.DirectoryWatchService;
import com.github.jasjisdo.directory_watch_service.service.SimpleDirectoryWatchService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        // Get the default temp dir by java system property. 
        String tempDir = System.getProperty("java.io.tmpdir");
        
        // Register service to watch file updates in temp dir.
        DirectoryWatchService watchService = null;
        try {
            watchService = new SimpleDirectoryWatchService();
            watchService.register(
                    new DirectoryWatchService.OnFileChangeListener() {
                        @Override
                        public void onFileCreate(String fileLocation) {
                            // File created
                            log.info("new file created: " + fileLocation);
                        }

                        @Override
                        public void onFileModify(String fileLocation) {
                            // File modified
                            log.info("file modified: " + fileLocation);
                        }

                        @Override
                        public void onFileDelete(String fileLocation) {
                            // File deleted
                            log.info("file deleted: " + fileLocation);
                        }
                    }
//                    <directory>, // Directory to watch
//                    <file-glob-pattern-1>, // E.g. "*.log"
//                    <file-glob-pattern-2>, // E.g. "input-?.txt"
//                    <file-glob-pattern-3>, // E.g. "config.ini"
//                    ... // As many patterns as you like
                    , tempDir 
                    , "*.txt" // Text files
                    , "*.csv" // CSV files
            );

            // Launch service
            watchService.start();

        } catch (IOException e) {
            log.error("Unable to register file change listener for " + tempDir);
        }

        // Wait 2 sec after watch service start...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        // Create a txt file
        File tempFile = new File(tempDir + File.separator + "data.txt");
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wait 2 sec after file creation...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        // write into file to trigger 3 file modification events (touch/open file, write file and save file).
        try {
            FileUtils.write(tempFile, "Hello", "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wait 2 sec after file modification...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        // Delete file
        tempFile.delete();

        // Wait 2 sec after file deletion...
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }

        // Stop service (responsive)
        watchService.stop();
    }
}
