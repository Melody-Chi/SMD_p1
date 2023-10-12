package src.monster;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import src.Game;
import src.Monster;
import src.MonsterType;
import src.State;

import java.util.*;

/**
 * A subclass of Monster that represents the Orion monster
 */
public class Orion extends Monster {
    // // The location that orion is heading towards.
    private Location targetLocation;
    // ArrayList that contains the current gold pieces that are visible.
    private ArrayList<Actor> currentGold = new ArrayList<>();
    // ArrayList that contains the locations of gold pieces that were already visited by Orion.
    private ArrayList<Location> visitedGold = new ArrayList<>();

    // Constructor for Orion monster.
    public Orion(Game game, MonsterType type) {
        super(game, type);

    }

    // Check if there is any visible gold on the screen.
    private boolean checkGold(){
        currentGold=game.getGoldPieces();
        for (Actor gold : currentGold){
            if(gold.isVisible()){
                return true;
            }
        }
        return false;
    }

    // Select a random target location from the gold pieces that are visible and were not visited yet.
    private void selectRamdonTarget() {
        int sign = (int)randomiser.nextDouble()*10;
        ArrayList<Location> ableLocation=new ArrayList<Location>();
        currentGold = game.getGoldPieces();

        for (Actor gold : currentGold) {
            if (!gold.isVisible() && checkGold()) {
                continue;
            }
            Location goldLocation = gold.getLocation();
            if (visitedGold.contains(goldLocation)) {
                continue;
            }
            ableLocation.add(goldLocation);
        }

        if (ableLocation.isEmpty()){
            visitedGold.clear();
            selectRamdonTarget();
        } else {
            int index=sign%ableLocation.size();
            targetLocation = ableLocation.get(index);
        }
    }

    // Overrides the walkApproach method of the Monster class.
    protected void walkApproach() {
        // If target location is not set, select a random gold piece as target.
        if (targetLocation==null){
            selectRamdonTarget();
        }
        // Create a dictionary to store possible moves and their distances to the target location.
        HashMap<Integer, Integer> dic = new HashMap<Integer, Integer>();
        int sign = randomiser.nextDouble() < 0.5 ? 1 : -1, shortestDis=1000, turnAngle=0;

        double oldDirection = getDirection();
        Location next;

        // Loop through all possible moves and calculate their distances to the target location.
        for (int i=0; i<8; i++) {
            setDirection(oldDirection);
            turn(sign*45*i);
            next = getNextMoveLocation();

            if (canMove(next)&&!isVisited(next)) {
                // If orion is in furious state, move forward and check if can move again.
                if (state== State.Furious) {
                    next=moveForward(next);
                    if(!canMove(next)||isVisited(next)){
                        continue;
                    }
                }
                dic.put(i, next.getDistanceTo(targetLocation));

            }
        }

        // Choose the move with the shortest distance to the target location.
        for (Map.Entry<Integer, Integer> entry : dic.entrySet()){
            if (entry.getValue()<=shortestDis) {
                shortestDis=entry.getValue();
                turnAngle= entry.getKey();

            }
        }

        // Turn and move the monster to the chosen location.
        setDirection(oldDirection);
        turn(sign*45*turnAngle);
        next = getNextMoveLocation();
        if (state==State.Furious){
            next=moveForward(next);
        }
        // If no move is possible, perform a random walk.
        if (dic.size()==0){
            setDirection(oldDirection);
            next=randomWalk();
        }
        // If the target location is reached, add it to visited list and select a new target location.
        if (next.equals(targetLocation)){
            visitedGold.add(targetLocation);
            selectRamdonTarget();

        }
        setLocation(next);
        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);

    }
}
