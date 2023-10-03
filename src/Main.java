import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int countFile = 0;
        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (fileExists == true && isDirectory == false)  {
                countFile++;
                System.out.println("Путь указан верно " +  "\nЭто файл номер " + countFile);
            }
            else {
                System.out.println("Данные не найдены");
            }
        }
    }
}