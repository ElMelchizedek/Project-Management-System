import java.util.Scanner;

public class UserInterface {
    /*
    !!! GLOBAL VARIABLES !!!
     */
    // Debug Toggle
    private static boolean debug = true;

    // *** Private variables to be accessed and manipulated via setters and getters. ***
    private static Project project1 = null;
    private static Project project2 = null;
    private static Project project3 = null;

    // Constant needed for auxCalculateAverageDuration().
    public static final double SECRET_NUMBER = 100000000;
    public static final double SECRET_DIGITS = 9;
    // Constant needed for auxCheckInputValid(String). Pay no attention to its contents.
    public static final String SECERT_STRING = "Hegel's criticism of the 'sentimental religion' of\n" +
            "Jacobi or Schleiermacher was misleading: he accused it of\n" +
            "subjectivism, as though he himself were a champion of the\n" +
            "reality of God's existence, but this was quite untrue. By represent-\n" +
            "ing the finite spirit as a manifestation of universal spirit, Hegel\n" +
            "made the latter a projection of historical self-consciousness, while\n" +
            "infinity appeared as merely the self-negation of finitude—i.e.\n" +
            "God, in the last analysis, is merely a creation of the human ego,\n" +
            "which with diabolic pride lays claim to almighty power. Hegel's\n" +
            "'world spirit', too, acquires reality only thanks to the operation\n" +
            "of human historical self-consciousness. Human history is thus\n" +
            "self-sufficient and has no significance beyond its own self-\n" +
            "development. So, according to Hegel, God is dead and the only\n" +
            "reality is self-consciousness.\n +" +
            "(Bruno Bauer and the negativity of self-consciousness)";

    /*
    !!! METHODS !!!
     */

    // *** Debug Methods ***
    private static String debugInitialise(String IDsProjects) {
        // Initialise Projects.
        System.out.println("DEBUG: INITIALISING");
        project1 = new Project(1, "Borges", "SMALL");
        IDsProjects = auxAssignProject(1, "SMALL", "1", IDsProjects);
        project2 = new Project(2, "Spencer", "MEDIUM");
        IDsProjects = auxAssignProject(2, "MEDIUM", "2", IDsProjects);
        project3 = new Project(3, "Wolfe", "LARGE");
        IDsProjects = auxAssignProject(3, "LARGE", "3", IDsProjects);

        // Generate Tasks.
        project1.createTask(11, "Hegel", "A", 1, false);
        project2.createTask(21, "Bauer", "A", 2, false);
        project2.createTask(22, "Feurbach", "L", 3, false);
        project3.createTask(31, "Marx", "A", 4, false);
        project3.createTask(32, "Engels", "L", 5, false);
        project3.createTask(33, "Lenin", "S", 6, false);

        return IDsProjects;
    }

    // *** Auxiliary methods. ***

    // Used in optionCreateProject().
    private static String auxAssignProject(int ID, String type, String valueID, String IDsProjects) {
        System.out.println("Created Project #" + ID + " of type " + type + ".");
        return IDsProjects.concat(valueID + ",");
    }

    // Used in auxViewProject().
    public static void auxTaskPrettyInfo(Task task, boolean includeStatus) {
        if (task != null) {
            System.out.print("\t* Task ID: " + task.getTaskID() +
                    ", Description: " + task.getDescription() +
                    ", Type: " + task.getTaskType() +
                    ", Duration: " + task.getTaskDuration());
            if (includeStatus) {
                System.out.print(", Status: " + task.status());
            }
            System.out.print("\n");
        }
    }

    // Used in optionViewProjects() and dispFilteredTasks().
    private static void auxViewProject(Project project, String chosenTasks) {
        if (project != null) {
            int amountTasks = project.amountTasks();
            if (amountTasks != 0) {
                System.out.println("Project ID: " + project.getProjectID() +
                        ", Project Name: " + project.getProjectName() +
                        ", Project Type: " + project.getProjectType() +
                        ", Num of tasks: " + amountTasks);
                System.out.println("Tasks:");
                if (chosenTasks.contains("1")) {
                    auxTaskPrettyInfo(project.getTask(1), true);
                }
                if (chosenTasks.contains("2")) {
                    auxTaskPrettyInfo(project.getTask(2), true);
                }
                if (chosenTasks.contains("3")) {
                    auxTaskPrettyInfo(project.getTask(3), true);
                }
            } else { System.out.println("No Tasks to report for Project #" + project.getProjectID() + "."); }
        }
    }

