package dukey.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dukey.exception.DukeyException;

public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) throws DukeyException {
        super(description);
        this.from = LocalDateTime.parse(from, INPUT_FORMAT);
        this.to = LocalDateTime.parse(to, INPUT_FORMAT);

        if (this.from.isAfter(this.to)) {
            throw new DukeyException("Event start date/time cannot be later than end date/time.");
        }
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(OUTPUT_FORMAT)
                + " to: " + this.to.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate startDate = this.from.toLocalDate();
        LocalDate endDate = this.to.toLocalDate();
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public String toFileString() {
        return String.format("E | %d | %s | %s | %s", this.isDone ? 1 : 0, this.description,
                this.from.format(INPUT_FORMAT), this.to.format(INPUT_FORMAT));
    }
}
