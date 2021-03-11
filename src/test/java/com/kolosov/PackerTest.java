package com.kolosov;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.kolosov.Const.ARCHIVE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackerTest {

    @Test
    void pack() throws IOException {
        // setup
        String[] fileNames = {"fileToPack_1.jpg", "fileToPack_2.jpg", "dirToPack_1", "dirToPack_2"};

        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("pack");
        Objects.requireNonNull(url);
        File sourceDirectory = new File(url.getFile());

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

    @Test
    void pack_fail_noFiles() {
        // setup
        String[] fileNames = {};

        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("pack");
        Objects.requireNonNull(url);
        File sourceDirectory = new File(url.getFile());

        Packer packer = new Packer(fileNames, sourceDirectory);

        // act and verify
        assertThrows(FileNotFoundException.class, packer::pack);
    }

}
