

public class DiscountEngine {
  public static void main(String[] args) {
    String customerType = "STUDENT";
    double orderAmount = 4000.0;
    String couponCode = "SAVE10";
    String membershipTier= "GOLD";

    double customerDiscount = 0;
    double amountDiscount = 0;
    double couponDiscount = 0 ;
    double membershipDiscount = 0;

    if (customerType.equals("STUDENT")) {
      customerDiscount = 5;
    } else if (customerType.equals("SENIOR")) {
      customerDiscount = 8;
    } else if (customerType.equals("REGULAR")) {
      customerDiscount = 0;
    }

    if (orderAmount >= 5000) {
      amountDiscount = 10;
    } else if (orderAmount >= 2000) {
      amountDiscount = 5;
    }

    if (couponCode.equals("SAVE10") && orderAmount >= 1000) {
      couponDiscount = 10;
    }

    if (membershipTier.equals("SILVER")) {
      membershipDiscount = 2;
    } else if (membershipTier.equals("GOLD")) {
      membershipDiscount = 5;
    } else if (membershipTier.equals("PLATINUM")) {
      membershipDiscount = 8;
    }

    double totalDiscount = customerDiscount
                + amountDiscount
                + couponDiscount
                + membershipDiscount;


    if (totalDiscount > 30) {
      totalDiscount = 30;
    }

    double discountAmount = orderAmount*totalDiscount/100;

    double finalPrice = orderAmount - discountAmount;

    System.out.println("Order Amount: " + orderAmount);
    System.out.println("Total Discount: " + totalDiscount + "%");
    System.out.println("Discount Amount: " + discountAmount);
    System.out.println("Final Price: " + finalPrice);
  }
}
