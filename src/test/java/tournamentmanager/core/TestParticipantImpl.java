package tournamentmanager.core;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tournamentmanager.core.api.Participant;
import tournamentmanager.core.impl.ParticipantImpl;
import static org.junit.jupiter.api.Assertions.*;

public class TestParticipantImpl {
    private ParticipantImpl participant;
    @BeforeEach
    public void initEach(){
        participant = new ParticipantImpl("Messi");

    }

    //Functional method test
    @Test
    public void testEliminated1(){
        participant.eliminate();
        assertTrue(participant.isEliminated());
    }

    //Functional method test
    @Test
    public void testEliminated2(){
        participant.setEliminated(true);
        participant.eliminate();
        assertTrue(participant.isEliminated());

    }

    //Functional method test
    @Test
    public void testGetNameProperlyRetrievesPlayerName(){
        String expected = "expected";

        participant = new ParticipantImpl(expected);

        assertEquals(expected, participant.getName());
    }

    //Functional method test
    @Test
    public void testSetNameProperlySetsPlayerName(){
        String expected = "expected";

        participant.setName(expected);

        assertEquals(expected, participant.getName());
    }


    //Mutation analysis method test
    @Test
    public void testIsEliminated(){
        participant.setEliminated(false);

        assertEquals(false, participant.isEliminated());
    }
}
