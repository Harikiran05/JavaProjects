import java.util.Map;
public class DiscountEngine {

  private static final Map<String, Double> CUSTOMER_DISCOUNTS = 
          Map.of(
            "STUDENT", 5.0,
            "SENIOR", 8.0,
            "REGULAR", 0.0
          );

  private static final Map<String, Double> MEMBERSHIP_DISCOUNTS = 
          Map.of(
            "SILVER", 2.0,
            "GOLD", 5.0,
            "PLATINUM", 8.0
          );

  public static void main(String[] args) {
    String customerType = "STUDENT";
    double orderAmount = 6000.0;
    String couponCode = "SAVE10";
    String membershipTier = "GOLD";

    if (orderAmount <= 0) {
      System.out.println("Order amount must be positive.");
      return;
    }

    if (!CUSTOMER_DISCOUNTS.containsKey(customerType)) {
      System.out.println("Invalid Customer type.");
      return;
    }

    if (!MEMBERSHIP_DISCOUNTS.containsKey(membershipTier)) {
      System.out.println("Invalid Membership tier.");
      return;
    }

    double customerDiscount = CUSTOMER_DISCOUNTS.get(customerType);
    double amountDiscount = getAmountDiscount(orderAmount);
    double couponDiscount = getCouponDiscount(couponCode, orderAmount);
    double membershipDiscount = MEMBERSHIP_DISCOUNTS.get(membershipTier);

    double totalDiscount = Math.min(customerDiscount + amountDiscount + couponDiscount + membershipDiscount, 30);

    double discountAmount = orderAmount*totalDiscount/100;
    double finalPrice = orderAmount - discountAmount;

    System.out.println("Order Amount: " + orderAmount);
    System.out.println("Total Discount: " + totalDiscount);
    System.out.println("Discount Amount: " + totalDiscount + "%");
    System.out.println("Final Price: " + finalPrice);
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


}
