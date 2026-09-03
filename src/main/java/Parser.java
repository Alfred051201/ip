import java.time.LocalDate;

/**
 * Makes sense of user input by identifying commands and extracting command arguments.
 */
public class Parser {
    public Command parse(String userInput) throws DukeyException {
        CommandWord commandWord = parseCommand(userInput);
        if (commandWord == CommandWord.BYE) {
            return new ExitCommand();
        } else if (commandWord == CommandWord.LIST) {
            return new ListCommand();
        } else if (commandWord == CommandWord.ON) {
            return new OnCommand(parseOnDate(userInput));
        } else if (commandWord == CommandWord.TODO) {
            return new TodoCommand(parseTodoDescription(userInput));
        } else if (commandWord == CommandWord.DEADLINE) {
            String[] parts = parseDeadline(userInput);
            return new DeadlineCommand(parts[0], parts[1]);
        } else if (commandWord == CommandWord.EVENT) {
            String[] parts = parseEvent(userInput);
            return new EventCommand(parts[0], parts[1], parts[2]);
        } else if (commandWord == CommandWord.MARK) {
            return new MarkCommand(parseTaskNumber(userInput, CommandWord.MARK,
                    "Please provide a task number to mark."));
        } else if (commandWord == CommandWord.UNMARK) {
            return new UnmarkCommand(parseTaskNumber(userInput, CommandWord.UNMARK,
                    "Please provide a task number to unmark."));
        }

        throw new DukeyException("I'm sorry, but I don't know what that means :-(");
    }

    public CommandWord parseCommand(String userInput) {
        for (CommandWord command : CommandWord.values()) {
            if (isCommand(userInput, command)) {
                return command;
            }
        }
        return null;
    }

    public LocalDate parseOnDate(String userInput) throws DukeyException {
        String dateText = getCommandArguments(userInput, CommandWord.ON);
        if (dateText.isEmpty()) {
            throw new DukeyException("Please provide a date using format: yyyy-MM-dd");
        }

        return LocalDate.parse(dateText);
    }

    public int parseTaskNumber(String userInput, CommandWord command, String emptyMessage) throws DukeyException {
        String taskNumberText = getCommandArguments(userInput, command);
        if (taskNumberText.isEmpty()) {
            throw new DukeyException(emptyMessage);
        }

        return Integer.parseInt(taskNumberText);
    }

    public String parseTodoDescription(String userInput) throws DukeyException {
        String description = getCommandArguments(userInput, CommandWord.TODO);
        if (description.isEmpty()) {
            throw new DukeyException("The description of a todo cannot be empty.");
        }

        return description;
    }

    public String[] parseDeadline(String userInput) throws DukeyException {
        String input = getCommandArguments(userInput, CommandWord.DEADLINE);
        if (input.isEmpty()) {
            throw new DukeyException("Please provide a deadline task description.");
        }

        String[] parts = parseByKeywords(input, "Please use: deadline {DESCRIPTION} /by {WHEN}", "/by");
        if (parts[0].isEmpty()) {
            throw new DukeyException("Please provide a deadline task description.");
        }

        if (parts[1].isEmpty()) {
            throw new DukeyException("Please provide a deadline task date/time after /by.");
        }

        return parts;
    }

    public String[] parseEvent(String userInput) throws DukeyException {
        String input = getCommandArguments(userInput, CommandWord.EVENT);
        if (input.isEmpty()) {
            throw new DukeyException("Please provide a event task description.");
        }

        String[] parts = parseByKeywords(input, "Please use: event {DESCRIPTION} /from {WHEN} /to {WHEN}",
                "/from", "/to");
        if (parts[0].isEmpty()) {
            throw new DukeyException("Please provide a event task description.");
        }

        if (parts[1].isEmpty()) {
            throw new DukeyException("Please provide a event task date/time after /from.");
        }

        if (parts[2].isEmpty()) {
            throw new DukeyException("Please provide a event task date/time after /to.");
        }

        return parts;
    }

    private boolean isCommand(String userInput, CommandWord command) {
        String commandWord = command.getWord();
        return userInput.equals(commandWord) || userInput.startsWith(commandWord + " ");
    }

    private String getCommandArguments(String userInput, CommandWord command) {
        return userInput.substring(command.getWord().length()).trim();
    }

    private String[] parseByKeywords(String input, String errorMessage, String... keywords) throws DukeyException {
        String[] result = new String[keywords.length + 1];

        int currentStart = 0;

        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];

            int keywordIndex = input.indexOf(keyword, currentStart);

            if (keywordIndex == -1) {
                throw new DukeyException(errorMessage);
            }

            while (keywordIndex != -1
                    && ((keywordIndex > 0 && input.charAt(keywordIndex - 1) != ' ')
                    || (keywordIndex + keyword.length() < input.length()
                    && input.charAt(keywordIndex + keyword.length()) != ' '))) {
                keywordIndex = input.indexOf(keyword, keywordIndex + 1);
            }

            if (keywordIndex == -1) {
                throw new DukeyException(errorMessage);
            }

            result[i] = input.substring(currentStart, keywordIndex).trim();
            currentStart = keywordIndex + keyword.length();

            while (currentStart < input.length() && input.charAt(currentStart) == ' ') {
                currentStart++;
            }
        }

        result[keywords.length] = input.substring(currentStart).trim();
        return result;
    }
}
