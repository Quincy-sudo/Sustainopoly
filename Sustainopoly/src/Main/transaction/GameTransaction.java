package Main.transaction;

import java.util.*;

import PlayerManagement.Player;
import PlayerManagement.transactions.RegisterPlayerTransaction;
import PlayerManagement.transactions.PlayerPayTransaction;
import PlayerManagement.transactions.PlayerRollTransaction;
import Dice.Dice;
import TileManagement.ChanceTileImpl;
import TileManagement.LoadTileTransaction;
import TileManagement.Tile;

public class GameTransaction {

    public List<Player> gamePlayers = RegisterPlayerTransaction.gamePlayers; // getting player list from
    // registerPlayerTransactions
    private static final EndGameTransaction egt = new EndGameTransaction();
    static MenuTransaction m = new MenuTransaction();

    private static Dice currentRoll;

    private static Scanner input = new Scanner(System.in);

    public enum tradeAns {
        sell, trade, no
    }

    public enum yesNo {
        yes, no
    }

    boolean holder = false;

    public void turn() {

        PlayerRollTransaction playerRoll = new PlayerRollTransaction();

        for (int i = 0; i < gamePlayers.size(); i++) {

            currentRoll = playerRoll.roll(gamePlayers.get(i)).get(i);
            System.out.println(gamePlayers.get(i).getName() + " your balance is " + gamePlayers.get(i).getBalance());


            // getting a temp value for the index the player is moved to
            int temp = currentRoll.getDice1() + currentRoll.getDice2()
                    + LoadTileTransaction.gameTiles.indexOf(gamePlayers.get(i).getCurrentTile());

            if (temp > 39) {
                temp = temp - 40;
                gamePlayers.get(i).setBalance(gamePlayers.get(i).getBalance() + 200);
                System.out.println(gamePlayers.get(i).getName() + " has passed go and received £200!");
            }

            // setting new currentTile for given player
            gamePlayers.get(i).setCurrentTile(LoadTileTransaction.gameTiles.get(temp));

            System.out.println(gamePlayers.get(i).getName() + " You have landed on:");
            gamePlayers.get(i).getCurrentTile().printDetails();

            switch (gamePlayers.get(i).getCurrentTile().getTileType()) {

                case "noActionTile":
                    break;
                case "chanceTile":
                    takeChanceCard(gamePlayers.get(i), gamePlayers.get(i).getCurrentTile());
                    break;

                case "taxTile":
                    payTax(gamePlayers.get(i), gamePlayers.get(i).getCurrentTile());
                    break;
                case "propertyTile":
                    if (gamePlayers.get(i).getCurrentTile().isBuyable()) {
                        buyProperty(gamePlayers.get(i), gamePlayers.get(i).getCurrentTile());
                        holder = false;
                        break;
                    } else {
                        payRent(gamePlayers.get(i), gamePlayers.get(i).getCurrentTile());
                    }
                case "goTile":
                    // add ‚£200
                    break;
            }

            // getting list of player owned tiles
            List<Tile> tempList = gamePlayers.get(i).getOwnedTiles();
            boolean hold = false;

            // option to sell or trade current properties.
            if (!tempList.isEmpty()) {

                while (!hold) {
                    System.out.println("would you like to sell or trade any of your properties  (sell/trade/No)");
                    String res = input.nextLine();

                    // tradeAns.valueOf(res);
                    EnumSet<tradeAns> except = EnumSet.of(tradeAns.sell, tradeAns.trade, tradeAns.no);

                    boolean valid;

                    try {
                        valid = !except.contains(tradeAns.valueOf(res));

                        if (res.equalsIgnoreCase("sell")) {
                            sellProperty(gamePlayers.get(i));
                        }
                        if (res.equalsIgnoreCase("trade")) {
                            tradeProperty(gamePlayers.get(i));
                        }
                        if (res.equalsIgnoreCase("no")) {
                            hold = true;
                        }
                        hold = true;
                    } catch (IllegalArgumentException e) {
                        valid = false;
                    }

                }

            }

            // option to buy solar panel park
            if (checkSolarPanelBuyable(gamePlayers.get(i))) {
                buySolarPanel(gamePlayers.get(i));
            }
            // ending turn sequence
            System.out.println(gamePlayers.get(i).getName() + ", press the enter button to end your turn");
            input.nextLine();
            System.out
                    .println("\n" + "#########################################################################" + "\n");
            checkGameOver(gamePlayers.get(i));

        }

    }

