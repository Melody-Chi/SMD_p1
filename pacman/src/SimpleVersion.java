//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package src;

import ch.aplu.jgamegrid.Location;
import java.util.ArrayList;
import java.util.Properties;
import src.monster.Troll;
import src.monster.Tx5;

/**
 * SimpleVersion is an implementation of GameVersion that represents a simplified
 * version of the game. It defines the necessary methods for setting up monsters and their
 * behaviors for this specific version of the game.
 */
public class SimpleVersion extends GameVersion {

    // Default constructor for SimpleVersion.
    public SimpleVersion() {
    }

    //  Sets up the monsters in the game by adding a Troll and a TX5 to the game world.
    //  The initial locations of the monsters are retrieved from the provided properties.
    public void setMonsters(Game game, Properties properties, ArrayList<Monster> allMonster) {
        Monster troll = new Troll(game, MonsterType.Troll);
        Monster tx5 = new Tx5(game, MonsterType.TX5);
        Location trollLocations = this.parseLocation(properties.getProperty("Troll.location"));
        Location tx5Locations = this.parseLocation(properties.getProperty("TX5.location"));
        allMonster.add(troll);
        allMonster.add(tx5);
        game.addActor(troll, trollLocations, Location.NORTH);
        game.addActor(tx5, tx5Locations, Location.NORTH);
    }

    // A method for implementing gold-eating behavior for monsters in the game.
    public void eatGold(ArrayList<Monster> allMonster) {
    }

    // A method for implementing ice-eating behavior for monsters in the game.
    public void eatIce(ArrayList<Monster> allMonster) {
    }
}
