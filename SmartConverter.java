import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class SmartConverter {
  public static void main(String[] args) {
    
    Scanner input = new Scanner(System.in);

    BigDecimal rupeesDollar = new BigDecimal("83.00");

    System.out.println("Welcome to Smart Converter!");
    System.out.println("1. Celcius to Fahrenheit");
    System.out.println("2. INR to USD");
    System.out.println("3. Kilograms to Pounds");
    System.out.println("4. Kilometers to Miles");
    System.out.println("5. Floating Point Demo");
    System.out.println("6. Exit");

    System.out.print("Enter your choice: ");
    int choice = input.nextInt();

    switch (choice) {
      case 1: {
        System.out.println("Enter temperature in celsius: ");
        double celsius = input.nextDouble();

        double fahrenheit = (celsius * 9/5) + 32;
        System.out.println("Fahrenheit: " + fahrenheit);
        System.out.println("Integer value after casting: " + (int) fahrenheit);
        break;
      }
      
      case 2: {
        System.out.println("Enter amount in INR: " );
        BigDecimal inr  = new BigDecimal(input.next());

        BigDecimal usd = inr.divide(
          rupeesDollar,
          2,
          RoundingMode.HALF_UP
        );
        System.out.println("USD: $"+ usd.toPlainString());
        

        break;
      }

      case 3: {
        System.out.println("Enter weight in kilos: " );
        double kilograms = input.nextDouble();

        double pounds = kilograms*2.20462;
        System.out.println("Pounds: "+ pounds);
      
        break;
      }

      case 4: {
        System.out.println("Enter Distance in kilometres: " );
        double Kilometres = input.nextDouble();

        double Miles = Kilometres*0.621371;
        System.out.println("Miles: "+ Miles);
        System.out.println("Integer value after casting: " + (int) Miles);

        break;
      }

      case 5: {
        double doubleResult = 0.1 + 0.2;

        BigDecimal decimalResult = 
                  new BigDecimal("0.1").add(new BigDecimal("0.2"));

                  System.out.println("Using double: " + doubleResult);

                  System.out.println("Using BigDecimal: " + decimalResult.toPlainString());

                  break;
      }
      case 6: 
        System.out.println("Goodbye!");
        break;
      
      default:
        System.out.println("Invalid menu option");
    }
    input.close();
  }
}