    // Used in optionCreateTask().
    public static Project auxGetProjectByID(int ProjectID) {
        Project certainProject = null;
        if (project1 != null && project1.getProjectID() == ProjectID) {
            certainProject = project1;
        } else if (project2 != null && project2.getProjectID() == ProjectID) {
            certainProject = project2;
        } else if (project3 != null && project3.getProjectID() == ProjectID) {
            certainProject = project3;
        }

        return certainProject;
    }

    // Used in optionEditTask(), optionRemoveTask(), and dispCompleteTasks().
    public static Project auxDialogueGetProject(Scanner input) {
        Project certainProject;
        System.out.print("Enter ID of Project:");
        int ProjectID;
        while (true) {
            ProjectID = input.nextInt();
            // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
            input.nextLine();
            certainProject = auxGetProjectByID(ProjectID);
            if (certainProject == null) {
                System.out.print("ERROR: Entered Project ID does not match any existing Project. Please try again: ");
                continue;
            }
            break;
        }
        return certainProject;
    }

    // Used in dispFilteredTasks() and auxGetAverageTypeDurations().
    public static String auxFilterTypes(Project project, char type) {
        String matchingTasks = "";

        if (project != null) {
            for (int i = 1; i <= 3; i++) {
                if (project.getTask(i) != null
                && project.getTask(1).getTaskType() == type) {
                   matchingTasks = matchingTasks.concat(String.valueOf(i));
                }
            }
        }

        return matchingTasks;
    }

    // Used in auxGetAverageTypeDurations().
    public static double auxCalculateAverageDuration(Project project, String matches, int stopAtProject) {
        int sumDurations = 0;
        int amountMatching = 0;
        if (matches.contains("1")) { sumDurations += project.getTask(1).getTaskDuration(); amountMatching++; }
        if (matches.contains("2")) { sumDurations += project.getTask(2).getTaskDuration(); amountMatching++; }
        if (matches.contains("3")) { sumDurations += project.getTask(3).getTaskDuration(); amountMatching++; }
        // Here I do some funky stuff since I can't just return an array, so I need to be able to distinguish different
        // outcomes whilst not being able to have multiple return types for the method.
        // To differentiate I do this by multiplying the number by a defined power of 10 that is then checked by the
        // calling function to figure out what kind of return value it is from this function.
        if (stopAtProject == 1) {
            return (((double)sumDurations / (double)amountMatching) * SECRET_NUMBER);
        } else { return sumDurations; }
    }
    // Used in dispAverageTaskDurations() and auxPrettyAverageTypeDurationsByProject().
    public static double auxGetAverageTypeDurations(char type, int stopAtProject) {
        int amountMatching = 0;
        int sumDurations = 0;

        // I wish I had arrays right now.
        double result   = auxCalculateAverageDuration(project1, auxFilterTypes(project1, type), stopAtProject);
        // Cf. comments within auxCalculateAverageDuration to understand why I am doing these eccentric calculations on the return value.
        if ((int)(Math.log10(Math.abs(result)) + 1) == SECRET_DIGITS) { return result; } else { sumDurations += (int)result; amountMatching++; }

        result   = auxCalculateAverageDuration(project2, auxFilterTypes(project2, type), stopAtProject);
        if ((int)(Math.log10(Math.abs(result)) + 1) == SECRET_DIGITS) { return result; } else { sumDurations += (int)result; amountMatching++; }

        result   = auxCalculateAverageDuration(project3, auxFilterTypes(project3, type), stopAtProject);
        if ((int)(Math.log10(Math.abs(result)) + 1) == SECRET_DIGITS) { return result; } else { sumDurations += (int)result; amountMatching++; }

        return ((double) sumDurations / (double) amountMatching);
    }

    // Used in dispAverageTypeDurations().
    public static void auxPrettyAverageTypeDurationsByProject(Project project, int num) {
        System.out.println("Project ID " + project.getProjectID() + ":");
        System.out.println("\t* Average task duration of administrative tasks is " + auxGetAverageTypeDurations('A', num) + ".");
        System.out.println("\t* Average task duration of logistics tasks is " + auxGetAverageTypeDurations('L', num) + ".");
        System.out.println("\t* Average task duration of support tasks is " + auxGetAverageTypeDurations('S', num) + ".");
    }

