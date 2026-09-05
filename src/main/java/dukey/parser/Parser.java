package dukey.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import dukey.command.Command;
import dukey.command.CommandWord;
import dukey.command.DeadlineCommand;
import dukey.command.DeleteCommand;
import dukey.command.EventCommand;
import dukey.command.ExitCommand;
import dukey.command.FindCommand;
import dukey.command.ListCommand;
import dukey.command.MarkCommand;
import dukey.command.OnCommand;
import dukey.command.TodoCommand;
import dukey.command.UnmarkCommand;
import dukey.exception.DukeyException;

/**
 * Makes sense of user input by identifying commands and extracting command arguments.
 */
public class Parser {
    /**
     * Parses raw user input into an executable command.
     *
     * @param userInput Full command entered by the user.
     * @return Command object representing the user input.
     * @throws DukeyException If the command is unknown or its arguments are invalid.
     */
    public Command parse(String userInput) throws DukeyException {
        CommandWord commandWord = parseCommand(userInput);

        try {
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
            } else if (commandWord == CommandWord.DELETE) {
                return new DeleteCommand(parseTaskNumber(userInput, CommandWord.DELETE,
                        "Please provide a task number to delete."));
            } else if (commandWord == CommandWord.FIND) {
                return new FindCommand(parseFindKeyword(userInput));
            }
        } catch (NumberFormatException e) {
            throw new DukeyException("Please provide a valid task number.");
        } catch (DateTimeParseException e) {
            throw new DukeyException(getDateFormatMessage(commandWord));
        }

        throw new DukeyException("I'm sorry, but I don't know what that means :-(");
    }

    /**
     * Parses the command word from raw user input.
     *
     * @param userInput Full command entered by the user.
     * @return Matching command word, or null if the command is unknown.
     */
    public CommandWord parseCommand(String userInput) {
        for (CommandWord command : CommandWord.values()) {
            if (isCommand(userInput, command)) {
                return command;
            }
        }
        return null;
    }

    /**
     * Parses the date argument of an on command.
     *
     * @param userInput Full on command entered by the user.
     * @return Date to search for.
     * @throws DukeyException If the date argument is missing.
     */
    public LocalDate parseOnDate(String userInput) throws DukeyException {
        String dateText = getCommandArguments(userInput, CommandWord.ON);
        if (dateText.isEmpty()) {
            throw new DukeyException("Please provide a date using format: yyyy-MM-dd");
        }

        return LocalDate.parse(dateText);
    }

    /**
     * Parses a one-based task number from a command.
     *
     * @param userInput Full command entered by the user.
     * @param command Command word whose arguments should be parsed.
     * @param emptyMessage Error message to use if the task number is missing.
     * @return One-based task number.
     * @throws DukeyException If the task number is missing.
     */
    public int parseTaskNumber(String userInput, CommandWord command, String emptyMessage) throws DukeyException {
        String taskNumberText = getCommandArguments(userInput, command);
        if (taskNumberText.isEmpty()) {
            throw new DukeyException(emptyMessage);
        }

        return Integer.parseInt(taskNumberText);
    }

    /**
     * Parses the description from a todo command.
     *
     * @param userInput Full todo command entered by the user.
     * @return Todo description.
     * @throws DukeyException If the description is empty.
     */
    public String parseTodoDescription(String userInput) throws DukeyException {
        String description = getCommandArguments(userInput, CommandWord.TODO);
        if (description.isEmpty()) {
            throw new DukeyException("The description of a todo cannot be empty.");
        }

        return description;
    }

    /**
     * Parses the search keyword from a find command.
     *
     * @param userInput Full find command entered by the user.
     * @return Keyword to search for.
     * @throws DukeyException If the keyword is empty.
     */
    public String parseFindKeyword(String userInput) throws DukeyException {
        String keyword = getCommandArguments(userInput, CommandWord.FIND);
        if (keyword.isEmpty()) {
            throw new DukeyException("Please provide a keyword to find.");
        }

        return keyword;
    }

    /**
     * Parses the description and date/time from a deadline command.
     *
     * @param userInput Full deadline command entered by the user.
     * @return Array containing the description followed by the by date/time.
     * @throws DukeyException If the description, keyword, or date/time is missing.
     */
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

    /**
     * Parses the description, start date/time, and end date/time from an event command.
     *
     * @param userInput Full event command entered by the user.
     * @return Array containing the description, from date/time, and to date/time.
     * @throws DukeyException If the description, keywords, or date/time values are missing.
     */
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

    private String getDateFormatMessage(CommandWord commandWord) {
        if (commandWord == CommandWord.ON) {
            return "Please use date format: yyyy-MM-dd";
        } else if (commandWord == CommandWord.DEADLINE) {
            return "Please use deadline date/time format: yyyy-MM-dd HHmm";
        } else if (commandWord == CommandWord.EVENT) {
            return "Please use event date/time format: yyyy-MM-dd HHmm";
        }

        return "Please use a valid date/time format.";
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
