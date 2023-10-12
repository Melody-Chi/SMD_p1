package src.monster;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.Monster;
import src.MonsterType;
import src.State;

/**
 * A subclass of Monster that represents the Tx5 monster
 */
public class Tx5 extends Monster {
    // Constructor for the Tx5 class.
    public Tx5(Game game, MonsterType type) {
        super(game, type);
        this.stopMoving(5);
    }

    // Overrides the walkApproach method from the Monster class.
    // Tx5 moves towards the Pac-Man using the 4 compass directions and sets the direction accordingly.
    protected void walkApproach() {

        double oldDirection = getDirection();
        Location pacLocation = game.pacActor.getLocation();
        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);

        if (!isVisited(next) && canMove(next)){
            // If the monster is furious, it moves two steps at a time if possible.
            if (state== State.Furious){
                if (canMove(moveForward(next))&&!isVisited(next)){
                    next=moveForward(next);
                }
            }
        } else
        // If it reaches an already visited location or a blocked cell, it randomly walks.
        {
            setDirection(oldDirection);
            next=randomWalk();
        }
        setLocation(next);
        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}
