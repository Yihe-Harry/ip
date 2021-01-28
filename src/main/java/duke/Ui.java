/**
 * This UI class handles the output string of Duke
 * 
 * @param logo Duke logo
 * @param line Indentation line
 * @param terminate_input input to end Duke
 * @param indentation spaces to aline the output string
 * 
 * @param sc Scanner to scan the user input
 * 
 * @author WangYihe
 * @author E0424695
 */

package duke;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Ui {
    public static String logo = " ____        _        \n" + "|  _ \\ _   _| | _____ \n" + "| | | | | | | |/ / _ \\\n"
            + "| |_| | |_| |   <  __/\n" + "|____/ \\__,_|_|\\_\\___|\n";
    public static String line = "    ____________________________________________________________";
    public static String indentation = "    ";

    public final Scanner sc;

    public Ui() {
        sc = new Scanner(System.in);
    }

    public void printLine() {
        System.out.println(line);
    }

    public void printMessage(String message) {
        System.out.println(indentation + message);
    }

    public void close() {
        sc.close();
    }

    public void greeting() {
        System.out.println(logo);
        System.out.println(line);
        System.out.println(indentation + "Hello! I'm Duke");
        System.out.println(indentation + "What I can do for you?");
        System.out.println(line);
    }

    public void bye() {
        System.out.println(line);
        System.out.println(indentation + "Bye. Hope to see you again soon!");
        System.out.println(line);
        sc.close();
    }

    public void reportTask(Task t, TaskList task) {
        int count = task.getSize();
        System.out.println(line);
        System.out.println(indentation + "Got it, I've added this task to the list:");
        System.out.println(indentation + t.toString());
        System.out.printf(indentation + "You now have %d task in the list.%n", count);
        System.out.println(line);
    }

    public void printErrorMessage(String message) {
        printLine();
        printMessage("☹ OOPS!!! " + message);
        printLine();
    }

    public Command getUserInputType(String userInput) throws DukeException {
        try {
            return Command.valueOf(userInput.toUpperCase());
        } catch (IllegalArgumentException error) {
            throw new DukeException("Sorry, I dont understand what that means :-(");
        }
    }

    public void reportFindedTask(List<Task> task) {
        printLine();
        printMessage("Here are the matching tasks in your list");
        for (int i = 0; i < task.size(); i++) {
            int index = i + 1;
            printMessage(index + "." + task.get(i).toString());
        }
        printLine();
    }

    public void runUi(TaskList task, FileSaver fs) {
        boolean run = true;
        while (run) {
            try {
                String temp = sc.nextLine();
                String[] input = temp.split(" ", 2);
                Command command = getUserInputType(input[0]);
                switch (command) {
                    case DEADLINE:
                    case TODO:
                    case EVENT:
                        task.add(input, this);
                        break;
                    case DELETE:
                        task.deleteTask(input[1], this);
                        break;
                    case LIST:
                        task.printTask(this);
                        break;
                    case DONE:
                        task.doneTask(input[1], this);
                        break;
                    case FIND:
                        reportFindedTask(task.findTask(input[1]));
                        break;
                    case BYE:
                        bye();
                        run = false;
                        break;
                    default:
                        throw new DukeException("Sorry, I dont understand that");
                }
                fs.save(task);
            } catch (DukeException e) {
                // TODO: handle exception
                printErrorMessage(e.getMessage());
            }
        }
    }
}
