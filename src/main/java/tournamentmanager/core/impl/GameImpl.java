package tournamentmanager.core.impl;

import tournamentmanager.core.api.*;

import java.util.*;

public class GameImpl implements Game {

    private final Map<Participant, Integer> participants = new HashMap<>();
    private Status status = Status.NOTSTARTED;
    private Game followingGame;
    private List<Game> previousGames = new ArrayList<>();

    @Override
    public void addParticipant(Participant participant) throws TournamentException {
        if (participant == null) {
            throw new IllegalArgumentException("The participant cannot be null.");
        }
        if (this.participants.size() >= 2) {
            throw new TournamentException("Cannot add participant to game, game already has two participants.");
        }
        this.participants.put(participant, 0);
    }

    @Override
    public void addPoints(Participant participant, int points) throws TournamentException {
        if (participant == null) {
            throw new IllegalArgumentException("The participant cannot be null.");
        }
        if (this.status != Status.INPROGRESS) {
            throw new TournamentException("Cannot change status of a game that is not in progress.");
        }
        if (participants.containsKey(participant)) {
            participants.put(participant, participants.get(participant) + points);
        } else {
            throw new IllegalArgumentException("Cannot add points to a participants that is not part of this game.");
        }
    }


    @Override
    public void start() throws TournamentException {
        if (this.status != Status.NOTSTARTED) {
            throw new TournamentException("Cannot start a game that has already started.");
        } else if (this.participants.size() < 2) {
            throw new TournamentException("Cannot start a game that does not have two participants.");
        }
        this.status = Status.INPROGRESS;
    }

    @Override
    public void finish() throws TournamentException {

        if (this.status != Status.INPROGRESS) {
            throw new TournamentException("Cannot finish a game that has not started.");
        }

        List<Integer> scores = List.copyOf(this.participants.values());
        if (scores.get(0).equals(scores.get(1))) {
            throw new TournamentException("Cannot set the game to 'finished', the scores are ex-aequo. A winner is required.");
        }

        this.status = Status.FINISHED;

        if (this.followingGame != null) {
            this.followingGame.addParticipant(this.getWinner());
        }
        this.getLoser().eliminate();


    }


    @Override
    public List<Participant> getParticipants() {
        return List.copyOf(this.participants.keySet());
    }


    @Override
    public Participant getWinner() throws TournamentException {
        if (this.status != Status.FINISHED) {
            throw new TournamentException("Cannot retrieve winner, the game is not finished.");
        }
        List<Participant> plist = List.copyOf(this.participants.keySet());
        Participant p1 = plist.get(0);
        Participant p2 = plist.get(1);
        if (this.participants.get(p1) > this.participants.get(p2)) {
            return p1;
        } else if (this.participants.get(p2) > this.participants.get(p1)) {
            return p2;
        } else {
            return null; //never happens
        }
    }

    @Override
    public Participant getLoser() throws TournamentException {
        if (this.status != Status.FINISHED) {
            throw new TournamentException("Cannot retrieve loser, the game is not finished.");
        }
        List<Participant> plist = List.copyOf(this.participants.keySet());
        Participant p1 = plist.get(0);
        Participant p2 = plist.get(1);
        if (this.participants.get(p1) < this.participants.get(p2)) {
            return p1;
        } else if (this.participants.get(p2) < this.participants.get(p1)) {
            return p2;
        } else {
            return null; //never happens
        }

    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Optional<Game> getFollowingGame() {
        return Optional.ofNullable(this.followingGame);
    }

    @Override
    public List<Game> getPreviousGames() {
        return Collections.unmodifiableList(this.previousGames);
    }

    @Override
    public void setFollowingGame(Game game) throws IllegalArgumentException {
        if (game == null) {
            throw new IllegalArgumentException("A game cannot be null.");
        }
        this.followingGame = game;
    }

    @Override
    public void addPreviousGame(Game game) throws TournamentException {
        if (status != Status.NOTSTARTED) {
            throw new TournamentException("Cannot modify the previous games after the game has started.");
        } else if (game == null) {
            throw new IllegalArgumentException("A game cannot be null.");
        } else if (this.previousGames.contains(game)) {
            throw new TournamentException("Cannot add a previous game, already present.");
        } else if (this.previousGames.size() >= 2) {
            throw new TournamentException("Cannot add a previous game, there are already two previous games.");
        }
        this.previousGames.add(game);
    }

    //Added this for Game Class testability
    public int getPoints(Participant participant){
        return this.participants.get(participant);
    }
    public void setStatus(Status status){this.status = status;}
}