    public void checkGameOver(Player player) {
        if (player.getBalance() < 0) {
            removePlayer(player);
            System.out.println("Game over! " + player.getName() + " has gone bankrupt.");
            // End the game
            egt.endGame(gamePlayers);
            m.loadMenu();

        }
    }

    public void buyProperty(Player player, Tile tile) {

        PlayerPayTransaction pay = new PlayerPayTransaction();
        // here you have option to buy

        while (!holder) {
            System.out.println("Are you interested in investing in the property?  (yes/no)");
            System.out.println("Cost: £" + tile.cost);
            String in = input.nextLine();

            boolean valid;

            EnumSet<yesNo> except = EnumSet.of(yesNo.yes, yesNo.no);

            try {
                valid = !except.contains(yesNo.valueOf(in));

                if (in.equalsIgnoreCase("Yes")) {
                    pay.playerBuy(player, tile);
                }
                if (in.equalsIgnoreCase("no")) {
                    return;
                }
                holder = true;

            } catch (IllegalArgumentException e) {
                valid = false;
                System.out.println("Please select one of the choices");
            }

        }

    }

    public void sellProperty(Player player) {
        PlayerPayTransaction sell = new PlayerPayTransaction();
        List<Tile> temp = new ArrayList<Tile>();
        temp = player.getOwnedTiles();
        boolean hol = false;
        while (hol == false) {
            try {
                System.out
                        .println("Select the property you would like to sell from the list using the assigned number");

                for (int i = 0; i < temp.size(); i++) {
                    System.out.println("\n" + i + " " + temp.get(i).getName() + " Amount: " + temp.get(i).getCost());
                }

                String response = input.nextLine();
                boolean numeric = true;

                try {
                    Double num = Double.parseDouble(response);
                } catch (NumberFormatException e) {
                    numeric = false;
                }

                if (numeric) {
                    int ans = Integer.parseInt(response);
                    String res = temp.get(ans).getName();
                    Tile t = temp.get(ans);

                    System.out.println("you have selected " + " " + res);
                    sell.playerSellTile(player, t);
                    hol = true;
                } else {
                    System.out.println(response + " is not a number");
                }

            } catch (Exception e) {
                System.out.println("Not an integer");
                hol = false;

            }

        }

    }

