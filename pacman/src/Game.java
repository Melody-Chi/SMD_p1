// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.monster.*;
import src.utility.GameCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The Game class represents the PacMan game and extends the GameGrid class.
 * It initializes the game and contains the game logic.
 * It implements a constructor that takes a GameCallback object and a Properties object as parameters.
 */
public class Game extends GameGrid
{
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);
  public PacActor pacActor = new PacActor(this);
  private ArrayList<Monster> allMonster = new ArrayList<Monster>();
  private GameVersion version;
  private ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();
  private ArrayList<Actor> iceCubes = new ArrayList<Actor>();
  private ArrayList<Actor> goldPieces = new ArrayList<Actor>();
  private GameCallback gameCallback;
  private Properties properties;
  private int seed = 30006;
  private ArrayList<Location> propertyPillLocations = new ArrayList<>();
  private ArrayList<Location> propertyGoldLocations = new ArrayList<>();

  // Constructor for the Game class.
  public Game(GameCallback gameCallback, Properties properties)
  {
    //Setup game
    super(nbHorzCells, nbVertCells, 20, false);
    this.gameCallback = gameCallback;
    this.properties = properties;
    setSimulationPeriod(100);
    setTitle("[PacMan in the Multiverse]");

    //Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
    loadPillAndItemsLocations();

    if (properties.getProperty("version").equals("simple")){
      version=new SimpleVersion();
    } else if (properties.getProperty("version").equals("multiverse")){
      version=new MultiverseVersion();
    }

    GGBackground bg = getBg();
    drawGrid(bg);
    version.setMonsters(this, properties, allMonster);


    //Setup Random seeds
    seed = Integer.parseInt(properties.getProperty("seed"));
    setSeedSlow(seed, 3);
    addKeyRepeatListener(pacActor);
    setKeyRepeatPeriod(150);
    setupPlayer();


    //Run the game
    doRun();
    show();

    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    boolean hasPacmanBeenHit;
    boolean hasPacmanEatAllPills;
    setupPillAndItemsLocations();
    int maxPillsAndItems = countPillsAndItems();
    do {
      hasPacmanBeenHit=false;
      for (Monster monster : allMonster) {
        if (monster.getLocation().equals(pacActor.getLocation())){
          hasPacmanBeenHit=true;
          break;
        }
      }

      hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
      delay(10);
    } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
    delay(120);

    Location loc = pacActor.getLocation();

    // Stop all monsters from moving.
    for (Monster monster: allMonster){
      monster.setStopMoving(true);
    }

    // Remove PacMan from the game.
    pacActor.removeSelf();

    String title = "";
    if (hasPacmanBeenHit) {
      bg.setPaintColor(Color.red);
      title = "GAME OVER";
      addActor(new Actor("sprites/explosion3.gif"), loc);
    } else if (hasPacmanEatAllPills) {
      bg.setPaintColor(Color.yellow);
      title = "YOU WIN";
    }
    setTitle(title);
    gameCallback.endOfGame(title);

    doPause();
  }


  // Set the random seed for all actors in the game.
  private void setSeedSlow(int seed, int slow){
    pacActor.setSeed(seed);
    pacActor.setSlowDown(3);
    for (Monster monster: allMonster){
      monster.setSeed(seed);
      monster.setSlowDown(slow);
    }

  }

  public GameCallback getGameCallback() {
    return gameCallback;
  }

  // Parse location from a string in the format "x,y".
  private Location parseLocation(String locationString) {
    String[] locationParts = locationString.split(",");
    int x = Integer.parseInt(locationParts[0]);
    int y = Integer.parseInt(locationParts[1]);
    return new Location(x, y);
  }

  // Add PacMan to the game at a given location.
  private void setupPlayer(){
    Location pacManLocations = parseLocation(this.properties.getProperty("PacMan.location"));
    addActor(pacActor, pacManLocations);
  }

  // Count the number of pills and gold pieces on the game grid.
  private int countPillsAndItems() {
    int pillsAndItemsCount = 0;
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          pillsAndItemsCount++;
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          pillsAndItemsCount++;
        }
      }
    }
    if (propertyPillLocations.size() != 0) {
      pillsAndItemsCount += propertyPillLocations.size();
    }

    if (propertyGoldLocations.size() != 0) {
      pillsAndItemsCount += propertyGoldLocations.size();
    }

    return pillsAndItemsCount;
  }

  // Get a list of all pill and gold piece locations on the game grid.
  public ArrayList<Location> getPillAndItemLocations() {
    return pillAndItemLocations;
  }

  // This method loads the locations of pills and gold from the properties file.
  private void loadPillAndItemsLocations() {

    String pillsLocationString = properties.getProperty("Pills.location");
    if (pillsLocationString != null) {
      String[] singlePillLocationStrings = pillsLocationString.split(";");
      for (String singlePillLocationString: singlePillLocationStrings) {
        String[] locationStrings = singlePillLocationString.split(",");
        propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }

    String goldLocationString = properties.getProperty("Gold.location");
    if (goldLocationString != null) {
      String[] singleGoldLocationStrings = goldLocationString.split(";");
      for (String singleGoldLocationString: singleGoldLocationStrings) {
        String[] locationStrings = singleGoldLocationString.split(",");
        propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }
  }

  // This method sets up the locations of pills and items.
  private void setupPillAndItemsLocations() {
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 3 &&  propertyGoldLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 4) {
          pillAndItemLocations.add(location);
        }
      }
    }
    if (propertyPillLocations.size() > 0) {
      for (Location location : propertyPillLocations) {
        pillAndItemLocations.add(location);
      }
    }
    if (propertyGoldLocations.size() > 0) {
      for (Location location : propertyGoldLocations) {
        pillAndItemLocations.add(location);
      }
    }
  }

  // This method draws the game grid.
  private void drawGrid(GGBackground bg)
  {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a > 0)
          bg.fillCell(location, Color.lightGray);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          putPill(bg, location);
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          putGold(bg, location);
        } else if (a == 4) {
          putIce(bg, location);
        }
      }
    }

    for (Location location : propertyPillLocations) {
      putPill(bg, location);
    }

    for (Location location : propertyGoldLocations) {
      putGold(bg, location);
    }
  }

  // This method puts a pill at the specified location on the game grid.
  private void putPill(GGBackground bg, Location location){
    bg.fillCircle(toPoint(location), 5);
  }

  // This method puts a gold piece at the specified location on the game grid.
  private void putGold(GGBackground bg, Location location){
    bg.setPaintColor(Color.yellow);
    bg.fillCircle(toPoint(location), 5);
    Actor gold = new Actor("sprites/gold.png");
    this.goldPieces.add(gold);
    addActor(gold, location);
  }

  // This method puts an ice piece at the specified location on the game grid.
  private void putIce(GGBackground bg, Location location){
    bg.setPaintColor(Color.blue);
    bg.fillCircle(toPoint(location), 5);
    Actor ice = new Actor("sprites/ice.png");
    this.iceCubes.add(ice);
    addActor(ice, location);
  }

  // This method removes an item from the game, based on its type and location.
  public void removeItem(String type,Location location){

    // If the item is gold, the version instance's eatGold method is called to update the game state.
    if(type.equals("gold")){
      version.eatGold(allMonster);

      for (Actor item : this.goldPieces){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }

    // If the item is ice, the version instance's eatIce method is called to update the game state.
    }else if(type.equals("ice")){
      version.eatIce(allMonster);

      for (Actor item : this.iceCubes){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }
  }

  // This method returns the list of gold pieces.
  public ArrayList<Actor> getGoldPieces() {
    return goldPieces;
  }

  // This method returns the number of horizontal cells in the game grid.
  public int getNumHorzCells(){
    return this.nbHorzCells;
  }

  // This method returns the number of vertical cells in the game grid.
  public int getNumVertCells(){
    return this.nbVertCells;
  }
}