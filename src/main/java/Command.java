/**
 * Represents the supported user commands.
 */
public enum Command {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete");

    private final String word;

    Command(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