    public void tradeProperty(Player player) {
        boolean numeric = true;
        boolean hol1 = false;
        while (hol1 == false) {

            try {

                System.out.println(
                        "Choose the player who you would like to trade with from the list using the assigned number");

                for (int i = 0; i < gamePlayers.size(); i++) {

                    if (gamePlayers.get(i) != player)
                        System.out.println("\n" + i + " " + gamePlayers.get(i).getName());
                }

                String chosen = input.nextLine();

                try {
                    Double num = Double.parseDouble(chosen);
                } catch (NumberFormatException e) {
                    numeric = false;
                }

                if (numeric) {
                    int ans = Integer.parseInt(chosen);

                    PlayerPayTransaction Trade = new PlayerPayTransaction();
                    List<Tile> temp = new ArrayList<Tile>();
                    System.out.println(
                            "Select which one of your properties you would like to trade from the list using the assigned number");
                    temp = player.getOwnedTiles();
                    for (int i = 0; i < temp.size(); i++) {
                        System.out.println("\n" + i + " " + temp.get(i).getName());
                    }
                    String response = input.nextLine();
                    boolean numeric1 = true;
                    try {
                        Double num1 = Double.parseDouble(response);
                    } catch (NumberFormatException e) {
                        numeric1 = false;
                    }
                    if (numeric1) {
                        int ans1 = Integer.parseInt(response);
                        String res = temp.get(ans1).getName();
                        Tile t1 = temp.get(ans1);

                        List<Tile> ptemp = new ArrayList<Tile>();
                        System.out.println("Select which property you would like to acquire from the other player " +
                                "from the list using the assigned number");
                        Player playTemp = gamePlayers.get(ans);
                        ptemp = playTemp.getOwnedTiles();
                        for (int i = 0; i < ptemp.size(); i++) {
                            System.out.println("\n" + i + " " + ptemp.get(i).getName());
                        }
                        String re = input.nextLine();
                        boolean numeric2 = true;
                        try {
                            Double num1 = Double.parseDouble(chosen);
                        } catch (NumberFormatException e) {
                            numeric2 = false;
                        }
                        if (numeric2) {
                            int ans2 = Integer.parseInt(re);
                            Tile t2 = ptemp.get(ans2);

                            boolean valid;
                            EnumSet<yesNo> except = EnumSet.of(yesNo.yes, yesNo.no);

                            System.out.println(gamePlayers.get(ans).getName() + " "
                                    + "do you wish to accept this trade? (Yes/no)");
                            String resp = input.nextLine();
                            try {
                                valid = !except.contains(yesNo.valueOf(resp));

                                if (resp.equalsIgnoreCase("Yes")) {
                                    Trade.playerTradeTile(player, playTemp, t1, t2);
                                    hol1 = true;
                                }
                                if (resp.equalsIgnoreCase("no")) {
                                    System.out.println("Trade has been declined");
                                    return;
                                }

                            } catch (IllegalArgumentException e) {
                                valid = false;
                                System.out.println("Please select one of the choices");
                            }

                        }

                    }
                }
            } catch (Exception e) {
                System.out.println("not valid");
            }

        }

    }

    public void buySolarPanel(Player player) {
        boolean holder = false;
        while (!holder) {
            System.out.println(player.getName()
                    + ", are you interested in building Solar panels on one of your Buildings?  (yes/no)");
            String playerChoice = input.nextLine();
            playerChoice = playerChoice.toLowerCase();
            boolean valid;
            EnumSet<yesNo> except = EnumSet.of(yesNo.yes, yesNo.no);

            try {
                valid = !except.contains(yesNo.valueOf(playerChoice));

                if (playerChoice.equalsIgnoreCase("Yes")) {
                    System.out.println("Here are all the tiles you can currently build Solar Panels for: \n");
                    List<Tile> potentialLocations = this.getSolarPanelPotentialLocations(player);
                    for (int k = 0; k < potentialLocations.size(); k++) {
                        System.out.println(k + " " + potentialLocations.get(k).getName() + " Price: Â£"
                                + potentialLocations.get(k).getCostOfPanels());
                    }
                    System.out.println("\n Please enter the index of the tile you want to build Solar Panels for:");
                    String index = input.nextLine();

                    boolean numeric = true;
                    try {
                        Double num1 = Double.parseDouble(index);
                    } catch (NumberFormatException e) {
                        numeric = false;
                    }
                    if (numeric) {
                        int num = Integer.parseInt(index);
                        potentialLocations.get(num).setRent(potentialLocations.get(num).getRentWithPanels());
                        potentialLocations.get(num).setPanelsBuildable(false);
                        player.setBalance(player.getBalance() - potentialLocations.get(num).getCostOfPanels());
                        System.out.println("Solar panel purchased for tile: " + potentialLocations.get(num).name);
                        System.out.println(player.getName() + " now has a balance of £" + player.getBalance());
                        holder = true;
                    }

                }
                if (playerChoice.equalsIgnoreCase("no")) {
                    return;
                }

            } catch (Exception e) {
                valid = false;
                System.out.println("Please select one of the choices");
                holder = false;
            }

        }

    }

