package com.kolosov;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UnpackerTest {

    @Test
    void unpack() throws IOException {
        // setup
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("unpack");
        Objects.requireNonNull(url);
        File sourceDirectory = new File(url.getFile());

        Unpacker unpacker = new Unpacker(sourceDirectory);

        // act
        unpacker.unpack();

        // verify
        assertTrue(new File(sourceDirectory, "fileToPack_1.jpg").exists());

        assertTrue(new File(sourceDirectory, "fileToPack_2.jpg").exists());

        File dirToPack_1 = new File(sourceDirectory, "dirToPack_1");
        assertTrue(dirToPack_1.exists());

        File dirToPack_2 = new File(sourceDirectory, "dirToPack_2");
        assertTrue(dirToPack_2.exists());

        assertTrue(new File(dirToPack_1, "fileToPack_1.jpg").exists());
        assertTrue(new File(dirToPack_1, "fileToPack_2.jpg").exists());
        assertTrue(new File(dirToPack_1, "text.txt").exists());

        File dirToPack_1_dirToPack_1 = new File(dirToPack_1, "dirToPack_1");
        assertTrue(dirToPack_1_dirToPack_1.exists());

        assertTrue(new File(dirToPack_1_dirToPack_1, "fileToPack_1.jpg").exists());
        assertTrue(new File(dirToPack_1_dirToPack_1, "fileToPack_2.jpg").exists());

        assertTrue(new File(dirToPack_2, "fileToPack_1.jpg").exists());
        assertTrue(new File(dirToPack_2, "fileToPack_2.jpg").exists());
    }

    @Test
    void unpack_fail_noArchive() {
        // setup
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("");
        Objects.requireNonNull(url);
        File sourceDirectory = new File(url.getFile());

        Unpacker unpacker = new Unpacker(sourceDirectory);

        // act and verify
        assertThrows(FileNotFoundException.class, unpacker::unpack);
    }

}