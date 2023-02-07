package TileManagement;

import java.util.Random;
import PlayerManagement.Player;

public class ChanceTileImpl implements ChanceTileInterface {

    public void drawCard(Player player, Tile tile) {

        Random rand = new Random();
        int i = rand.nextInt(9);
        double newBalance;
        Tile current;
        int temp;

        String[] chances = { "Some trees have fallen onto the road, go back to Go!",
                "Lightning has suddenly struck, you get pushed back 2 steps!",
                "You make the last Bus for the day, move forward 1 step!",
                "You invest Goven and your invest pays off, pick up £200!",
                "You help cleaning your local park, pick up £100 as a reward!",
                "You stay to help the Homeless shelter feed the homeless, they pay you £100!",
                "You are caught littering, pay £50!",
                "A massive storm floods your tent, pay £100 to replace it!",
                "You are found leaving rubbish at Tile10, pay £200 as a fine!" };


        switch (i) {
            case 0 -> {
                System.out.println(chances[0]);
                current = LoadTileTransaction.gameTiles.get(0);
                player.setCurrentTile(current);
                System.out.println("You have landed on " + player.getCurrentTile().name);
            }
            case 1 -> {
                System.out.println(chances[1]);
                temp = LoadTileTransaction.gameTiles.indexOf(player.getCurrentTile()) - 2;
                player.setCurrentTile(LoadTileTransaction.gameTiles.get(temp));
                System.out.println("You have landed on " + player.getCurrentTile().name);
            }
            case 2 -> {
                System.out.println(chances[2]);
                temp = LoadTileTransaction.gameTiles.indexOf(player.getCurrentTile()) + 1;
                player.setCurrentTile(LoadTileTransaction.gameTiles.get(temp));
                System.out.println("You have landed on " + player.getCurrentTile().name);
            }
            case 3 -> {
                System.out.println(chances[3]);
                newBalance = player.getBalance() + 200.00;
                player.setBalance(newBalance);
                System.out.println("Your new balance : " + player.getBalance());
            }
            case 4 -> {
                System.out.println(chances[4]);
                newBalance = player.getBalance() + 100.00;
                player.setBalance(newBalance);
                System.out.println("Your new balance : " + player.getBalance());
            }
            case 5 -> {
                System.out.println(chances[5]);
                newBalance = player.getBalance() + 100.00;
                player.setBalance(newBalance);
                System.out.println("Your new balance : " + player.getBalance());
            }
            case 6 -> {
                System.out.println(chances[6]);
                newBalance = player.getBalance() - 50.00;
                player.setBalance(newBalance);
                System.out.println("Your new balance : " + player.getBalance());
            }
            case 7 -> {
                System.out.println(chances[7]);
                newBalance = player.getBalance() - 100.00;
                player.setBalance(newBalance);
                System.out.println("Your new balance : " + player.getBalance());
            }
            case 8 -> {
                System.out.println(chances[8]);
                newBalance = player.getBalance() - 200.00;
                player.setBalance(newBalance);
                System.out.println("Your new balance : " + player.getBalance());
            }
        }


    }
}
