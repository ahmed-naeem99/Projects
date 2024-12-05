package battleShip;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.util.Scanner;
import org.junit.Test;

public class UnitTest {

    @Test
    public void testValidCharInput() {
        Player player = new Player("TestPlayer");
        Scanner scanner = new Scanner("h\n");
        assertEquals('h', player.readValidChar(scanner));
    }

    @Test
    public void testValidIntegerInput() {
        Player player = new Player("TestPlayer");
        ByteArrayInputStream in = new ByteArrayInputStream("5\n".getBytes());
        System.setIn(in);
        assertEquals(5, player.readValidInteger());
    }

    @Test
    public void testInValidIntegerInput() {
        Player player = new Player("TestPlayer");
        ByteArrayInputStream in = new ByteArrayInputStream("invalid\n5\n".getBytes());
        System.setIn(in);
        assertEquals(5, player.readValidInteger());
    }
}
