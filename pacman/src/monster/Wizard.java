package src.monster;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.Monster;
import src.MonsterType;
import src.State;

/**
 * A subclass of Monster that represents the Tx5 monster
 */
public class Wizard extends Monster {
    // Constructor for the Tx5 class.
    public Wizard(Game game, MonsterType type) {
        super(game, type);
    }

    // Overrides the walkApproach method from the Monster class.
    // The Wizard will move in a spiraling pattern around its current location and looking for the player.
    protected void walkApproach() {
        int sign = (int)Math.round(randomiser.nextDouble() *10)+1;

        double oldDirection = getDirection();
        Location next;

        next = getNextMoveLocation();

        for (int i=sign;i<8+sign; i++) {
            setDirection(oldDirection);
            turn(45 * i);
            next = getNextMoveLocation();
            // When it finds a valid direction to move in, it will move in that direction.
            if (canMove(next)){
                // In Furious state, it tries to move forward twice in the same direction if it can't move normally.
                if (state== State.Furious){
                    next=moveForward(next);
                    if (canMove(next)){
                        break;
                    } else {
                        next=moveForward(next);
                        if (canMove(next)&&!isVisited(next)){
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                next=moveForward(next);
                if (canMove(next)){
                    if (state==State.Furious){
                        next=moveForward(next);
                        if (canMove(next)){
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        setLocation(next);
        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}
