import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

import dukey.exception.DukeyException;
import dukey.task.Deadline;
import dukey.task.Event;
import dukey.task.Task;
import dukey.task.TaskList;
import dukey.task.Todo;

/**
 * Handles loading tasks from the data file and saving tasks to the data file.
 */
public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public TaskList load() throws FileNotFoundException, DukeyException {
        TaskList tasks = new TaskList();
        File file = new File(this.filePath);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                tasks.add(parseTask(scanner.nextLine()));
            }
        }

        return tasks;
    }

    public void save(TaskList tasks) throws DukeyException {
        File file = new File(this.filePath);
        File parentDirectory = file.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            boolean isParentDirCreated = parentDirectory.mkdirs();

            if (!isParentDirCreated) {
                throw new DukeyException("Could not create data directory");
            }
        }

        try (FileWriter fw = new FileWriter(file)) {
            for (int i = 1; i <= tasks.size(); i++) {
                Task task = tasks.get(i);
                String fileLine = task.toFileString();

                if (fileLine.isEmpty()) {
                    throw new DukeyException("Could not save an unknown task type.");
                }
                fw.write(fileLine);
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new DukeyException("Could not save tasks to file.");
        }
    }

    private Task parseTask(String fileLine) throws DukeyException {
        try {
            String[] parts = fileLine.split("\\s*\\|\\s*");

            if (parts.length < 3) {
                throw new DukeyException("Saved task is missing fields.");
            }

            if (!Arrays.asList("T", "D", "E").contains(parts[0])) {
                throw new DukeyException("Undefined task type.");
            }

            if (parts[1].isEmpty()) {
                throw new DukeyException("Done status is missing for this task.");
            }

            if (!Arrays.asList("0", "1").contains(parts[1])) {
                throw new DukeyException("Undefined done status.");
            }

            if (parts[2].isEmpty()) {
                throw new DukeyException("Description is missing for this task.");
            }

            Task task = createTask(parts);
            if (parts[1].equals("1")) {
                task.markAsDone();
            }
            return task;
        } catch (DateTimeParseException e) {
            throw new DukeyException("Saved date/time must use format: yyyy-MM-dd HHmm");
        }
    }

    private Task createTask(String[] parts) throws DukeyException {
        if (parts[0].equals("T")) {
            return new Todo(parts[2]);
        } else if (parts[0].equals("D")) {
            if (parts.length < 4 || parts[3].isEmpty()) {
                throw new DukeyException("Deadline date/time is missing for this task.");
            }
            return new Deadline(parts[2], parts[3]);
        } else {
            if (parts.length < 5 || parts[3].isEmpty() || parts[4].isEmpty()) {
                throw new DukeyException("Event date/time is missing for this task.");
            }
            return new Event(parts[2], parts[3], parts[4]);
        }
    }
}
