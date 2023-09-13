import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число:");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int secondNumber = new Scanner(System.in).nextInt();

        //Выводим сумму
        System.out.println("Сумма двух чисел:");
        int sum = firstNumber + secondNumber;
        System.out.println(sum);

        //Выводим разность
        System.out.println("Разность двух чисел:");
        int diff = firstNumber - secondNumber;
        System.out.println(diff);

        //Выводим произведение
        System.out.println("Произведение двух чисел:");
        int multiply = firstNumber * secondNumber;
        System.out.println(multiply);

        //Выводим частное
        System.out.println("Частное двух чисел:");
        double quotient = (double) firstNumber / secondNumber;
        System.out.println(quotient);
    }
}