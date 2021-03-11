package com.kolosov;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.kolosov.Const.ARCHIVE_NAME;

@Slf4j
public class Unpacker {

    public void unpack() throws IOException {
        File archiveFile = new File(ARCHIVE_NAME);
        if (!archiveFile.exists()) {
            throw new FileNotFoundException(String.format("Файл %s для распаковки не найден в директории проекта", ARCHIVE_NAME));
        }

        try (FileInputStream fileInputStream = new FileInputStream(archiveFile)) {
            try (ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {

                    File newFile = new File(zipEntry.getName());
                    if (zipEntry.getName().endsWith(File.separator)) {
                        newFile.mkdir();
                    } else {
                        try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)){
                            zipInputStream.transferTo(fileOutputStream);
                        }
                        log.info("файл {} распакован", zipEntry.getName());
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }

    }
}