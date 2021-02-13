/**
 * This TaskList class handles the logic of adding and deleting tasks of Duke
 * 
 * @param file Task file to remember the tasks
 * 
 * @author WangYihe
 * @author E0424695
 */

package duke.fileSaver;

import duke.ui.Ui;
import duke.exception.DukeException;
import duke.task.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

import java.util.Scanner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FileSaver {
    public File file;
    
    public FileSaver(String folderName, String fileName) {
        file = new File(folderName, fileName);
    }

    public void createFile() throws DukeException {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
                System.out.println("CREATED_FOLDER");
            }
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("CREATED_SAVE_FILE");
            }
        } catch (IOException e) {
            //TODO: handle exception
            throw new DukeException(e.getMessage());
        }
    }

    public void addTask(TaskList task, Task t, String isDone) {
        if (isDone.trim().equals("1")) {
            t.markAsDone();
        } 
        task.add(t);
    }

    public static boolean isDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (ParseException e) {
            //TODO: handle exception
            return false;
        }
        return true;
    }

    public void parseTask(TaskList task, String info) throws IndexOutOfBoundsException{
        String[] taskInfo = info.split(" \\| ");
        String type = taskInfo[0].trim();
        switch (type) {
        case "T":
            Todo t = new Todo(taskInfo[2]);
            addTask(task, t, taskInfo[1]);                
            break;
        case "E":
            Event e = new Event(taskInfo[2], taskInfo[3]);
            addTask(task, e, taskInfo[1]);
            break;
        case "D":
            String time = taskInfo[3];
            if (isDateFormat(time)) {
                LocalDate date = LocalDate.parse(time);
                time = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            }
            Deadline deadline = new Deadline(taskInfo[2], time);
            addTask(task, deadline, taskInfo[1]);
            break;
        
        default:
            break;
        } 
    }

    public void load(TaskList task) throws DukeException {
        createFile();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                parseTask(task, sc.nextLine());
            }    
        } catch (IndexOutOfBoundsException e) {
            //TODO: handle exception
            throw new DukeException("File is not being read properly.");
        } catch (FileNotFoundException f) {
            throw new DukeException("File is not found");
        }
    }

    public void save(TaskList task) throws DukeException {
        try {
            FileWriter fw = new FileWriter(file);
            for (int i = 0; i < task.getSize(); i++) {
                Task t = task.get(i);
                String saveFormat = t.savedFormat();
                assert saveFormat.length() >= 0 : "Save format error, length less than 0";
                fw.write(t.savedFormat() + System.lineSeparator());
            } 
            fw.close();
        } catch (IOException e) {
            //TODO: handle exception
            throw new DukeException("There is something wrong writing to the file");
        }
    }
}