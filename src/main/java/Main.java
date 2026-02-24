import exceptions.LengthLineException;
import logs.LogEntry;
import monitoring.Statistics;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int countCorPath = 0;
        String path = "D:\\JAVALearn\\StepUp\\AccessLogParser\\access.log";

        Statistics statistics = new Statistics();

//        while (true) {
            System.out.println("Укажите путь к файлу");
//            path = new Scanner(System.in).nextLine();
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

                System.out.println("Общий трафик: " + statistics.getTotalTraffic());
                System.out.printf("Средний трафик в час: %.2f\n", statistics.getTrafficRate());

//                Для проверки
                System.out.println(Arrays.toString(statistics.getPaths().toArray()));
                System.out.println(statistics.getStatisticOS());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
//        }
    }
}