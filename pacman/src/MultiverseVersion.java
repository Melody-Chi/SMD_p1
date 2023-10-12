//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package src;

import ch.aplu.jgamegrid.Location;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import src.monster.Alien;
import src.monster.Orion;
import src.monster.Troll;
import src.monster.Tx5;
import src.monster.Wizard;

/**
 * This class extends the GameVersion class and
 * represents the multiverse version of the game with monsters that can change state based.
 */
public class MultiverseVersion extends GameVersion {
    public MultiverseVersion() {
    }

    // This method sets up the monsters for the game by creating instances of
    // different Monster subclasses and adding them to the allMonster list.
    public void setMonsters(Game game, Properties properties, ArrayList<Monster> allMonster) {
        // Create instances of different monster subclasses.
        Monster troll = new Troll(game, MonsterType.Troll);
        Monster tx5 = new Tx5(game, MonsterType.TX5);
        Monster alien = new Alien(game, MonsterType.Alien);
        Monster wizard = new Wizard(game, MonsterType.Wizard);
        Monster orion = new Orion(game, MonsterType.Orion);

        // Parse location properties for each monster.
        Location trollLocations = this.parseLocation(properties.getProperty("Troll.location"));
        Location tx5Locations = this.parseLocation(properties.getProperty("TX5.location"));
        Location alienLocations = this.parseLocation(properties.getProperty("Alien.location"));
        Location orionLocations = this.parseLocation(properties.getProperty("Orion.location"));
        Location wizardLocations = this.parseLocation(properties.getProperty("Wizard.location"));

        // Add monsters to the list of all monsters.
        allMonster.add(troll);
        allMonster.add(tx5);
        allMonster.add(alien);
        allMonster.add(wizard);
        allMonster.add(orion);

        // Add monsters to the game.
        game.addActor(troll, trollLocations, Location.NORTH);
        game.addActor(tx5, tx5Locations, Location.NORTH);
        game.addActor(alien, alienLocations, Location.NORTH);
        game.addActor(orion, orionLocations, Location.NORTH);
        game.addActor(wizard, wizardLocations, Location.NORTH);
    }

    // This method changes the state to Furious of all monsters.
    public void eatGold(ArrayList<Monster> allMonster) {

        for (Monster monster: allMonster){
            monster.changeState(State.Furious);
        }
    }

    // This method changes the state to Freeze of all monsters.
    public void eatIce(ArrayList<Monster> allMonster) {
        for (Monster monster: allMonster){
            monster.changeState(State.Freeze);
        }
    }
}