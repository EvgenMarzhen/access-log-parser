import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int countCorPath = 0;
        String path;

        while (true) {
            System.out.println("Укажите путь к файлу");
            path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if(isDirectory) {
                System.out.println("Указана папка, а не файл");
            }

            if (!fileExists){
                System.out.println("Файл не найден");
            } else {
                countCorPath++;
                System.out.printf("Путь указан верно %d раз(-a)\n", countCorPath);
            }

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                String line;
                int countLine = 0;
                int maxLength = 0;
                int minLength = Integer.MAX_VALUE;

                while ((line = reader.readLine()) != null) {
                    if(line.length() > 1024) throw new LengthLineException("Строка не должна быть длиннее 1024 символов");

                    countLine++;

                    if(line.length() > maxLength) {
                        maxLength = line.length();
                    }

                    if(line.length() < minLength) {
                        minLength = line.length();
                    }
                }

                System.out.println("Общее количество строк в файле: " + countLine);
                System.out.println("Самая длинная строка в файле: " + maxLength);
                System.out.println("Самая короткая строка в файле: " + minLength);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}