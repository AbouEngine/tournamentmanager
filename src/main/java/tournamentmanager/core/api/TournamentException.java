package tournamentmanager.core.api;

/**
 * Represents an error happening in the execution of the Tournament.
 * This error can be of any kind.
 */
public class TournamentException extends Exception {
    public TournamentException(String message) {
        super(message);
    }
}
