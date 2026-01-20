import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());
        int countCorPath = 0;

        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if(isDirectory) {
                System.out.println("Указана папка, а не файл");
                continue;
            } else if (!fileExists){
                System.out.println("Файл не найден");
            }
            else {
                countCorPath++;
                System.out.printf("Путь указан верно %d раз\n", countCorPath);
            }
        }
    }
}