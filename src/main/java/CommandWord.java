/**
 * Represents the supported user commands.
 */
public enum CommandWord {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete"),
    ON("on");

    private final String word;

    CommandWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
