package tournamentmanager.core.api;

/**
 * Represents a Status of either a Game or a Tournament.
 *
 * Typically, it is only possible to change a status from
 * NOTSTARTED to INPROGRESS, and from INPROGRESS to FINISHED.
 */
public enum Status {

    /**
     * Has not started yet.
     */
    NOTSTARTED,

    /**
     * Has started, and is now in progress.
     */
    INPROGRESS,

    /**
     * Has finished, cannot change anymore.
     */
    FINISHED
}
