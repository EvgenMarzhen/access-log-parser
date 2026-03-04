import exceptions.LengthLineException;
import logs.LogEntry;
import monitoring.Statistics;

import java.io.*;
import java.util.Arrays;
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
                statistics.getFractionByOS();
//       ============================================
                //Проверка, что сложение долей даст 1.0
//       ============================================
                double sum = 0.0;
                for (Double frac : statistics.getOsFractions().values()) {
                    sum += frac;
                }

                System.out.println(sum);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}