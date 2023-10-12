//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package src;

import ch.aplu.jgamegrid.Location;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This abstract class serves all versions of this game.
 * It declares three abstract methods that must be implemented by the concrete game version.
 */
public abstract class GameVersion {
    public GameVersion() {
    }

    // Parses a location string into a Location object.
    protected Location parseLocation(String locationString) {
        String[] locationParts = locationString.split(",");
        int x = Integer.parseInt(locationParts[0]);
        int y = Integer.parseInt(locationParts[1]);
        return new Location(x, y);
    }

    // Abstract method to set the monsters for the game.
    public abstract void setMonsters(Game var1, Properties var2, ArrayList<Monster> var3);

    // Abstract method for the pac actor to eat gold.
    public abstract void eatGold(ArrayList<Monster> var1);

    // Abstract method for the pac actor to eat ice.
    public abstract void eatIce(ArrayList<Monster> var1);
}
