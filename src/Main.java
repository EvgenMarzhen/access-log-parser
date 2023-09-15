import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Ввод двух чисел, на основании которых будет выполняться операция
        System.out.println("Введите первое число:");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int secondNumber = new Scanner(System.in).nextInt();

        //Расчет суммы
        int sum = firstNumber + secondNumber;

        //Расчет разности
        int diff = firstNumber - secondNumber;

        //Расчет произведение
        int multiply = firstNumber * secondNumber;

        //Расчет частного
        double quotient = (double) firstNumber / secondNumber;

        //Вывод результата
        System.out.println( "Результат: " + "\n" +
                "Сумма - " + sum + "\n" +
                "Разность - " + diff + "\n" +
                "Произведение - " + multiply + "\n" +
                "Частное - " + quotient);

    }
}