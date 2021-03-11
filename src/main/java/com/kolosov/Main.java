package com.kolosov;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            log.info("Найдены имена файлов/директорий в качестве аргументов. Запаковка архива начата");
            Packer packer = new Packer(args);
            packer.pack();
            log.info("Запаковка архива закончена");
        } else {
            log.info("Не найдены имена файлов/директорий в качестве аргументов. Распаковка архива начата");
            Unpacker unpacker = new Unpacker();
            unpacker.unpack();
            log.info("Распаковка архива закончена");
        }
    }
}
