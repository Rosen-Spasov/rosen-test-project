package rspasov.problems;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface FamilyBudget {
   String userRole() default "GUEST";
   int budgetLimit();
}

class FamilyMember {
   @FamilyBudget(userRole = "Senior", budgetLimit = 100)
   public void seniorMember(int budget, int moneySpend) {
      System.out.println("Senior Member");
      System.out.println("Spend: " + moneySpend);
      System.out.println("Budget Left: " + (budget - moneySpend));
   }

   @FamilyBudget(userRole = "Junior", budgetLimit = 50)
   public void juniorUser(int budget, int moneySpend) {
      System.out.println("Junior Member");
      System.out.println("Spend: " + moneySpend);
      System.out.println("Budget Left: " + (budget - moneySpend));
   }
}

public class DeclaringAnnotations {
   public static void main(String[] args) {
      Scanner in = new Scanner(System.in);
      try {
	      int testCases = Integer.parseInt(in.nextLine());
	      while (testCases > 0) {
	         String role = in.next();
	         int spend = in.nextInt();
	         try {
	            Class<?> annotatedClass = FamilyMember.class;
	            Method[] methods = annotatedClass.getMethods();
	            for (Method method : methods) {
	               if (method.isAnnotationPresent(FamilyBudget.class)) {
	                  FamilyBudget family = method
	                        .getAnnotation(FamilyBudget.class);
	                  String userRole = family.userRole();
	                  int budgetLimit = family.budgetLimit();
	                  if (userRole.equals(role)) {
	                     if(spend <= budgetLimit){
	                        method.invoke(FamilyMember.class.newInstance(),
	                              budgetLimit, spend);
	                     }else{
	                        System.out.println("Budget Limit Over");
	                     }
	                  }
	               }
	            }
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         testCases--;
	      }
      } finally {
    	  in.close();
      }
   }
}