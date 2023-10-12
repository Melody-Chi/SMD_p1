package src.monster;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.Monster;
import src.MonsterType;

/**
 * A subclass of Monster that represents the Troll monster
 */
public class Troll extends Monster {

    // Constructor for the Troll class.
    public Troll(Game game, MonsterType type) {
        super(game, type);
    }

    // Overrides the walkApproach method in the Monster class.
    // It makes the Troll move randomly in the game.
    protected void walkApproach() {
        Location next=randomWalk();
        setLocation(next);
        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}