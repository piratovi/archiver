package com.kolosov;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.kolosov.Const.ARCHIVE_NAME;

/**
 * Упаковщик файлов. В результате работы создает файл с именем Const.ARCHIVE_NAME в директории Const.SOURCE_DIRECTORY.
 * Если ни один файл не найден в директории Const.SOURCE_DIRECTORY, то программа завершается с ошибкой.
 * Если в архивируемых файлах присутствует директория, то совершается ее рекурсивный обход.
 * Параметр сжатия файлов установлен на максимальное значение.
 */
@RequiredArgsConstructor
@Slf4j
public class Packer {

    //Список файлов/директорий на архивацию
    private final String[] fileNames;
    //Директория в которой расположены искомые файлы/директории, а также место где будет располагаться результирующий архив
    private final File sourceDirectory;

    /**
     * Основной метод для запаковки файлов/директорий
     * @throws IOException Выбрасывается в случае ошибки в файловой операции
     */
    public void pack() throws IOException {
        List<File> existingFiles = getExistingFiles();

        if (existingFiles.isEmpty()) {
            throw new FileNotFoundException("Файлы не найдены в директории проекта");
        }

        if (existingFiles.size() != fileNames.length) {
            List<File> missingFiles = getMissingFiles();
            log.warn("Некоторые файлы не найдены: {}", missingFiles);
        }

        File archiveFile = new File(sourceDirectory, ARCHIVE_NAME);
        try (FileOutputStream fileOutputStream = new FileOutputStream(archiveFile, false)) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
                zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);

                for (File fileToPack : existingFiles) {
                    if (fileToPack.isDirectory()) {
                        packDirectory(fileToPack, "", zipOutputStream);
                    } else {
                        packFile(fileToPack, "", zipOutputStream);
                    }
                }
            }
        }
    }

    /**
     * Метод для поиска существующих запрошеных файлов
     * @return существующие файлы
     */
    private List<File> getExistingFiles() {
        return Arrays.stream(fileNames)
                .map(fileName -> new File(sourceDirectory, fileName))
                .filter(File::exists)
                .collect(Collectors.toList());
    }

    /**
     * Метод для поиска отсутствующих запрошеных файлов
     * @return отсутствующие файлы
     */
    private List<File> getMissingFiles() {
        return Arrays.stream(fileNames)
                .map(fileName -> new File(sourceDirectory, fileName))
                .filter(file -> !file.exists())
                .collect(Collectors.toList());
    }

    /**
     * Метод для рекурсивного обхода директории.
     * @param file Обрабатываемая директория
     * @param parentFileName Имя внешней директории. Используется для корректного формирования ZipEntry.
     * @param zipOutputStream Zip stream для записи в архив
     * @throws IOException Выбрасывается в случае ошибки в файловой операции
     */
    private void packDirectory(File file, String parentFileName, ZipOutputStream zipOutputStream) throws IOException {
        String fullFileName = getFullFileName(parentFileName, file.getName());
        zipOutputStream.putNextEntry(new ZipEntry(fullFileName + "/"));
        zipOutputStream.closeEntry();

        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                packDirectory(childFile, fullFileName, zipOutputStream);
            } else {
                packFile(childFile, fullFileName, zipOutputStream);
            }
        }
    }

    /**
     * Метод для записи файла в архив.
     * @param file Обрабатываемый файл
     * @param parentFileName Имя внешней директории. Используется для корректного формирования ZipEntry.
     * @param zipOutputStream Zip stream для записи в архив
     * @throws IOException Выбрасывается в случае ошибки в файловой операции
     */
    private void packFile(File file, String parentFileName, ZipOutputStream zipOutputStream) throws IOException {
        String fullFileName = getFullFileName(parentFileName, file.getName());
        try (FileInputStream inputStream = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fullFileName);
            zipOutputStream.putNextEntry(zipEntry);
            inputStream.transferTo(zipOutputStream);
        }
        zipOutputStream.closeEntry();
        log.info("файл {} запакован", fullFileName);
    }

    /**
     * Метод получения entry name для формирования ZipEntry
     * @param parentFileName Имя внешней директории.
     * @param fileName Имя файл
     * @return entry name для ZipEntry
     */
    private String getFullFileName(String parentFileName, String fileName) {
        return parentFileName.isEmpty() ? fileName : parentFileName + "/" + fileName;
    }
}