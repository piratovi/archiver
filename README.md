# archiver
Приложение для архивации и деархивации файлов.
Работает с файлами в корневой папке проекта.

В папке проекта уже есть файлы и директории с вложеными файлами для проверки работы программы.
Файлы для проверки архивации : fileToPack_1.jpg, fileToPack_2.jpg, dirToPack_1, dirToPack_2
Файл для проверки разархивации : archived.zip

Чтобы запустить приложение на архивацию, нужно указать аргументами список файлов/директорий, которые расположены в корневой папке проекта.
Пример команды для запуска архивации:
mvn exec:java -Dexec.mainClass=com.kolosov.Main -Dexec.args="fileToPack_1.jpg fileToPack_2.jpg dirToPack_1 dirToPack_2"
После исполнения этой команды в папке проекта появится запакованый архив archived.zip.

Чтобы запустить приложение на разархивацию, аргументы указывать не нужно.
Программа попробует найти архив archived.zip в корневой папке проекта. Если такого файла нет, то приложение завершится с ошибкой.
Пример команды для запуска разархивации:
mvn exec:java -Dexec.mainClass=com.kolosov.Main
После исполнения этой команды в корневой папке проекта появятся распакованные файлы из архива archived.zip.
