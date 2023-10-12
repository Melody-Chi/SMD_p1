// Monster.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.util.*;

/**
 * This abstract class represents a Monster in the game and extends the Actor class.
 */
public abstract class Monster extends Actor
{
  protected Game game;
  protected MonsterType type;
  protected ArrayList<Location> visitedList = new ArrayList<Location>();
  protected final int listLength = 10;
  protected boolean stopMoving = false;
  protected int seed = 0;
  protected Random randomiser = new Random(0);
  protected State state;

  // Constructor for the Monster class.
  public Monster(Game game, MonsterType type)
  {
    super("sprites/" + type.getImageName());
    this.game = game;
    this.type = type;
    this.state=State.Normal;
  }

  // This method stops the monster from moving for a certain number of seconds.
  public void stopMoving(int seconds) {
    this.stopMoving = true;
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.stopMoving = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  // This method changes the state of the monster to either Freeze or Furious.
  public void changeState(State state) {
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    if (state==State.Freeze) {
      this.state=State.Freeze;
      this.stopMoving(3);
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          monster.state = State.Normal;
        }
      }, 3 * SECOND_TO_MILLISECONDS);
    } else if (state==State.Furious) {
      if (this.state!=State.Freeze) {
        this.state=state;
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            monster.state = State.Normal;
          }
        }, 3 * SECOND_TO_MILLISECONDS);
      }
    }
  }

  // This method sets the seed for the monster's random number generator.
  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  // This method sets the stopMoving flag for the monster.
  public void setStopMoving(boolean stopMoving) {
    this.stopMoving = stopMoving;
  }

  // This method is called on each frame of the game and contains the main logic for the monster's actions.
  public void act()
  {
    if (stopMoving) {
      return;
    }
    walkApproach();
    if (getDirection() > 150 && getDirection() < 210)
      setHorzMirror(false);
    else
      setHorzMirror(true);
  }

  // This method moves the monster forward and returns the new location.
  protected Location moveForward (Location next) {
    Location forward;
    double direction=getLocation().getDirectionTo(next);
    forward=next.getNeighbourLocation(direction);
    return forward;
  }

  // This method generates a random movement for the monster and returns the new location.
  protected Location randomWalk(){

    double oldDirection = getDirection();

    Location next;

    int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
    int[] turnList={1,0,-1,2};
    next = getNextMoveLocation();
    for (int i:turnList) {
      setDirection(oldDirection);
      turn(i*sign * 90);
      next = getNextMoveLocation();
      if (canMove(next)) {
        if (state == State.Furious) {
          next = moveForward(next);
          if (!canMove(next)) {
            continue;
          }
        }
        return next;
      }
    }
    return next;
  }

  // This method contains the main logic for the monster's movement and is implemented by the subclasses.
  protected abstract void walkApproach();

  // This method gets the type of the monster.
  public MonsterType getType() {
    return type;
  }

  // This method adds a location to the monster's visited list.
  protected void addVisitedList(Location location)
  {
    visitedList.add(location);
    if (visitedList.size() == listLength)
      visitedList.remove(0);
  }

  // This method checks whether the location has been visited.
  protected boolean isVisited(Location location)
  {
    for (Location loc : visitedList)
      if (loc.equals(location))
        return true;
    return false;
  }

  // This method checks whether the monster gain enough conditions to move.
  protected boolean canMove(Location location)
  {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
          || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0)
      return false;
    else
      return true;
  }
}
