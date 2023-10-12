package src.monster;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.Monster;
import src.MonsterType;
import src.State;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an Alien monster in the PacMan game.
 * It extends the abstract class Monster and implements.
 */
public class Alien extends Monster {
    public Alien(Game game, MonsterType type) {
        super(game, type);
    }

    // This method controls Alien's behavior. Alien can move towards PacMan, but only make 45-degree turns.
    // It uses a HashMap to calculate the distance between the Alien and PacMan.
    // Then updates location and notifies the GameCallback object.
    protected void walkApproach() {
        HashMap<Integer, Integer> dic = new HashMap<Integer, Integer>();
        int sign = randomiser.nextDouble() < 0.5 ? 1 : -1, shortestDis=1000, turnAngle=0;
        double oldDirection = getDirection();
        Location next;
        Location pacLocation = game.pacActor.getLocation();
        for (int i=0;i<8; i++) {
            setDirection(oldDirection);
            turn(sign*45*i);
            next = getNextMoveLocation();

            // The Alien can move through walls when it is in a furious state.
            if (canMove(next)) {
                if (state== State.Furious) {
                    next=moveForward(next);
                    if(!canMove(next)){
                        continue;
                    }
                }
                dic.put(i, next.getDistanceTo(pacLocation));
            }
        }

        for (Map.Entry<Integer, Integer> entry : dic.entrySet()){
            if (entry.getValue()<=shortestDis) {
                shortestDis=entry.getValue();
                turnAngle= entry.getKey();
            }
        }
        setDirection(oldDirection);
        turn(sign*45*turnAngle);
        next = getNextMoveLocation();
        if (state==State.Furious){
            next=moveForward(next);
        }
        setLocation(next);
        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}
