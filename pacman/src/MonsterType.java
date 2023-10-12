package src;

/**
 * This enum represents the types of monsters in the game.
 */
public enum MonsterType {
    Troll,
    TX5,
    Alien,
    Wizard,
    Orion;

    // This method returns the image name for the corresponding monster type.
    public String getImageName() {
        switch (this) {
            case Troll: return "m_troll.gif";
            case TX5: return "m_tx5.gif";
            case Alien: return "m_alien.gif";
            case Wizard: return "m_wizard.gif";
            case Orion: return "m_orion.gif";
            default: {
                assert false;
            }
        }
        return null;
    }
}
