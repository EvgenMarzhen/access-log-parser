import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Ввод двух чисел, на основании которых будет выполняться операция
        System.out.println("Введите первое число:");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int secondNumber = new Scanner(System.in).nextInt();
        System.out.println("Расчет:");

        //Выводим сумму
        int sum = firstNumber + secondNumber;
        System.out.println("Сумма двух чисел: " + sum);

        //Выводим разность
        int diff = firstNumber - secondNumber;
        System.out.println("Разность двух чисел " + diff);

        //Выводим произведение
        int multiply = firstNumber * secondNumber;
        System.out.println("Произведение двух чисел: " + multiply);

        //Выводим частное
        double quotient = (double) firstNumber / secondNumber;
        System.out.println("Частное двух чисел: " + quotient);
    }
}