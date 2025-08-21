package finance.code;

import java.util.Scanner;

public class Toolbox {
    private static final Scanner cin = new Scanner(System.in);

    public static int intInput() {
        boolean error = true;
        int amount = 0;
        System.out.print("Amount: ");
        while (error) {
            if (cin.hasNextInt()) {
                amount = cin.nextInt();
                error = false;
                cin.nextLine();
            } else {
                System.out.print("Wrong data type! provide an integer value: ");
                cin.next();
                continue;
            }
        }
        return amount;
    }

    public static String stringCatInput() {
        String category;
        System.out.print("Category: ");
        category = cin.nextLine();
        return category;
    }

    public static String stringDescInput() {
        String description;
        System.out.print("Description: ");
        description = cin.nextLine();
        return description;
    }

}
