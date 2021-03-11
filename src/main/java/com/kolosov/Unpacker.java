package com.kolosov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.kolosov.Const.ARCHIVE_NAME;

/**
 * Распаковщик файлов. Из архива с именем Const.ARCHIVE_NAME в директории Const.SOURCE_DIRECTORY распаковываются все файлы в директорию Const.SOURCE_DIRECTORY.
 * Если Const.ARCHIVE_NAME не найден в директории Const.SOURCE_DIRECTORY, то программа завершается с ошибкой.
 */
@Slf4j
@RequiredArgsConstructor
public class Unpacker {

    //Директория в которой расположен искомый архив, а также место где будет располагаться разархивированные файлы/директории
    private final File sourceDirectory;

    /**
     * Основной метод для распаковки архива
     * @throws IOException Выбрасывается в случае ошибки в файловой операции
     */
    public void unpack() throws IOException {
        File archiveFile = new File(sourceDirectory, ARCHIVE_NAME);
        if (!archiveFile.exists()) {
            throw new FileNotFoundException(String.format("Файл %s для распаковки не найден в директории проекта", ARCHIVE_NAME));
        }

        try (FileInputStream fileInputStream = new FileInputStream(archiveFile)) {
            try (ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();

                while (zipEntry != null) {
                    File newFile = new File(sourceDirectory, zipEntry.getName());
                    if (zipEntry.isDirectory()) {
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
