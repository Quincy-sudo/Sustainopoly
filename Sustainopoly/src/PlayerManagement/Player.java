package PlayerManagement;

import java.util.ArrayList;
import java.util.List;

import TileManagement.Tile;
import TileManagement.LoadTileTransaction;

public class Player {
    private String name;
    private Tile currentTile;
    private double balance;
    private List<Tile> ownedTiles = new ArrayList<Tile>();
    private List<Tile> GovenTiles = new ArrayList<Tile>();

    public Player(String _name, double _balance) {
        name = _name;
        balance = _balance;
        currentTile = LoadTileTransaction.gameTiles.get(0);

    }

    public void printDetails() {
        System.out.println("Name: " + name + " | " + "Balance: £" + balance);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Tile> getOwnedTiles() {
        return ownedTiles;
    }

    public void setOwnedTiles(List<Tile> ownedTiles) {
        this.ownedTiles = ownedTiles;
    }

    public List<Tile> getGovenTiles() {
        return GovenTiles;
    }

    public void setGovenTiles(List<Tile> ownedTiles) {
        this.GovenTiles = ownedTiles;
    }

    public void addTile(Tile tile) {
        ownedTiles.add(tile);
        tile.setBuyable(false);
    }

    public void addGovenTiles(Tile tile) {

        GovenTiles.add(tile); balance = balance - tile.getCost();
          tile.setBuyable(false);

    }

    public void removeTile(Tile tile) {
        ownedTiles.remove(tile);
    }


}