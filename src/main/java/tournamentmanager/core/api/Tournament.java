package tournamentmanager.core.api;

import java.util.List;
import java.util.Set;

/**
 * Represents a single-elimination tournament, and can be used to manage both participants and games.
 * <p>
 * A Tournament is a sequence of Rounds, each round containing a set of Games, each Game eliminating one participant.
 * <p>
 * Single-elimination means that a Participant is eliminated from the Tournament as soon as he or she lose one game.
 * <p>
 * A Tournament is managed in three phases (based on the values defined in the Status enumeration):
 * - First, participants must be added to the Tournament.
 * - Second, the Tournament can be started, which creates the game tree, and make it possible to register results.
 * When a game is over, the participant is automatically sent to the next game.
 * - Third, the Tournament can be ended, and the final ranking can be computed.
 */
public interface Tournament {


    /**
     * Add a new participant to the Tournament.
     * Can only be called before the Tournament has started.
     *
     * @param participant The Participant to add.
     * @throws TournamentException If the Tournament has already started.
     */
    void addParticipant(Participant participant) throws TournamentException;

    /**
     * Start the tournament.
     * This will shuffle the list of participantts, create the complete tournament tree,
     * and assign participants to their first games in the first round.
     * <p>
     * Can only be called if the Tournament has not started, and if the number of participants is more than 1,
     * and if the number of participants is a power of two. Cannot be undone.
     *
     * @throws TournamentException If the tournament has already started, or if there are strictly less than 2 participants,
     *                             or if the number of participants is not a power of two.
     */
    void start(TournamentTreeBuilder tournamentTreeBuilder) throws TournamentException;


    /**
     * End the tournament.
     * Once a tournament has ended, no new results can be registered.
     * <p>
     * Can only be called if the Tournament has already started, and if all games have ended.
     *
     * @throws TournamentException If the tournament has not started, or if some games have not ended yet.
     */
    void end() throws TournamentException;

    /**
     * Retrieve all the games of the Tournament tree.
     *
     * @return All the games of the Tournament tree.
     */
    List<Game> getAllGames();

    /**
     * Retrieve all rounds of the Tournament. A round is simply a list of Games that take place in parallel.
     *
     * @return The list of rounds.
     */
    List<List<Game>> getRounds();

    /**
     * Retrieve all Games of the tournament tree that are ready to start.
     * A Game is ready to start if it has two participants and if it has neither started or ended.
     *
     * @return All Games of the tournament tree that are ready to start.
     */
    List<Game> getGamesReadyToStart();

    /**
     * Retrieve all Games of the tournament tree that are finished.
     *
     * @return All Games of the tournament tree that are finished.
     */
    List<Game> getFinishedGames();

    /**
     * Retrieve all games of the tournament tree that are in progress.
     *
     * @return All games of the tournament tree that are in progress.
     */
    List<Game> getGamesInProgress();

    /**
     * Retrieve all games of the tournament tree that are not ready to start and that will be played in the future.
     *
     * @return All future games of the tournament tree.
     */
    List<Game> getFutureGames();

    /**
     * Compute the final ranking based on the Tournament results, and return it.
     * <p>
     * The computed ranking is provided as an *ordered partition*, which is essentially
     * a list of sets. Each set contains one of several Participants that are ex-æquo.
     * The list of set gives the ranking among sets of Participants.
     * <p>
     * Two participants are ex-æquo if they were eliminated in the same round.
     * <p>
     * Can only be called after the Tournament has ended.
     *
     * @return The final ranking.
     */
    List<Set<Participant>> computeFinalRanking() throws TournamentException;

    /**
     * Retrieve the Status of the Tournament.
     * It can either be:
     * - NOTSTARTED when adding players,
     * - INPROGRESS when playing games and registering results,
     * - FINISHED when no more games can be played and the final ranking can be computed.
     *
     * @return The current Status of the Tournament.
     */
    Status getStatus();


}
