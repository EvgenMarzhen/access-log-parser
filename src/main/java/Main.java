import exceptions.LengthLineException;
import logs.LogEntry;
import monitoring.Statistics;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int countCorPath = 0;
        String path;

        Statistics statistics = new Statistics();

        while (true) {
            System.out.println("Укажите путь к файлу");
            path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (isDirectory) {
                System.out.println("Указана папка, а не файл");
            }

            if (!fileExists) {
                System.out.println("Файл не найден");
            } else {
                countCorPath++;
                System.out.printf("Путь указан верно %d раз(-a)\n", countCorPath);
            }

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.length() > 1024) {
                        throw new LengthLineException("Строка не должна быть длиннее 1024 символов");
                    }

                    LogEntry logEntry = new LogEntry(line);
                    statistics.addEntry(logEntry);

                }

//              Для проверки
                System.out.printf("Среднее кол-во обращений от юзеров за час: %.2f\n", statistics.getAvgTrafficPerHourOfUser());
                System.out.printf("Среднее кол-во запросов, завершившихся ошибкой за час: %.2f\n", statistics.getAvgErrorReqPerHour());
                System.out.printf("Cредняя посещаемость одним пользователем: %.2f\n", statistics.getAvgPerIp());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}