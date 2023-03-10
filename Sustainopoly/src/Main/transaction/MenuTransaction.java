package Main.transaction;

import Main.Menu;
import PlayerManagement.transactions.RegisterPlayerTransaction;

public class MenuTransaction {

    static RegisterPlayerTransaction RP = new RegisterPlayerTransaction();
    static HowToPlayTransaction HTP = new HowToPlayTransaction();

    public void loadMenu() {
        int choice = 0;
        String[] options = {"New Game", "How To Play", "Exit Application"};
        String title = "Actions:";
        Menu m = new Menu(title, options);
        m.display();
        choice = m.getChoice();
        if(choice != 3) {
            processChoice(choice);
        }
        else if (choice == 3) {
            System.out.println("Game Closing now");
            System.exit(0);;
        }
        // do {
        // 	m.display();
        // 	choice = m.getChoice();
        // 	if(choice != 4) {
        // 		processChoice(choice);
        // 	}
        // }
        // while(choice != 4);
        // System.out.println("(WARNING) Applicaiton is closing");
    }

    public static void processChoice(int choice) {
        switch(choice) {
            case 1:
                RP.RegisterPlayers();
                break;
            case 2:
                HTP.ShowRules();
                break;
            default:
                System.out.println("That is not a valid option. Please choose another number.");
                break;
        }
    }
}