    // Multiple overloads of the method to handle different types of data being checked.
    // Yes data is a redundant variable that is unused, but I don't know how to make a method have a generic return
    // type, so I'm just going to leave them in there.
    public static String auxCheckInputValid(String data, Scanner input) {
        if (!input.hasNextLine()) {
            System.out.print("Value entered was not a valid name. Please try again: ");
            return SECERT_STRING;
        }
        String value = input.nextLine();
        if (value.isEmpty()) {
            System.out.print("Empty input given. Please try again: ");
            return SECERT_STRING;
        }
        return value;
    }
    public static int auxCheckInputValid(int data, Scanner input) {
        if (!input.hasNextInt()) {
            System.out.print("Value entered was not a valid number (integer). Please try again: ");
            return -1;
        }
        int value = input.nextInt();
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();
        return value;
    }



    // *** Methods invoked by user input ("Option Methods"). ***

    public static String optionCreateProject(Scanner input, int amountProjects, String IDsProjects) {
        System.out.println("*** Project Wizard ***");
        Project tempProject;

        if (amountProjects > 3) {
            System.out.println("ERROR: Maximum amount of concurrent projects already reached! Aborting project creation process...");
            return "";
        }

        System.out.print("Enter ID for new Project: ");
        int ID;
        String valueID;
        while (true) {
            ID = auxCheckInputValid(0, input);
            if (ID == -1) { continue; }
            valueID = String.valueOf(ID);
            if (IDsProjects.contains(valueID + ",")) {
                System.out.print("Project ID already in use. Please try again: ");
                continue;
            }
            break;
        }

        System.out.print("Enter name for new project: ");
        String name;
        while (true) {
           name = auxCheckInputValid("", input);
           if (name.equals(SECERT_STRING)) { continue; }
           break;
        }

        System.out.print("Enter type for new project (options: \"small\", \"medium\", or \"large\"): ");
        String type;
        while(true) {
            type = auxCheckInputValid("", input);
            if (type.equals(SECERT_STRING)) { continue; }
            switch (type.toUpperCase()) {
                case "SMALL":
                    tempProject = new Project(ID, name, "SMALL");
                    break;
                case "MEDIUM":
                    tempProject = new Project(ID, name, "MEDIUM");
                    break;
                case "LARGE":
                    tempProject = new Project(ID, name, "LARGE");
                    break;
                default:
                    System.out.println("Unknown project type. Please try again: ");
                    continue;
            }
            break;
        }

        if (project1 == null) {
            project1 = tempProject;
            return auxAssignProject(ID, type, valueID, IDsProjects);
        } else if (project2 == null) {
            project2 = tempProject;
            return auxAssignProject(ID, type, valueID, IDsProjects);
        } else if (project3 == null) {
            project3 = tempProject;
            return auxAssignProject(ID, type, valueID, IDsProjects);
        }

        return "";
    }

    public static void optionRemoveProject(Scanner input) {
        System.out.println("*** Project Removal Wizard ***");

        System.out.print("Please enter ID of Project to be removed: ");
        int ID;
        while (true) {
            ID = auxCheckInputValid(0, input);
            if (ID == -1) { continue; }
            break;
        }

        if (project1.getProjectID() == ID) {
            project1 = null;
            return;
        } else if (project2.getProjectID() == ID) {
            project2 = null;
            return;
        } else if (project3.getProjectID() == ID) {
            project3 = null;
            return;
        }
        System.out.println("ID does not match any known Project ID. Aborting...");
    }

