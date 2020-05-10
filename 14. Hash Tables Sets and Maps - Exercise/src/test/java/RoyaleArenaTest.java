import org.junit.Test;

import static org.junit.Assert.*;

public class RoyaleArenaTest {

    @Test
    public void changeCardTypeShouldChangeItsTypeByReference() {
        RoyaleArena arena = new RoyaleArena();
        arena.add(new Battlecard(1, CardType.BUILDING, "Name", 4.3, 43.4));
        arena.changeCardType(1, CardType.MELEE);
        for (Battlecard battlecard : arena.getByCardType(CardType.MELEE)) {
            assertEquals(CardType.MELEE, battlecard.getType());
        }
    }

}