package com.kolosov;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UnpackerTest {

    @Test
    void unpack() throws IOException {
        // setup
        ClassLoader classLoader = getClass().getClassLoader();
        File sourceDirectory = new File(classLoader.getResource("unpack").getFile());

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
}