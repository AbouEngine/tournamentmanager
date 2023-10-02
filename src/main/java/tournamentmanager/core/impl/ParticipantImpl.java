package tournamentmanager.core.impl;

import tournamentmanager.core.api.Participant;

public class ParticipantImpl implements Participant {

    private String name;
    private boolean eliminated = false;

    public ParticipantImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isEliminated() {
        return this.eliminated;
    }

    @Override
    public void eliminate() {
        this.eliminated = true;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /* we implement setter method from isEliminated variable. That allows us to test eliminated method()*/
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }
}
