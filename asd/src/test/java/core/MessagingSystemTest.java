package core;

import model.Message;
import model.TextMessage;
import org.junit.Before;
import org.junit.Test;
import shared.DataTransferSystem;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MessagingSystemTest {
    private List<Message> messages;

    private DataTransferSystem system;

    @Before
    public void setUp() {
        this.messages = List.of(
                new TextMessage(11, "test_text"),
                new TextMessage(6, "test_text"),
                new TextMessage(19, "test_text"),
                new TextMessage(4, "test_text"),
                new TextMessage(8, "test_text"),
                new TextMessage(17, "test_text")
        );

        this.system = new MessagingSystem();

        assertEquals(0, this.system.size());

        for (Message message : this.messages) {
            this.system.add(message);
        }
    }

    @Test
    public void testAddSingleShouldWorkCorrectly() {
        DataTransferSystem system = new MessagingSystem();

        assertEquals(0, system.size());

        system.add(new TextMessage(12, "test_text"));

        assertEquals(1, system.size());
    }

    @Test
    public void testAddMultipleShouldWorkCorrectly() {
        DataTransferSystem system = new MessagingSystem();

        assertEquals(0, system.size());

        for (Message message : messages) {
            system.add(message);
        }

        assertEquals(messages.size(), system.size());
    }

    @Test
    public void testAddMultipleShouldSetCorrectElements() {
        assertEquals(messages.size(), system.size());
        Message lightest = system.getLightest();
        assertNotNull(lightest);
        assertEquals(4, lightest.getWeight());
        Message heaviest = system.getHeaviest();
        assertNotNull(heaviest);
        assertEquals(19, heaviest.getWeight());
    }

    @Test
    public void testGetPostOrderShouldReturnCorrectSequence() {
        List<Message> postOrder = this.system.getPostOrder();

        int[] expected = {4, 8, 6, 17, 19, 11};
        assertNotNull(postOrder);
        assertEquals(expected.length, postOrder.size());

        for (int i = 0; i < messages.size(); i++) {
            assertEquals(expected[i], postOrder.get(i).getWeight());
        }
    }

    @Test
    public void testGetLightestShouldReturnCorrect() {
        Message lightest = this.system.getLightest();
        assertNotNull(lightest);
        assertEquals(4, lightest.getWeight());
    }

    @Test
    public void testGetHeaviestShouldReturnCorrect() {
        Message heaviest = this.system.getHeaviest();
        assertNotNull(heaviest);
        assertEquals(19, heaviest.getWeight());
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteHeaviest() {
        assertEquals(19, system.deleteHeaviest().getWeight());
        assertEquals(17, system.deleteHeaviest().getWeight());
        assertEquals(11, system.deleteHeaviest().getWeight());
        assertEquals(8, system.deleteHeaviest().getWeight());
        assertEquals(6, system.deleteHeaviest().getWeight());
        assertEquals(4, system.deleteHeaviest().getWeight());
        system.deleteHeaviest();
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteLightest() {
        assertEquals(4, system.deleteLightest().getWeight());
        assertEquals(6, system.deleteLightest().getWeight());
        assertEquals(8, system.deleteLightest().getWeight());
        assertEquals(11, system.deleteLightest().getWeight());
        assertEquals(17, system.deleteLightest().getWeight());
        assertEquals(19, system.deleteLightest().getWeight());
        system.deleteLightest();
    }

    @Test(expected = IllegalStateException.class)
    public void getLightest() {
        int we = system.getLightest().getWeight();
        assertEquals(4, we);
        system.deleteLightest();
        we = system.getLightest().getWeight();
        assertEquals(6, we);
        system.deleteLightest();
        assertEquals(8, system.getLightest().getWeight());
        system.deleteLightest();

        MessagingSystem messagingSystem = new MessagingSystem();
        messagingSystem.getLightest();
    }

    @Test(expected = IllegalStateException.class)
    public void getHeaviest() {
        int we = system.getHeaviest().getWeight();
        assertEquals(19, we);
        system.deleteHeaviest();
        we = system.getHeaviest().getWeight();
        assertEquals(17, we);
        system.deleteHeaviest();
        assertEquals(11, system.getHeaviest().getWeight());
        system.deleteHeaviest();
        assertEquals(8, system.getHeaviest().getWeight());
        system.deleteHeaviest();
        assertEquals(6, system.getHeaviest().getWeight());
        system.deleteHeaviest();
        assertEquals(4, system.getHeaviest().getWeight());
        system.deleteHeaviest();
        system.getHeaviest();
    }
}