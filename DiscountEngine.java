

public class DiscountEngine {
  public static void main(String[] args) {
    String customerType = "STUDENT";
    double orderAmount = 100;
    String couponCode = "SAVE10";
    String membershipTier = "GOLD";

    if (orderAmount <= 0) {
      System.out.println("Order amount must be positive.");
      return;
    }

    if (!isValidCustomer(customerType)) {
      System.out.println("Invalid Customer type.");
      return;
    }

    if (!isValidTier(membershipTier)) {
      System.out.println("Invalid Membership tier.");
      return;
    }

    double customerDiscount = getCustomerDiscount(customerType);
    double amountDiscount = getAmountDiscount(orderAmount);
    double couponDiscount = getCouponDiscount(couponCode, orderAmount);
    double membershipDiscount = getMembershipDiscount(membershipTier);

    double totalDiscount = Math.min(customerDiscount + amountDiscount + couponDiscount + membershipDiscount, 30);

    double discountAmount = orderAmount*totalDiscount/100;
    double finalPrice = orderAmount - discountAmount;

    System.out.println("Order Amount: " + orderAmount);
    System.out.println("Total Discount: " + totalDiscount);
    System.out.println("Discount Amount: " + totalDiscount + "%");
    System.out.println("Final Price: " + finalPrice);
  }

  

  static boolean isValidCustomer(String customerType) {
    if (customerType == null) {
      return false;
    }

    return switch (customerType) {
      case "STUDENT", "SENIOR", "REGULAR" -> true;
      default -> false;
    };
  }

  static boolean isValidTier(String membershipTier) {
    if(membershipTier == null){
      return false;
    }

    return switch (membershipTier) {
      case "SILVER", "GOLD", "PLATINUM" -> true;
      default -> false;
    };
  }

  static double getCustomerDiscount(String customerType) {
    return switch (customerType) {
      case "STUDENT" -> 5;
      case "SENIOR" -> 8;
      case "REGULAR" -> 0;
      default -> 0;
    };
  }

  static double getAmountDiscount(double orderAmount) {
    if(orderAmount <= 0) {
      return 0;
    }

    if (orderAmount >= 5000) {
      return 10;
    }

    if(orderAmount >= 2000) {
      return 5;
    }
    return 0;
  }

  static double getCouponDiscount(String couponCode, double orderAmount) {
    if (couponCode == null || orderAmount < 1000) {
      return 0;
    }

    if (!couponCode.equals("SAVE10")) {
      return 0;
    }

    return 10;
  }

  static double getMembershipDiscount(String membershipTier) {
    return switch (membershipTier) {
      case "SILVER" -> 2;
      case "GOLD" -> 5;
      case "PLATINUM" -> 8;
      default -> 0;
    };
  }
}
