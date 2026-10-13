import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class SmartConverter {

    public static final Scanner INPUT = new Scanner(System.in);

    private static final BigDecimal RUPEES_PER_DOLLAR = 
                          new BigDecimal("83.00");

    private static final double POUNDS_PER_KG = 2.20462;

    private static final double MILES_PER_KM = 0.621371;

  public static void main(String[] args) {

    int totalConversions = 0;

    while (true) {
      System.out.println("""
          SMART UNIT AND CURRENCY CONVERTER
                    
          1. Temperature
          2. Currency
          3. Weight
          4. Distance
          5. Floating-point precision demo
          6. Exit
          """);

          int choice = readIntInRange("choose an option: ", 1, 6);

          if (choice == 6) {
            System.out.println("Total successful conversions: " + totalConversions);

            System.out.println("Goodbye!");
            break;
          }

           switch (choice) {
                case 1:
                    convertTemperature();
                    break;

                case 2:
                    convertCurrency();
                    break;

                case 3:
                    convertWeight();
                    break;

                case 4:
                    convertDistance();
                    break;

                case 5:
                    demonstratePrecision();
                    break;

                default:
                    // The validation method prevents this case.
                    System.out.println("Invalid option.");
            }
    }
    INPUT.close();
  }

  private static void  convertTemperature() {
    System.out.println("""
        Temperature Conversion
        1. Celsius to Fahrenheit
        2. Fahrenheit to Celsius
        """);

        int direction = readIntInRange("choose direction: ", 1, 2);

        double value = readDouble(
          direction == 1 ? "Enter Celsius: " : "Enter Fahrenheit: ", true
        );

        double result;

        if (direction == 1) {
          result = (value*9/5)+32;
          System.out.printf("Result: %.2f °F%n", result);
        } else {
          result = (value - 32) *5/9;
          System.out.printf("Result:  %.2f °C%n", result);
        }
  }

  private static void convertCurrency() {
    System.out.print("""
        currency conversion
        1. INR to USD
        2. USD to INR
        """);

        int direction =  readIntInRange("choose direction: ", 1, 2);

        BigDecimal amount = readBigDecimal("Enter amount (zero or greater): ");

        if (direction == 1) {
          BigDecimal dollars  = amount.divide(
            RUPEES_PER_DOLLAR,
            2,
            RoundingMode.HALF_UP
          );
          System.out.println("Result: $" + dollars.toPlainString());
        } else {
          BigDecimal rupees  = amount.
          multiply(RUPEES_PER_DOLLAR)
          .setScale(2, RoundingMode.HALF_UP);

          System.out.println("Result: ₹" + rupees.toPlainString());
        }
  }

   private static void convertWeight() {

        System.out.print("""

                Weight conversion
                1. Kilograms to pounds
                2. Pounds to kilograms
                """);

        int direction = readIntInRange("Choose direction: ", 1, 2);

        double value = readDouble(
            direction == 1
                ? "Enter kilograms: "
                : "Enter pounds: ",
            false
        );

        double result;

        if (direction == 1) {
            result = value * POUNDS_PER_KG;
            System.out.printf("Result: %.2f lb%n", result);
        } else {
            result = value / POUNDS_PER_KG;
            System.out.printf("Result: %.2f kg%n", result);
        }
    }

    private static void convertDistance() {

        System.out.print("""

                Distance conversion
                1. Kilometres to miles
                2. Miles to kilometres
                """);

        int direction = readIntInRange("Choose direction: ", 1, 2);

        double value = readDouble(
            direction == 1
                ? "Enter kilometres: "
                : "Enter miles: ",
            false
        );

        double result;

        if (direction == 1) {
            result = value * MILES_PER_KM;
            System.out.printf("Result: %.2f miles%n", result);
        } else {
            result = value / MILES_PER_KM;
            System.out.printf("Result: %.2f km%n", result);
        }
    }

    private static void demonstratePrecision() {

        double doubleResult = 0.1 + 0.2;

        BigDecimal decimalResult =
            new BigDecimal("0.1").add(new BigDecimal("0.2"));

        System.out.println("\nFloating-point precision demonstration:");
        System.out.println("Using double: " + doubleResult);
        System.out.println(
            "Using BigDecimal: " + decimalResult.toPlainString()
        );
    }


  private static int readIntInRange(
    String prompt, int minimum, int maximum) {
      while (true) {
        System.out.println(prompt);
        String text = INPUT.nextLine().trim();

        try {
          int value = Integer.parseInt(text);

          if(value >= minimum && value <= maximum) {
            return value;
          }

          System.out.printf("Enter anumber from %d to %d.%n",
            minimum, 
            maximum
          );
        } catch (NumberFormatException exception) {
          System.out.println("Invalid input. Enter a whole number,");
        }
      }
    }

    private static double readDouble(String prompt, boolean allowNegative) {

      while (true) {
        System.out.print(prompt);
        String text = INPUT.nextLine().trim();

        try {
          double value = Double.parseDouble(text);

          if(!Double.isFinite(value)) {
            System.out.println("Enter a finite number.");
            continue;
          }

          if (!allowNegative && value < 0) {
            System.out.println("Negative values are not allowed.");
            continue;
          }
          return value;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid Number. Try again.");
        }
      }
    }

    private static BigDecimal readBigDecimal(String prompt) {
      while (true) {
        System.out.print(prompt);
        String text = INPUT.nextLine().trim();

        try{
          BigDecimal amount = new BigDecimal(text);

          if (amount.signum() < 0){
            System.out.println("Negative amounts are not allowed.");
            continue;
          } 
          return amount;
        } catch (NumberFormatException exception) {
          System.out.println("Invalid amount. Enter a vlaid decimal number.");
        }
      }
    }
}
