package dukey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DukeyTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getResponse_listCommand_returnsCommandOutput() throws IOException {
        Path dataFile = temporaryDirectory.resolve("dukey.txt");
        Files.writeString(dataFile, "T | 1 | read book\n");
        Dukey dukey = new Dukey(dataFile.toString(), false);

        String response = dukey.getResponse("list");

        assertEquals("Here are the tasks in your list:\n1.[T][X] read book", response);
        assertFalse(dukey.isExit());
    }

    @Test
    public void getResponse_exitCommand_returnsByeAndMarksExit() {
        Path dataFile = temporaryDirectory.resolve("dukey.txt");
        Dukey dukey = new Dukey(dataFile.toString(), false);

        String response = dukey.getResponse("bye");

        assertEquals("Bye. Hope to see you again soon!", response);
        assertTrue(dukey.isExit());
    }
}
