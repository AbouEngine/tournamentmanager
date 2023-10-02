package tournamentmanager.core;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tournamentmanager.core.api.*;
import tournamentmanager.core.impl.GameImpl;
import tournamentmanager.core.impl.ParticipantImpl;
import tournamentmanager.core.impl.TournamentTreeBuilderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentTreeBuilderTest {

    List<Participant> participants;
    TournamentTreeBuilder ttb;

    @BeforeEach
    void beforeEach(){
        Participant p1 = new ParticipantImpl("p1");
        Participant p2 = new ParticipantImpl("p2");
        Participant p3 = new ParticipantImpl("p3");
        Participant p4 = new ParticipantImpl("p4");
        Participant p5 = new ParticipantImpl("p5");
        Participant p6 = new ParticipantImpl("p6");
        Participant p7 = new ParticipantImpl("p7");
        Participant p8 = new ParticipantImpl("p8");
        //As specified in the global specification of the software, only tournament with 2 to the nth power entrants are accepted

        participants = new ArrayList<>();

        participants.add(p1);
        participants.add(p2);
        participants.add(p3);
        participants.add(p4);
        participants.add(p5);
        participants.add(p6);
        participants.add(p7);
        participants.add(p8);

        ttb = new TournamentTreeBuilderImpl();
    }

    //Functional method test
    @Test
    void buildInitalRoundProvidesEveryParticipantAGameAndEveryGameDoesNotContainAnyPreviousGames(){
        List<Game> testInitialRound = assertDoesNotThrow(() -> ttb.buildInitialRound(participants));

        List<Participant> allParticipantsInBuiltInitalRound = new ArrayList<>();

        for (Game game: testInitialRound){
            allParticipantsInBuiltInitalRound.addAll(game.getParticipants());

            assertTrue(game.getPreviousGames().isEmpty());
        }

        assertTrue(participants.containsAll(allParticipantsInBuiltInitalRound));
        assertTrue(allParticipantsInBuiltInitalRound.containsAll(participants));
    }

    //Functional method test
    @Test
    void buildNextRoundProperlyBuildsANextRound(){
        List<Game> round = ttb.buildInitialRound(participants);

        List<Game> testNextRound = ttb.buildNextRound(round);

        assertTrue(testNextRound.size() == round.size() / 2);

        for (Game game: testNextRound){
            assertTrue(game.getPreviousGames().size() == 2);
            assertTrue(round.containsAll(game.getPreviousGames()));
        }
    }

    //Functional method test
    @Test
    void buildAllRoundsProperlyBuildsATournamentTree(){
        List<List<Game>> testTournamentTree = assertDoesNotThrow(() -> ttb.buildAllRounds(participants));

        assertEquals(3, testTournamentTree.size());
        assertEquals(4, testTournamentTree.get(0).size());
        assertEquals(2, testTournamentTree.get(1).size());
        assertEquals(1, testTournamentTree.get(2).size());
    }


    //Structural method test
    @Test
    void testBuildNextRoundThrowsErrorWhenThereAreGameDuplicates(){
        TournamentTreeBuilder treeBuilder = new TournamentTreeBuilderImpl();
        Game g1 = new GameImpl();
        Game g2 = new GameImpl();
        ArrayList<Game> games = new ArrayList<>();
        games.add(g1);
        games.add(g1);
        assertThrows(RuntimeException.class, () -> treeBuilder.buildNextRound(games));
    }

    //Structural method test
    @Test
    void testBuildNextRoundProperlyCreatesNextRoundWhenGivenUniqueGames(){
        TournamentTreeBuilder treeBuilder = new TournamentTreeBuilderImpl();
        Game g1 = new GameImpl();
        Game g2 = new GameImpl();
        ArrayList<Game> games = new ArrayList<>();
        List <Game> nextRound = new ArrayList<>();
        games.add(g1);
        games.add(g2);
        nextRound = treeBuilder.buildNextRound(games);
        assertEquals(1, nextRound.size());
    }

    //Structural method test
    @Test
    void testBuildInitialRoundWorksProperlyWithTwoParticipants(){
        Participant p1 = new ParticipantImpl("p1");
        Participant p2 = new ParticipantImpl("p2");

        participants = new ArrayList<>();

        participants.add(p1);
        participants.add(p2);

        List<Game> res = assertDoesNotThrow(() -> ttb.buildInitialRound(participants));
        assertTrue(res.size() == 1);
    }

    //Structural method test
    @Test
    void testBuildInitialRoundThrowsErrorWhenThereAreThreePlayers(){
        Participant p1 = new ParticipantImpl("p1");
        Participant p2 = new ParticipantImpl("p2");
        Participant p3 = new ParticipantImpl("p3");

        participants = new ArrayList<>();

        participants.add(p1);
        participants.add(p2);
        participants.add(p3);

        assertThrows(RuntimeException.class, () -> ttb.buildInitialRound(participants));
    }

    //Structural method test
    @Test
    void testBuildInitialRoundThrowsErrorWhenThereIsOnePlayer(){
        Participant p1 = new ParticipantImpl("p1");

        participants = new ArrayList<>();

        participants.add(p1);

        assertThrows(RuntimeException.class, () -> ttb.buildInitialRound(participants));
    }

    //Structural method test
    @Test
    void testBuildInitialRoundThrowsErrorWhenGivenAnEmptyList(){
        participants = new ArrayList<>();

        List<Game> res = assertDoesNotThrow(() -> ttb.buildInitialRound(participants));
        assertTrue(res.size() == 0);
    }

    //Structural method test
    @Test
    void testBuildNextRoundCreatesEmptyRoundWhenGivenLessThanTwoGames(){
        TournamentTreeBuilder treeBuilder = new TournamentTreeBuilderImpl();
        Game g1 = new GameImpl();
        Game g2 = new GameImpl();
        ArrayList<Game> games = new ArrayList<>();
        List <Game> nextRound = new ArrayList<>();
        games.add(g1);
        nextRound = treeBuilder.buildNextRound(games);
        assertTrue(nextRound.size() == 0);
    }

}
