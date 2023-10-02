package tournamentmanager.core.api;

/**
 * Represents a Participant to the Tournament.
 * <p>
 * A Participant can be eliminated, which means she or he has lost a game
 * and cannot play in the Tournament anymore.
 */
public interface Participant {

    /**
     * Retrieve the name of the Player.
     *
     * @return The name of the Player.
     */
    String getName();

    /**
     * Retrieve whether the Player was eliminated from the Tournament (ie. has lost a game).
     * If true, then the player cannot play anymore.
     *
     * @return True if the the Player was eliminated from the Tournament, false otherwise.
     */
    boolean isEliminated();

    /**
     * Sets the player as eliminated.
     * Must be called when a player loses a game.
     */
    void eliminate();

    /**
     * Sets the name of the Participant.
     *
     * @param name The name.
     */
    void setName(String name);
}
