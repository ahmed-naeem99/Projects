package battleShip;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemsTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream mockOutput;

    @Before
    public void setUpOutput() {
        mockOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(mockOutput));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void testGameFlow() {
        // Prepare input for the game flow
        String input = "0\n0\nh\n1\n1\nv\n2\n2\nh\n3\n3\nv\n4\n4\nh\n5\n5\nv\n";
        // Create a mock input stream with the prepared input
        InputStream mockInput = new ByteArrayInputStream(input.getBytes());
        // Set the mock input stream as the system input
        System.setIn(mockInput);

        // Run the main method of the game
        Main.main(new String[] {});

        // Define the expected output
        String expectedOutput = "Player 2 has lost the game!\nGame Over!\n";
        // Assert that the actual output matches the expected output
        assertEquals(expectedOutput, mockOutput.toString());
    }
}
