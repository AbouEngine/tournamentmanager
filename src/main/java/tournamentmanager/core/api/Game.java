package tournamentmanager.core.api;

import java.util.List;
import java.util.Optional;

/**
 * Represents a game in a Tournament tree.
 * A Game means that there is a confrontation between two participants.
 * <p>
 * At the beginning, a Game is NOTSTARTED.
 * When the confrontation between two players begins, a Game must be INPROGRESS.
 * Once in progress, points can be given to each player.
 * Once the confrontation is over, the Game must be FINISHED.
 */
public interface Game {

    /**
     * Give or remove points (with a negative value) to one Participant of the Game.
     * <p>
     * Can only be called if the game is INPROGRESS.
     *
     * @param participant The Participant to which points must be given.
     * @param points      The points to add (or  to remove, using a negative value).
     * @throws TournamentException      If the Game is not INPROGRESS.
     * @throws IllegalArgumentException If the given participant is either null or not part of the Game.
     */
    void addPoints(Participant participant, int points) throws TournamentException, IllegalArgumentException;

    /**
     * Adds a previous Game to this Game.
     * <p>
     * A previous Game is a Game from the previous round whose winner will become a participant
     * in the current Game.
     * <p>
     * There can only be two previous Games max for a Game.
     * <p>
     * Can only be used if the Game is NOTSTARTED.
     * Is used to initially construct the Tournament tree.
     *
     * @param game The Game to add as a previous Game.
     * @throws TournamentException      If there are already two previous Games, or if the Game already part of the previous Games,
     *                                  or if the current Game is not NOTSTARTED.
     * @throws IllegalArgumentException If the provided Game is null.
     */
    void addPreviousGame(Game game) throws IllegalArgumentException, TournamentException;

    /**
     * Add a Participant to the Game.
     * There can only be two participants max for a Game.
     *
     * @param participant The participant to add.
     * @throws TournamentException      If there are already two participants or if the participant is already part of this Game.
     * @throws IllegalArgumentException If the provided participant is null.
     */
    void addParticipant(Participant participant) throws TournamentException;

    /**
     * Sets the status to INPROGRESS.
     * <p>
     * Once a game has started, points can be given to players.
     *
     * @throws TournamentException If the game has already started, or if the game does not have two participants.
     */
    void start() throws TournamentException;

    /**
     * Retrieve the previous Games of this Game, ie. the Games whose winners will be participants in the current Game.
     * If this Game is in the first round, returns an empty list.
     *
     * @return The previous Games of this Game.
     */
    List<Game> getPreviousGames();

    /**
     * Set the status to FINISHED.
     * <p>
     * Once finished, a Game cannot be edited anymore,
     * the winner is automatically added as a participant in the following Game.
     * and the loser is set to eliminated from the tournament.
     * <p>
     * Can only be called if the status is INPROGRESS, and if the scores are not ex-aequo.
     *
     * @throws TournamentException If the status is not INPROGRESS, or if the scores are ex-aequo.
     */
    void finish() throws TournamentException;


    /**
     * Retrieve the status of the Game.
     *
     * @return The status of the Game.
     */
    Status getStatus();

    /**
     * Retrieve the participants of the Game (max 2).
     *
     * @return The participants of the Game.
     */
    List<Participant> getParticipants();


    /**
     * Retrieve the winner of the Game.
     * <p>
     * Must be called when there is a winner (for instance if a game is finished).
     *
     * @return The winner of this game.
     * @throws TournamentException If there is no winner yet.
     */
    Participant getWinner() throws TournamentException;

    /**
     * Retrieve the loser of the Game, if any.
     * <p>
     * Must be called when there is a loser (for instance if a game is finished).
     *
     * @return The loser of this Game.
     * @throws TournamentException If there is no loser yet.
     */
    Participant getLoser() throws TournamentException;

    /**
     * Retrieve the following Game, which is the Game where the winner of this Game will go.
     * If this Game is the final Game, then returns an empty Optional.
     *
     * @return The following Game where the winner will go.
     */
    Optional<Game> getFollowingGame();

    /**
     * Sets the following Game of this Game, ie. the Game where the winner of this current Game will go.
     *
     * @param game The Game to set as following Game.
     * @throws IllegalArgumentException If the provided Game is null.
     */
    void setFollowingGame(Game game) throws IllegalArgumentException;

    /**
     * Gets the score of a participant
     * @param participant The participant to retrieve the score of.
     * @return The current score of the participant.
     */
    int getPoints(Participant participant);
    public void setStatus(Status status);
}
