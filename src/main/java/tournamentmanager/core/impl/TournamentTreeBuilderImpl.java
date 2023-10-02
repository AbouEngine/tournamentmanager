package tournamentmanager.core.impl;

import tournamentmanager.core.api.Game;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.api.TournamentException;
import tournamentmanager.core.api.TournamentTreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class TournamentTreeBuilderImpl implements TournamentTreeBuilder {

    @Override
    public List<List<Game>> buildAllRounds(List<Participant> rankedParticipants) {
        List<List<Game>> rounds = new ArrayList<>();
        List<Game> nextRound = buildInitialRound(rankedParticipants);
        while (!nextRound.isEmpty()) {
            rounds.add(nextRound);
            nextRound = new ArrayList<>(buildNextRound(nextRound));
        }
        return rounds;
    }

    @Override
    public List<Game> buildInitialRound(List<Participant> participants) {

        List<Participant> remainingRankedParticipants = new ArrayList<>(participants);
        List<Game> initialRound = new ArrayList<>();
        int amountOfInitialGames = participants.size() / 2;
        for (int i = 0; i < amountOfInitialGames; i++) {
            Game game = new GameImpl();
            try {

                game.addParticipant(remainingRankedParticipants.remove(0));
                game.addParticipant(remainingRankedParticipants.remove(0));
            } catch (TournamentException e) {
                throw new RuntimeException(
                        "INTERNAL ERROR: a game was not constructed correctly! This should never happen.");
            }
            initialRound.add(game);
        }
        if (remainingRankedParticipants.size() > 0) {
            throw new RuntimeException("INTERNAL ERROR: there are participants remaining! This should never happen.");
        }
        return initialRound;
    }

    @Override
    public List<Game> buildNextRound(List<? extends Game> previousRound) {
        List<Game> nextRound = new ArrayList<>();
        for (int i = 0; i < previousRound.size() - 1; i = i + 2) {
            Game gameA = previousRound.get(i);
            Game gameB = previousRound.get(i + 1);
            Game newGame = new GameImpl();
            try {
                newGame.addPreviousGame(gameA);
                newGame.addPreviousGame(gameB);
            } catch (TournamentException e) {
                throw new RuntimeException(
                        "INTERNAL ERROR: failed when adding previous games to a new Game, should never happen!", e);
            }
            gameA.setFollowingGame(newGame);
            gameB.setFollowingGame(newGame);
            nextRound.add(newGame);
        }
        return nextRound;
    }

}