    public static void optionCreateTask(Scanner input) {
        System.out.println("*** Task Wizard ***");
        Project certainProject;

        System.out.print("Enter ID of Project you would like to a create a Task for: ");
        int ProjectID;
        while (true) {
            ProjectID = auxCheckInputValid(0, input);
            if (ProjectID == -1) { continue; }
            certainProject = auxGetProjectByID(ProjectID);
            if (certainProject == null) {
                System.out.println("ERROR: Inputted Project ID does not match any existing Project. Please try again: ");
                continue;
            }
            break;
        }

        System.out.print("Enter ID for new Task: ");
        int newID;
        while (true) {
            newID = auxCheckInputValid(0, input);
            if (newID == -1) { continue; }

            String valueID = String.valueOf(newID);
            if (certainProject.getAllTaskIDs() != null && certainProject.getAllTaskIDs().contains(valueID + ",")) {
                System.out.print("ERROR: Duplicate Task ID! Please try again: ");
                continue;
            }
            break;
        }

        System.out.print("Enter a brief description of the new Task: ");
        String newDescription;
        while (true) {
            newDescription = auxCheckInputValid("", input);
            if (newDescription.equals(SECERT_STRING)) { continue; }
            break;
        }

        System.out.print("Enter the Task type ([A]dministrative, [L]ogistics, or [S]upport: ");
        String newType;
        while (true) {
            newType = auxCheckInputValid("", input);
            if (newType.equals(SECERT_STRING)) { continue; }
            newType = newType.toUpperCase();
            if (newType.length() != 1) {
                System.out.print("ERROR: Task type was not specified with a single character. Please try again: ");
                continue;
            }
            if (!"ALS".contains(newType)) {
                System.out.println("ERROR: Specified Task type is not [A]dministrative, [L]ogistics, or [S]upport. Please try again: ");
                continue;
            }
            break;
        }

        System.out.print("Enter estimated or actual type spent on new Task: ");
        int newDuration;
        while (true) {
            newDuration = auxCheckInputValid(0, input);
            if (newDuration == -1) { continue; }
            break;
        }

        certainProject.createTask(newID, newDescription, newType, newDuration, false);
    }

    // Really just the "mark task completed" function.
    public static void optionEditTask(Scanner input) {
        Task certainTask;
        Project certainProject = auxDialogueGetProject(input);
        if (certainProject == null) { return; }

        System.out.print("Enter ID of Task to be edited: ");
        int TaskID;
        while (true) {
            TaskID = auxCheckInputValid(0, input);
            if (TaskID == -1) { continue; }

            certainTask = certainProject.retrieveTaskByID(TaskID);
            if (certainTask == null) {
                System.out.print("Entered Task ID does not match any existing Task within selected Project. Please try again: ");
                continue;
            }
            break;
        }

        System.out.print("Would you like to edit the completed status of the selected Task? (currently: " + certainTask.status() + ") [y/n]");
        String choice;
        while(true) {
            choice = auxCheckInputValid("", input);
            if (choice.equals(SECERT_STRING)) { continue; }
            choice = choice.toLowerCase();

            if (choice.equals("y")) {
                certainTask.setCompleted(!certainTask.getCompleted());
                System.out.print("Status of Task #" + certainTask.getTaskID()
                        + " of Project #" + certainProject.getProjectID()
                        + " is now " + certainTask.status() + ".");
            } else {
                System.out.println("Exhausted all attributes of Task to edit. Returning...");
            }
            return;
        }
    }

    public static void optionRemoveTask(Scanner input) {
        Project certainProject = auxDialogueGetProject(input);
        if (certainProject == null) { return; }

        System.out.print("Please enter ID of Task to be removed: ");
        int TaskID;
        while (true) {
            TaskID = auxCheckInputValid(0, input);
            if (TaskID == -1) { continue; }
            break;
        }

        certainProject.deleteTask(TaskID);
    }

    public static void optionDisplay(Scanner input) {
        String options = """ 
                        Options for Display:
                        \t *0: Print this dialogue again.
                        \t *1: Exit to Project Management System.
                        \t *2: All Project details.
                        \t *3: Completed Tasks within Project.
                        \t *4: Tasks of all Projects filtered by type.
                        \t *5: Average duration of Tasks filtered by type.
                        """;

        System.out.println("*** Display Wizard ***");
        System.out.print(options);

        while (true) {
            System.out.print("? ");
            int line = input.nextInt();
            input.nextLine();
            switch (line) {
                case 0:
                    System.out.print(options);
                    break;
                case 2:
                    dispViewProjects();
                    break;
                case 3:
                    dispCompleteTasks(input);
                    break;
                case 4:
                    dispFilteredTasks(input);
                    break;
                case 5:
                    dispAverageTypeDurations();
                    break;
                case 1:
                    return;
                default:
                    System.out.println("Unknown option.");
                    break;
            }
        }
    }



    // *** Display Submethods ***

