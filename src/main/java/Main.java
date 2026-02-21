import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int countCorPath = 0;
        String path = "D:\\JAVALearn\\StepUp\\AccessLogParser\\access.log";

//        while (true) {
//            System.out.println("Укажите путь к файлу");
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
                    System.out.println(logEntry);

//                    String fragment = "";
//
//                    String[] parts = userAgent.toString().split(";");
//                    if (parts.length >= 2) {
//                        fragment = parts[1];
//                    }
//                    String cleanFragment = fragment.trim();
//
//                    String[] programs = cleanFragment.split("/");
//                    String program = "";
//
//                    if (programs.length > 0) {
//                        program = programs[0];
//                    }
//
//                    countReq++;
//
//                    if (program.equalsIgnoreCase("YandexBot")) {
//                        countYandexBot++;
//                    }
//
//                    if (program.equalsIgnoreCase("GoogleBot")) {
//                        countGoogleBot++;
//                    }
                }

//                System.out.println("Общее число запросов: " + countReq);
//
//                if (countReq > 0) {
//                    System.out.printf("Доля запросов от YandexBot: %.2f%%\n", (double) countYandexBot / countReq * 100);
//                }
//
//                if (countReq > 0) {
//                    System.out.printf("Доля запросов от GoogleBot: %.2f%%\n", (double) countGoogleBot / countReq * 100);
//                }


            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
//        }
    }
}