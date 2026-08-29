import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task{
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    protected LocalDateTime by;

    public Deadline(String description, String by) throws DukeyException {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMAT);

        if (this.by.isBefore(LocalDateTime.now())) {
            throw new DukeyException("Deadline date/time cannot be in the past.");
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return this.by.toLocalDate().equals(date);
    }

    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s", this.isDone ? 1 : 0, this.description, this.by.format(INPUT_FORMAT));
    }
}