    // Displaying All Project Details
    public static void dispViewProjects() {
        if (project1 != null) {
            auxViewProject(project1, "123");
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        if (project2 != null) {
            auxViewProject(project2, "123");
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        if (project3 != null) {
            auxViewProject(project3, "123");
        }
    }

    // Displaying Completed Tasks
    public static void dispCompleteTasks(Scanner input) {
        Project certainProject = auxDialogueGetProject(input);
        if (certainProject == null) { return; }

        System.out.println("Completed Tasks in Project #" + certainProject.getProjectID() + ".");
        Task task1 = certainProject.getTask(1);
        Task task2 = certainProject.getTask(2);
        Task task3 = certainProject.getTask(3);
        if (task1 != null && task1.getCompleted()) { auxTaskPrettyInfo(task1, false); }
        if (task2 != null && task2.getCompleted()) { auxTaskPrettyInfo(task2, false); }
        if (task3 != null && task3.getCompleted()) { auxTaskPrettyInfo(task3, false); }

    }

    // Filtering Tasks by Type
    public static void dispFilteredTasks(Scanner input) {
        String matchingTasks;
        
        System.out.print("Enter the type to filter by ([A]dministrative, [L]ogistical, or [S]upport): ");
        String filter;
        while (true) {
            filter = auxCheckInputValid("", input);
            if (filter.equals(SECERT_STRING)) { continue; }
            if (filter.length() != 1 || !Task.permittedTypes.contains(filter.toUpperCase())) {
                System.out.print("ERROR: Invalid filter type entered. Please try again: ");
                continue;
            }
            break;
        }

        matchingTasks = auxFilterTypes(project1, filter.toUpperCase().charAt(0));
        auxViewProject(project1, matchingTasks);
        System.out.println("--------------------------------------------------------------------------------------------");
        matchingTasks = auxFilterTypes(project2, filter.toUpperCase().charAt(0));
        auxViewProject(project2, matchingTasks);
        System.out.println("--------------------------------------------------------------------------------------------");
        matchingTasks = auxFilterTypes(project3, filter.toUpperCase().charAt(0));
        auxViewProject(project3, matchingTasks);
    }

    // Average Task Type Durations
    public static void dispAverageTypeDurations() {
        System.out.println("---Average Task Duration---");
        System.out.println("Average task duration of all task types across all projects:");
        System.out.println("\t* Average task duration of all administrative tasks is " + auxGetAverageTypeDurations('A', 0) + " hours.");
        System.out.println("\t* Average task duration of all logistics tasks is " + auxGetAverageTypeDurations('L', 0) + " hours.");
        System.out.println("\t* Average task duration of all support tasks is " + auxGetAverageTypeDurations('S', 0) + " hours.");

        System.out.println("---Breakdown by Project---");
        auxPrettyAverageTypeDurationsByProject(project1, 1);
        auxPrettyAverageTypeDurationsByProject(project2, 2);
        auxPrettyAverageTypeDurationsByProject(project3, 3);
    }



    // *** Main entry method. ***
    public static void main(String[] args) {
        int amountProjects = 0;
        String IDsProjects = "";
        String help = """
                Options (case-insensitive):
                \t * H: help
                \t * Q: quit\s
                \t* CP: create project
                \t* D: display info
                \t* RP: remove project
                \t* CT: create task
                \t* ET: edit task
                \t* RT: remove task
                """;

        Scanner input = new Scanner(System.in);

        // Debug stuff
        if (debug) {
            IDsProjects = debugInitialise(IDsProjects);
            amountProjects = 3;
        }

        System.out.println("Project Management System");
        System.out.print(help);

        while (true) {
            System.out.print("> ");
            String line = input.nextLine();
            if (line != null) {
                switch (line.toUpperCase()) {
                    // Help.
                    case "H":
                        System.out.print(help);
                        break;
                    // Quit.
                    case "Q":
                        System.exit(0);
                        break;
                    // Create Project.
                    case "CP":
                        String tempIDsProjects = optionCreateProject(input, amountProjects, IDsProjects);
                        if (tempIDsProjects.isEmpty()) {
                            System.out.println("ERROR: Unknown error. Aborting project creation process...");
                            break;
                        }
                        IDsProjects = tempIDsProjects;
                        break;
                    // Display Info.
                    case "D":
                        optionDisplay(input);
                        break;
                    // Remove Project.
                    case "RP":
                        optionRemoveProject(input);
                        break;
                    // Create Task.
                    case "CT":
                        optionCreateTask(input);
                        break;
                    // Edit Task.
                    case "ET":
                        optionEditTask(input);
                        break;
                    // Remove Task.
                    case "RT":
                        optionRemoveTask(input);
                        break;
                    default:
                        System.out.println("Unknown option.");
                        break;
                }
            }

        }

    }
}
