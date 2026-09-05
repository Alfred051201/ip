package dukey.task;

import java.time.LocalDate;

/**
 * Represents a task in the chatbot's task list.
 */

public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an undone task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Creates a task with the given description and completion status.
     *
     * @param description Description of the task.
     * @param isDone Whether the task is already done.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns the display icon for the task completion status.
     *
     * @return X if done, or a blank space otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Checks whether this task occurs on the given date.
     *
     * @param date Date to check against.
     * @return False for a plain task.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    public boolean containsKeyword(String keyword) {
        return this.description.contains(keyword);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }

    /**
     * Returns a storage representation of this task.
     *
     * @return Empty string for an unknown base task type.
     */
    public String toFileString() {
        return "";
    }
}
