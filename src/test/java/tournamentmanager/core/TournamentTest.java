package tournamentmanager.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tournamentmanager.core.api.*;
import tournamentmanager.core.impl.ParticipantImpl;
import tournamentmanager.core.impl.TournamentImpl;
import tournamentmanager.core.impl.TournamentTreeBuilderImpl;

import java.util.*;
import java.util.stream.Collectors;

public class TournamentTest {

	Tournament t;
	Participant p1;
	Participant p2;
	Participant p3;
	Participant p4;
	Participant p5;

	@BeforeEach
	void beforeEach() {
		t = new TournamentImpl();
		p1 = new ParticipantImpl("player1");
		p2 = new ParticipantImpl("player2");
		p3 = new ParticipantImpl("player3");
		p4 = new ParticipantImpl("player4");
	}

	
	//addParticipant()
	//Functional method test
	@Test
	public void testAddParticipantGameNotStarted() {
		assertDoesNotThrow(() -> {
			t.addParticipant(p1);
		});
	}

	//Functional method test
	@Test
	public void testAddParticipantGameHasStarted() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.start(new TournamentTreeBuilderImpl());
		assertThrows(TournamentException.class, () -> {
			t.addParticipant(p3);
		});
	}

	//Functional method test
	@Test
	public void testAddParticipantGameHasEnded() throws TournamentException {
		TournamentTreeBuilder fakeTtb = mock(TournamentTreeBuilder.class);
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.start(fakeTtb);
		t.end();
		assertThrows(TournamentException.class, () -> {
			t.addParticipant(p3);
		});
	}

	//Functional method test
	@Test
	public void testAddNullParticipant() {
		assertDoesNotThrow(() -> {
			p5 = null;
			t.addParticipant(p5);
		});
	}

	//Functional method test
	@Test
	public void testAddSameParticipantMultipleTimes() throws TournamentException {

		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p2);
		t.addParticipant(p3);
		assertThrows(TournamentException.class, () -> {
			t.start(new TournamentTreeBuilderImpl());
		});
	}

	// start()
	//Functional method test
	@Test
	public void testStartWithLessThanTwoParticipants() throws TournamentException {

		t.addParticipant(p1);
		assertThrows(TournamentException.class, () -> {
			t.start(new TournamentTreeBuilderImpl());
		});
	}

	//Functional method test
	@Test
	public void testStartNumberOfParticipantsNotPowerOfTwo() throws TournamentException {

		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		assertThrows(TournamentException.class, () -> {
			t.start(new TournamentTreeBuilderImpl());
		});
	}

	//Functional method test
	@Test
	public void testStartWithTwoOrMoreParticipantsInPowerOfTwo() throws TournamentException {

		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);
		assertDoesNotThrow(() -> {
			t.start(new TournamentTreeBuilderImpl());
		});
	}

	//Functional method test
	@Test
	public void testStartWhenTournamentInProgress() throws TournamentException {

		t.addParticipant(p1);
		t.addParticipant(p2);
		t.start(new TournamentTreeBuilderImpl());
		assertThrows(TournamentException.class, () -> {
			t.start(new TournamentTreeBuilderImpl());
		});
	}

	//Functional method test
	@Test
	public void testStartWhenTournamentFinished() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		TournamentTreeBuilder fakeTtb = mock(TournamentTreeBuilder.class);

		t.start(fakeTtb);
		t.end();
		assertThrows(TournamentException.class, () -> {
			t.start(fakeTtb);
		});
	}
	
	//end()
	//Functional method test
	@Test
    public void testEndTournamentThatHasNotStarted() throws TournamentException{
    	t.addParticipant(p1);
		t.addParticipant(p2);
    	assertThrows(TournamentException.class, () -> t.end());
    }

	//Functional method test
	@Test
    public void testEndTournamentInProgress() throws TournamentException{
    	t.addParticipant(p1);
		t.addParticipant(p2);
		t.start(new TournamentTreeBuilderImpl());
    	assertThrows(TournamentException.class, () -> t.end());
    }

	//Functional method test
	@Test
    public void testEndTournamentWithGamesFinished() throws TournamentException{
    	t.addParticipant(p1);
		t.addParticipant(p2);
		List<List<Game>> returnList = new ArrayList<>();
		List<Participant> participantsList = new ArrayList<>();
		participantsList.add(p3);
		participantsList.add(p4);
		TournamentTreeBuilder fakeTtb = mock(TournamentTreeBuilder.class);
		t.start(fakeTtb);
		Game fakeGame = mock(Game.class);
		when(fakeGame.getStatus()).thenReturn(Status.FINISHED);

		List<Game> fakeRound = new ArrayList<>();
		fakeRound.add(fakeGame);
		returnList.add(fakeRound);

		when(fakeTtb.buildAllRounds(participantsList)).thenReturn(returnList);
		
		assertDoesNotThrow(() -> t.end());
    }

	// getAllGames()
	//Functional method test
	@Test
	void testGetAllGamesReturnsAllGames() throws TournamentException{
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());

		List<Game> games = t.getRounds().stream().flatMap(List::stream).collect(Collectors.toList());

		assertTrue(games.containsAll(t.getAllGames()));
		assertTrue(t.getAllGames().containsAll(games));
	}


	// getRounds()
	//Functional method test
	@Test
	void testGetRoundsRetrieveAllRoundsProperly() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());

		assertTrue(t.getRounds().size() == 2);
		assertTrue(t.getRounds().get(0).size() == 2);
		assertTrue(t.getRounds().get(1).size() == 1);
	}

	// getGamesReadyToStart()
	//Functional method test
	@Test
    public void testGamesReadyToStart() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());

		Game gameToStart = t.getRounds().get(0).get(0);
		Game expectedReadyToStartGame = t.getRounds().get(0).get(1);
		Game gameWaitingForSecondParticipant = t.getRounds().get(1).get(0);

		gameToStart.start();
		gameToStart.addPoints(gameToStart.getParticipants().get(0), 2);
		gameToStart.finish();

		assertTrue(t.getGamesReadyToStart().contains(expectedReadyToStartGame));
		assertTrue(t.getGamesReadyToStart().size() == 1);


	}

	// getFinishedGames()
	//Functional method test
	@Test
    public void testGetFinishedGames() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());

		Game expectedFinishedGame = t.getRounds().get(0).get(0);
		Game expectedInProgressGame = t.getRounds().get(0).get(1);
		Game gameWaitingForSecondParticipant = t.getRounds().get(1).get(0);

		expectedFinishedGame.start();
		expectedFinishedGame.addPoints(expectedFinishedGame.getParticipants().get(0), 2);
		expectedFinishedGame.finish();

		expectedInProgressGame.start();

		assertTrue(t.getFinishedGames().contains(expectedFinishedGame));
		assertEquals(1, t.getGamesInProgress().size());
    }

	// getGamesInProgress()
	//Functional method test
	@Test
	void testGetGamesInProgressRetrivesGamesInProgress() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());

		Game gameToStart = t.getRounds().get(0).get(0);
		Game expectedInProgressGame = t.getRounds().get(0).get(1);
		Game gameWaitingForSecondParticipant = t.getRounds().get(1).get(0);

		gameToStart.start();
		gameToStart.addPoints(gameToStart.getParticipants().get(0), 2);
		gameToStart.finish();

		expectedInProgressGame.start();

		assertTrue(t.getGamesInProgress().contains(expectedInProgressGame));
		assertTrue(t.getGamesInProgress().size() == 1);
	}

	// getFutureGames()
	//Functional method test
	@Test
	void  testGetFutureGamesRetrievesFutureGamesProperly() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());

		Game gameToStart = t.getRounds().get(0).get(0);
		Game expectedReadyToStartGame = t.getRounds().get(0).get(1);
		Game gameWaitingForSecondParticipant = t.getRounds().get(1).get(0);

		gameToStart.start();
		gameToStart.addPoints(gameToStart.getParticipants().get(0), 2);
		gameToStart.finish();

		assertTrue(t.getFutureGames().contains(expectedReadyToStartGame));
		assertTrue(t.getFutureGames().contains(gameWaitingForSecondParticipant));
		assertEquals(2, t.getFutureGames().size());
	}
	
	// computeFinalRankings()
	//Functional method test
	@Test
	void testComputeFinalRankingsThrowsTournamentExceptionWhenItIsNotFinished() {
		assertDoesNotThrow(() -> t.addParticipant(p1));
		assertDoesNotThrow(() -> t.addParticipant(p2));

		assertDoesNotThrow(() -> t.start(new TournamentTreeBuilderImpl()));

		assertThrows(TournamentException.class, () -> t.computeFinalRanking());
	}

	//Functional method test
	@Test
	void testComputeFinalRankingsReturnsTheCorrectRankings() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());

		List<Set<Participant>> expectedRankings = new ArrayList<>();

		while (!t.getGamesReadyToStart().isEmpty()) {

			Set<Participant> rank = new HashSet<>();

			List<Game> gamesReadyToStart = t.getGamesReadyToStart();

			for (Game game : t.getGamesReadyToStart()) {
				game.start();

				Participant winner = game.getParticipants().get(0);

				game.addPoints(winner, 2);

				game.finish();

				rank.add(game.getParticipants().get(1));
			}

			expectedRankings.add(0, rank);

			if (gamesReadyToStart.size() == 1) {
				Participant winner = gamesReadyToStart.get(0).getParticipants().get(0);

				Set<Participant> firstRank = Set.of(winner);

				expectedRankings.add(0, firstRank);
			}

		}

		t.end();

		List<Set<Participant>> result = t.computeFinalRanking();

		for (int i = 0; i < result.size(); i++) {
			assertTrue(expectedRankings.get(i).containsAll(result.get(i)));
			assertTrue(result.get(i).containsAll(expectedRankings.get(i)));
		}
	}

	// getStatus()
	//Functional method test
	@Test
	public void testStatusWhenTournamentHasNotStarted() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		assertEquals(t.getStatus(), Status.NOTSTARTED);
	}

	//Functional method test
	@Test
	public void testStatusWhenTournamentHasStarted() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.start(new TournamentTreeBuilderImpl());
		assertEquals(t.getStatus(), Status.INPROGRESS);
	}

	//Functional method test
	@Test
	public void testStatusWhenTournamentHasEnded() throws TournamentException {
		TournamentTreeBuilder fakeTtb = mock(TournamentTreeBuilder.class);
		
		assertDoesNotThrow(() -> t.addParticipant(p1));
		assertDoesNotThrow(() -> t.addParticipant(p2));

		assertDoesNotThrow(() -> t.start(fakeTtb));
		assertDoesNotThrow(() -> t.end());
    	assertEquals(t.getStatus(),Status.FINISHED);
	}

	//Mutation analysis method test
	@Test
	public void testComputeFinalRankingsIsNotEmpty() throws TournamentException {
		t.addParticipant(p1);
		t.addParticipant(p2);
		t.addParticipant(p3);
		t.addParticipant(p4);

		t.start(new TournamentTreeBuilderImpl());


		while (!t.getGamesReadyToStart().isEmpty()) {

			Set<Participant> rank = new HashSet<>();

			List<Game> gamesReadyToStart = t.getGamesReadyToStart();

			for (Game game : t.getGamesReadyToStart()) {
				game.start();

				Participant winner = game.getParticipants().get(0);

				game.addPoints(winner, 2);

				game.finish();

				rank.add(game.getParticipants().get(1));
			}


			if (gamesReadyToStart.size() == 1) {
				Participant winner = gamesReadyToStart.get(0).getParticipants().get(0);

				Set<Participant> firstRank = Set.of(winner);
			}

		}

		t.end();

		List<Set<Participant>> result = t.computeFinalRanking();

		assertNotEquals(Collections.emptyList(), result);
	}
}
