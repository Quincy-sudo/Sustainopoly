package TileManagement;

import PlayerManagement.Player;

public class Tile implements TileInterface {

    public String Colour;
    public String name;
    public double cost;
    public double rent;
    public double cost_of_PV_Panels;
    public double rent_with_PV_Panels;
    public boolean buyable;
    public Player owner;
    public String tileType;
    public boolean Building_Buildable;


    public Tile(String _Colour, String _name, double _cost, double _rent, double _costPanel, double _rentPanel,
                String _type, boolean _isBuy) {
        Colour = _Colour;
        name = _name;
        cost = _cost;
        rent = _rent;
        cost_of_PV_Panels = _costPanel;
        rent_with_PV_Panels = _rentPanel;
        tileType = _type;
        buyable = _isBuy;
        Building_Buildable = true;
    }


    public void printDetails() {
        if (Colour != "N/A") {
            System.out.println("Goven Building: " + name + "\nColour: " + Colour );
        } else {
            System.out.println("Tile Name: " + name);
        }
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String Colour) {
        this.Colour = Colour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getCostOfPanels() {
        return cost_of_PV_Panels;
    }

    public void setCostOfPanels(double cost) {
        this.cost = cost;
    }

    public double getRentWithPanels() {
        return rent_with_PV_Panels;
    }

    public void setRentWithPanels(double rent) {
        this.rent = rent;
    }

    public boolean isBuyable() {
        return buyable;
    }

    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
    }

    public String getTileType() {
        return tileType;
    }

    public void setPanelsBuildable(boolean isBuildable) {
        this.Building_Buildable = isBuildable;
    }

    public boolean getPanelsBuildable() {
        return Building_Buildable;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
