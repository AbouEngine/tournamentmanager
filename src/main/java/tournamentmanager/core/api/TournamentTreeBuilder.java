package tournamentmanager.core.api;

import java.util.List;


/**
 * Set of services to build the Tournament tree from a list of participants.
 * <p>
 * A Tournament tree is composed of a list of rounds, each round composed of a list of games.
 * <p>
 * Except for the final Game, each Game is connected to a Game of the next round.
 * When a participant wins in a Game, he or she goes to the following Game in the next round.
 */
public interface TournamentTreeBuilder {

    /**
     * Build a complete Tournament tree for all rounds of the Tournament, using a list of participants.
     * <p>
     * A Tournament tree is simply a list of rounds. Since a round is a list of
     * Games, a Tournament tree is a list of lists of Games.
     * <p>
     * See buildInitialRound() regarding how the first round is built, and
     * buildNextRound() regarding how all other round is built.
     *
     * @param participants The list with all participants.
     * @return The complete tournament tree.
     */
    List<List<Game>> buildAllRounds(List<Participant> participants);

    /**
     * Build the first round of a tournament tree. This first round is special because it is initially filled with all participants.
     *
     * @param participants The list with all participants.
     * @return The initial round of the tournament tree.
     */
    List<Game> buildInitialRound(List<Participant> participants);

    /**
     * Given an already created round, build the next round of a tournament tree.
     * <p>
     * Each Game of this next round is connected to two Games from the previous round.
     *
     * @param previousRound The round that precedes this newly created round.
     * @return The round that follows the given previous round.
     */
    List<Game> buildNextRound(List<? extends Game> previousRound);


}
