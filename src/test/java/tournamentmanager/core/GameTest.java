package tournamentmanager.core;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.Status;
import tournamentmanager.core.api.TournamentException;
import tournamentmanager.core.impl.GameImpl;
import tournamentmanager.core.impl.ParticipantImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game g;
    Participant p1;
    Participant p2;

    @BeforeEach
    void beforeEach(){
        g = new GameImpl();
        p1 = new ParticipantImpl("player1");
        p2 = new ParticipantImpl("player2");
    }

    //addPoints
    //Functional method test
    @Test
    void addPointsProperlyAddsPositiveAmountOfPoints() throws TournamentException {

        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        assertDoesNotThrow(() -> g.addPoints(p1, 2));
        assertDoesNotThrow(() -> g.addPoints(p2, 0));

        assertEquals(g.getPoints(p1),
                2);

        assertEquals(g.getPoints(p2),
                0);
    }

    //Functional method test
    @Test
    void addPointsThrowsErrorWhenPlayerIsNull() throws TournamentException {

        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        assertThrows(IllegalArgumentException.class,
                () -> g.addPoints(null, 5) );
        assertThrows(IllegalArgumentException.class,
                () -> g.addPoints(null, -5) );
    }

    //Functional method test
    @Test
    void addPointsProperlyAddsNegativeAmountOfPoints() throws TournamentException {

        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        assertDoesNotThrow(() -> g.addPoints(p1, -2));

        assertEquals(g.getPoints(p1),
                -2);
    }

    //Functional method test
    @Test
    void addPointsThrowsErrorWhenGameIsNotInProgress() throws TournamentException {
        g.addParticipant(p1);

        g.addParticipant(p2);

        assertThrows(TournamentException.class,
                () -> g.addPoints(p1, 5));
        assertThrows(TournamentException.class,
                () -> g.addPoints(p1, -5));
    }

    //addParticipant
    //Functional method test
    @Test
    void addParticipantThrowsIllegalArgumentExceptionWhenParticipantIsNull(){
        assertThrows(IllegalArgumentException.class,
                () -> g.addParticipant(null));
    }

    //Functional method test
    @Test
    void addParticipantProperlyAddsParticipantWhenParticipantsListContainsTwoOrLessParticipantsAndParticipantIsNotNull() throws TournamentException {
        g.addParticipant(p1);

        List<Participant> expected = new ArrayList<>();
        expected.add(p1);

        assertIterableEquals(expected, g.getParticipants());
    }

    //Functional method test
    @Test
    void addParticipantThrowsTournamentExceptionWhenParticipantsListAlreadyContainsTwoOrMoreParticipants() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        Participant p3 = new ParticipantImpl("player3");

        assertThrows(TournamentException.class,
                () -> g.addParticipant(p3));
    }

    //addPreviousGame
    //Functional method test
    @Test
    void addPreviousGameProperlyAddsAPreviousGameWhenItIsNotAlreadyAddedAndPreviousGamesContainsLessThanTwoGames(){
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addPreviousGame(gameToAdd));

        List<Game> expected = new ArrayList<>();
        expected.add(gameToAdd);

        assertIterableEquals(expected, g.getPreviousGames());
    }

    //Functional method test
    @Test
    void addPreviousGameThrowsTournamentExceptionWhenTheGameToAddHasAlreadyBeenAdded(){
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addPreviousGame(gameToAdd));

        assertThrows(TournamentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }

    //Functional method test
    @Test
    void addPreviousGameThrowsTournamentExceptionWhenPreviousGamesAlreadyContainsTwoOrMoreGames(){
        Game fillerGame1 = new GameImpl();
        Game fillerGame2 = new GameImpl();
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addPreviousGame(fillerGame1));
        assertDoesNotThrow(() -> g.addPreviousGame(fillerGame2));

        assertThrows(TournamentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }

    //Functional method test
    @Test
    void addPreviousGameThrowsTournamentExceptionWhenGameIsNotStarted(){
        Game gameToAdd = new GameImpl();

        assertDoesNotThrow(() -> g.addParticipant(p1));
        assertDoesNotThrow(() -> g.addParticipant(p2));

        assertDoesNotThrow(() -> g.start());

        assertThrows(TournamentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }

    //Functional method test
    @Test
    void addPreviousGameThrowsIllegalArgumentExceptionWhenTheGameToAddIsNull(){
        Game gameToAdd = null;

        assertThrows(IllegalArgumentException.class,
                () -> g.addPreviousGame(gameToAdd));
    }
    
    //finish
    //Functional method test
    @Test
    void finishThrowsTournamentExceptionWhenScoresAreEven() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 1);
        g.addPoints(p2, 1);

        assertThrows(TournamentException.class,
                () -> g.finish());
    }

    //Functional method test
    @Test
    void finishThrowsTournamentExceptionWhenGameIsNotInProgress() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        assertThrows(TournamentException.class,
                () -> g.finish());
    }

    //Functional method test
    @Test
    void finishProperlyFinishesTheGame() throws TournamentException {
        Game followingGame = new GameImpl();
        g.setFollowingGame(followingGame);

        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 1);
        g.addPoints(p2, 2);

        assertDoesNotThrow(() -> g.finish());

        assertEquals(Status.FINISHED, g.getStatus());
        assertTrue(g.getFollowingGame().get().getParticipants().contains(p2));
        assertTrue(p1.isEliminated());
    }

    //Functional method test
    //start
    @Test
    void startThrowsTournamentExceptionWhenTheGameIsInProgressAndThereAreTwoParticipants() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        assertDoesNotThrow(() -> g.start());

        assertThrows(TournamentException.class,
                () -> g.start());

    }

    //Functional method test
    @Test
    void startThrowsTournamentExceptionWhenTheGameIsNotStartedAndThereAreLessThanTwoParticipants() throws TournamentException {
        g.addParticipant(p1);

        assertThrows(TournamentException.class,
                () -> g.start());
    }

    //Functional method test
    @Test
    void startSetsTheGameStatusToInProgressWhenThereAreTwoParticipantsAndTheGameIsNotStarted() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        assertDoesNotThrow(() -> g.start());

        assertEquals(Status.INPROGRESS, g.getStatus());
    }

    //setPreviousGame
    //Functional method test
    @Test
    void setFollowingGameThrowsTournamentExceptionWhenFollowingGameIsNull(){
        assertThrows(IllegalArgumentException.class,
                () -> g.setFollowingGame(null));
    }

    //Functional method test
    @Test
    void setFollowingGameProperlyAssociatesGameAndFollowingGame(){
        Game followingGameToAdd = new GameImpl();
        g.setFollowingGame(followingGameToAdd);

        Optional<Game> expected = Optional.of(followingGameToAdd);

        assertEquals(expected,
                g.getFollowingGame());
    }

    //getPreviousGames
    //Functional method test
    @Test
    void getPreviousGamesContainsExactlyAllPreviousGamesOfCurrentGame() throws TournamentException {
        Game previousGameToAdd = new GameImpl();
        g.addPreviousGame(previousGameToAdd);

        List<Game> expected = new ArrayList<>();
        expected.add(previousGameToAdd);

        assertIterableEquals(expected, g.getPreviousGames());
    }
    @Test
    //Functional method test
    void getPreviousGamesReturnsAnEmptyListWhenThereAreNoPreviousGames(){
        assertIterableEquals(new ArrayList<>(), g.getPreviousGames());
    }

    //getWinner
    //Functional method test
    @Test
    void getWinnerReturnsTheWinnerOfAGameWhenThereIsAWinnerAndTheGameIsFinished() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 2);
        g.addPoints(p2, 1);

        g.finish();

        assertEquals(p1, g.getWinner());
    }

    //Functional method test
    @Test
    void getWinnerThrowsTournamentExceptionWhenScoresAreNotEvenButTheGameIsNotFinished() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 2);
        g.addPoints(p2, 1);

        assertThrows(TournamentException.class,
                () -> g.getWinner());
    }

    //Functional method test
    @Test
    void getWinnerThrowsTournamentExceptionWhenScoresAreEvenAndTheGameIsNotFinished() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 2);
        g.addPoints(p2, 2);

        assertThrows(TournamentException.class,
                () -> g.getWinner());
    }

    //getLoser
    //Functional method test
    @Test
    void getLoserReturnsTheLoserOfAGameWhenThereIsALoserAndTheGameIsFinished() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 2);
        g.addPoints(p2, 1);

        g.finish();

        assertEquals(p2, g.getLoser());
    }

    //Functional method test
    @Test
    void getLoserThrowsTournamentExceptionWhenScoresAreNotEvenButTheGameIsNotFinished() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 2);
        g.addPoints(p2, 1);

        assertThrows(TournamentException.class,
                () -> g.getLoser());
    }

    //Functional method test
    @Test
    void getLoserThrowsTournamentExceptionWhenScoresAreEvenAndTheGameIsNotFinished() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 2);
        g.addPoints(p2, 2);

        assertThrows(TournamentException.class,
                () -> g.getLoser());
    }

    //getStatus
    //Functional method test
    @Test
    void getStatusReturnsTheStatusOfTheGame() throws TournamentException {
        assertEquals(Status.NOTSTARTED, g.getStatus());

        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        assertEquals(Status.INPROGRESS, g.getStatus());

        g.addPoints(p1, 2);
        g.addPoints(p2, 1);

        g.finish();

        assertEquals(Status.FINISHED, g.getStatus());
    }

    //getStatus
    //Functional method test
    @Test
    void getParticipantsReturnsExactlyAllTheParticipantsOfTheGame() throws TournamentException {
        List<Participant> expected = new ArrayList<>();

        // compares the content of arrays which are not necessarily ordered in the same way but should contain the same elements
        assertTrue(expected.containsAll(g.getParticipants()) && expected.size() == g.getParticipants().size());
        assertTrue(g.getParticipants().size() <= 2);

        g.addParticipant(p1);
        expected.add(p1);

        assertTrue(expected.containsAll(g.getParticipants()) && expected.size() == g.getParticipants().size());
        assertTrue(g.getParticipants().size() <= 2);

        g.addParticipant(p2);
        expected.add(p2);

        assertTrue(expected.containsAll(g.getParticipants()) && expected.size() == g.getParticipants().size());
        assertTrue(g.getParticipants().size() <= 2);

    }

    //getFollowingGame
    //Functional method test
    @Test
    void getFollowingGameReturnsAnEmptyOptionalIfThereIsNoFollowingGame(){
        Optional<Game> expected = Optional.empty();

        assertEquals(expected, g.getFollowingGame());
    }

    //Functional method test
    @Test
    void getFollowingGameReturnsTheFollowingGame(){
        Game followingGameToAdd = new GameImpl();
        Optional<Game> expected = Optional.of(followingGameToAdd);

        g.setFollowingGame(followingGameToAdd);

        assertEquals(expected, g.getFollowingGame());
    }

    //Functional method test
    @Test
    void addPointsThrowsErrorWhenParticipantDoesNotBelongToGame() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        Participant impostor = new ParticipantImpl("impostor");

        assertThrows(IllegalArgumentException.class, () -> g.addPoints(impostor, 3));
    }

    //Mutation analysis method test
    @Test
    public void getWinnerDoesNotReturnAPlayerIfScoresAreEven() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 1);
        g.addPoints(p2, 1);

        g.setStatus(Status.FINISHED);


        assertEquals(null, g.getWinner());
    }

    //Mutation analysis method test
    @Test
    public void getLoserDoesNotReturnAPlayerIfScoresAreEven() throws TournamentException {
        g.addParticipant(p1);
        g.addParticipant(p2);

        g.start();

        g.addPoints(p1, 1);
        g.addPoints(p2, 1);

        g.setStatus(Status.FINISHED);

        assertEquals(null, g.getLoser());
    }
}