    public void payRent(Player player, Tile tile) {

        List<Tile> temp = new ArrayList<Tile>();
        temp = player.getOwnedTiles();

        if (player.getBalance() >= player.getCurrentTile().getRent()) {
            System.out.println("Player " + tile.getOwner().getName() + " owns this tile!");
            System.out.println(player.getName() + "  you must pay £" + player.getCurrentTile().getRent());
            player.setBalance(player.getBalance() - player.getCurrentTile().getRent());
            System.out.println("Your new balance is £" + player.getBalance());
            Player x = tile.getOwner();
            x.setBalance(x.getBalance() + tile.getRent());
        } else if (!temp.isEmpty()) {
            System.out.println(
                    "You do not have sufficient funds to pay the rent you are being forced to liquidate your assets");
            liquidate(player);
        } else {
            System.out.println("You have gone bust and have lost");
            removePlayer(player);
        }
    }

    public void removePlayer(Player player) {
        gamePlayers.remove(player);
        System.out.println("You have been removed from the game");
        if (gamePlayers.remove(player)) {
            egt.endGame(gamePlayers);

        }

    }

    public void liquidate(Player player) {
        Tile t;
        PlayerPayTransaction sell = new PlayerPayTransaction();
        List<Tile> temp = new ArrayList<Tile>();
        temp = player.getOwnedTiles();
        for (int i = 0; i < temp.size(); i++) {
            t = temp.get(i);
            sell.playerSellTile(player, t);
        }
    }

    public void takeChanceCard(Player player, Tile tile) {
        // array of different chance cards
        // randomly select one
        ChanceTileImpl chance = new ChanceTileImpl();
        chance.drawCard(player, tile);

    }

    public void payTax(Player player, Tile tile) {
        List<Tile> temp = new ArrayList<Tile>();
        temp = player.getOwnedTiles();
        boolean hol = false;

        if (player.getBalance() >= player.getCurrentTile().getRent()) {
            System.out.println(player.getName() + "  you must pay £" + player.getCurrentTile().getRent());
            player.setBalance(player.getBalance() - player.getCurrentTile().getRent());
            System.out.println("Your new balance is £" + player.getBalance());
        }
    }


    public boolean checkSolarPanelBuyable(Player player) {

        Map<String, Integer> ownedTileCounts = new HashMap<>();
        Map<String, Integer> totalTileCounts = new HashMap<>();

        for (Tile ownedTile : player.getOwnedTiles()) {
            String colour = ownedTile.getColour();
            ownedTileCounts.put(colour, ownedTileCounts.getOrDefault(colour, 0) + 1);
        }

        for (Tile gameTile : LoadTileTransaction.gameTiles) {
            String colour = gameTile.getColour();
            totalTileCounts.put(colour, totalTileCounts.getOrDefault(colour, 0) + 1);
        }
        for (Tile ownedTile : player.getOwnedTiles()) {
            String colour = ownedTile.getColour();
            if (ownedTileCounts.get(colour) == totalTileCounts.get(colour) && ownedTile.getPanelsBuildable()) {
                return true;
            }
        }
        return false;
    }

    public List<Tile> getSolarPanelPotentialLocations(Player player) {
        List<Tile> potentialLocations = new ArrayList<Tile>();
        Map<String, Integer> ownedTileCounts = new HashMap<>();
        Map<String, Integer> totalTileCounts = new HashMap<>();

        for (Tile ownedTile : player.getOwnedTiles()) {
            String colour = ownedTile.getColour();
            ownedTileCounts.put(colour, ownedTileCounts.getOrDefault(colour, 0) + 1);
        }

        for (Tile gameTile : LoadTileTransaction.gameTiles) {
            String colour = gameTile.getColour();
            totalTileCounts.put(colour, totalTileCounts.getOrDefault(colour, 0) + 1);
        }
        for (Tile ownedTile : player.getOwnedTiles()) {
            String colour = ownedTile.getColour();
            if (ownedTileCounts.get(colour) == totalTileCounts.get(colour) && ownedTile.getPanelsBuildable()) {
                potentialLocations.add(ownedTile);
            }
        }
            return potentialLocations;
        }
    }