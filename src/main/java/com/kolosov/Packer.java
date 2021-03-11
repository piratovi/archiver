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

@RequiredArgsConstructor
@Slf4j
public class Packer {

    private final String[] fileNames;
    private final File sourceDirectory;

    public void pack() throws IOException {
        List<File> files = Arrays.stream(fileNames)
                .map(fileName -> new File(sourceDirectory, fileName))
                .filter(File::exists)
                .collect(Collectors.toList());

        if (files.isEmpty()) {
            throw new FileNotFoundException("Файлы не найдены в директории проекта");
        }

        File archiveFile = new File(sourceDirectory, ARCHIVE_NAME);
        try (FileOutputStream fileOutputStream = new FileOutputStream(archiveFile, false)) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
                zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);

                for (File fileToPack : files) {
                    if (fileToPack.isDirectory()) {
                        packDirectory(fileToPack, "", zipOutputStream);
                    } else {
                        packFile(fileToPack, "", zipOutputStream);
                    }
                }
            }
        }
    }

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

    private String getFullFileName(String parentFileName, String fileName) {
        return parentFileName.isEmpty() ? fileName : parentFileName + "/" + fileName;
    }
}
