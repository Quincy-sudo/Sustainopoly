package Main.transaction;

import java.util.Scanner;

public class HowToPlayTransaction {

    static MenuTransaction m = new MenuTransaction();

    private static Scanner input = new Scanner(System.in);

    public void ShowRules() {

        System.out.print("\nWelcome to Sustainopoly! \n"
                + "this will showcase the game rules and how to play the game");


        System.out.println("Press enter to go back to main menu.");
        input.nextLine();
        m.loadMenu();

    }

}
