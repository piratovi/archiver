package com.kolosov;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.kolosov.Const.ARCHIVE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackerTest {

    @Test
    void Pack() throws IOException {
        // setup
        String[] fileNames = {"fileToPack_1.jpg", "fileToPack_2.jpg", "dirToPack_1", "dirToPack_2"};

        ClassLoader classLoader = getClass().getClassLoader();
        File sourceDirectory = new File(classLoader.getResource("pack").getFile());

        Packer packer = new Packer(fileNames, sourceDirectory);

        // act
        packer.pack();

        // verify
        File archiveFile = new File(sourceDirectory, ARCHIVE_NAME);
        assertTrue(archiveFile.exists());
        assertTrue(archiveFile.length() > 0);

        int fileCounter = 0;
        try (FileInputStream fileInputStream = new FileInputStream(archiveFile)) {
            try (ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    fileCounter++;
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }
        assertEquals(12, fileCounter);
    }
}
