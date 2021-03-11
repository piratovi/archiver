package com.kolosov;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.kolosov.Const.SOURCE_DIRECTORY;

@Slf4j
public class Main {

    /**
     * Точка входа в приложение.
     * Если список файлов/директорий передан в качестве аргументов приложения, то начнется архивация этих файлов в файл archived.zip.
     * Если аргументы не переданы, то начнется поиск файла archived.zip и его разархивация в корневую папку проекта.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            log.info("Найдены имена файлов/директорий в качестве аргументов. Запаковка архива начата");
            Packer packer = new Packer(args, SOURCE_DIRECTORY);
            packer.pack();
            log.info("Запаковка архива закончена");
        } else {
            log.info("Не найдены имена файлов/директорий в качестве аргументов. Распаковка архива начата");
            Unpacker unpacker = new Unpacker(SOURCE_DIRECTORY);
            unpacker.unpack();
            log.info("Распаковка архива закончена");
        }
    }
}
