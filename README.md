# archiver
Приложение для архивации и деархивации файлов.<br/>
Работает с файлами в корневой папке проекта.<br/>

В папке проекта уже есть файлы и директории с вложеными файлами для проверки работы программы.<br/>
Файлы для проверки архивации : fileToPack_1.jpg, fileToPack_2.jpg, dirToPack_1, dirToPack_2<br/>
Файл для проверки разархивации : archived.zip<br/>

Чтобы запустить приложение на архивацию, нужно указать аргументами список файлов/директорий, которые расположены в корневой папке проекта.<br/>
Пример команды для запуска архивации:<br/>
mvn exec:java -Dexec.mainClass=com.kolosov.Main -Dexec.args="fileToPack_1.jpg fileToPack_2.jpg dirToPack_1 dirToPack_2"<br/>
После исполнения этой команды в папке проекта появится запакованый архив archived.zip.<br/>

Чтобы запустить приложение на разархивацию, аргументы указывать не нужно.<br/>
Программа попробует найти архив archived.zip в корневой папке проекта. Если такого файла нет, то приложение завершится с ошибкой.<br/>
Пример команды для запуска разархивации:<br/>
mvn exec:java -Dexec.mainClass=com.kolosov.Main<br/>
После исполнения этой команды в корневой папке проекта появятся распакованные файлы из архива archived.zip.<br/>
