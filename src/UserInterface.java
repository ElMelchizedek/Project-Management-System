import java.util.Scanner;

public class UserInterface {
    /*
    !!! GLOBAL VARIABLES !!!
     */

    // *** Private variables to be accessed and manipulated via setters and getters. ***
    private static Project project1 = null;
    private static Project project2 = null;
    private static Project project3 = null;

    // Constant needed for auxCalculateAverageDuration(), as a way to distinguish calls made to calculate durations for
    // a specific project, or for durations of a certain type across all projects.
    public static final double SECRET_NUMBER = 5051818;
    // Constant needed for auxCheckInputValid(String) to check if it failed a check.
    // Pay no attention to its contents: It is like this to remove the possibility of a false positive.
    public static final String SECRET_STRING =  """
                                                The question whether objective truth can be attributed to human thinking is not a question of theory but is a practical question.
                                                Man must prove the truth, i.e., the reality and power, the this-sidedness [Diesseitigkeit] of his thinking, in practice.
                                                The dispute over the reality or non-reality of thinking which is isolated from practice is a purely scholastic question.
                                                +\
                                                """;


    /*
    !!! METHODS !!!
     */

    // *** Debug Methods ***
    // DESC: Initialises three Projects, each being assigned exclusively one of the three sizes available
    // And then having Tasks assigned to themselves.
    // USAGE: main().
    private static String debugInitialise(String IDsProjects) {
        System.out.println("*** INITIALISING ***");
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

    // DESC: Attempts to perform a stress test, however too late into development did I realise I exclusively
    // wrote many of the methods regarding the management of Projects and Tasks to require human input,
    // and not wanting to refactor the whole codebase I simply accepted this lackluster version of a "test". 
    // USAGE: main().
    private static boolean debugTest() {
        System.out.println("*** STRESS TEST ***");
        project1 = new Project(0, "Test project", "Large");
        project1.createTask(1, "Valid ID", "A", 1, false);
        project1.deleteTask(999);
        project1.createTask(2, "Test capacity", "A", 1, false);
        project1.createTask(3, "Test capacity", "A", 1, false);
        project1.createTask(4, "Test capacity", "A", 1, false);
        project2 = new Project(99, "Empty", "Medium");
        auxPrettyAverageTypeDurationsByProject(project2, 99);
        return true;
    }

    // *** Auxiliary methods. ***

    // DESC: Prints an acknowledgement to the user of creation and then returns IDsProjects modified to have the
    // new Project's ID appended. 
    // USAGE: optionCreateProject().
    private static String auxAssignProject(int ID, String type, String valueID, String IDsProjects) {
        System.out.println("Created Project #" + ID + " of type " + type + ".");
        return IDsProjects.concat(valueID + ",");
    }

    // USAGE: Display info of the specified Task.
    // USAGE: auxViewProject().
    private static void auxTaskPrettyInfo(Task task, boolean includeStatus) {
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

    // DESC: Displays details of a selected Project and the Tasks assigned to it.
    // USAGE: optionViewProjects() and dispFilteredTasks().
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

    // DESC: Gets one of the three Project variables required by the specifications, using its ID.
    // USAGE: optionCreateTask().
    private static Project auxGetProjectByID(int ProjectID) {
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

    // DESC: Typical prompt request grabbing Project by ID, isolated due to being repeated in multiple methods.
    // USAGE: optionEditTask(), optionRemoveTask(), and dispCompleteTasks().
    private static Project auxDialogueGetProject(Scanner input) {
        Project certainProject;
        System.out.print("Enter ID of Project: ");
        int ProjectID;
        while (true) {
            ProjectID = auxCheckInputValid(0, input);
            if (ProjectID == -1) { continue; }

            certainProject = auxGetProjectByID(ProjectID);
            if (certainProject == null) {
                System.out.print("ERROR: Entered Project ID does not match any existing Project. Please try again: ");
                continue;
            }
            break;
        }
        return certainProject;
    }

    // DESC: Returns a String containing numbers indicating which Tasks in a Project have the type specified.
    // E.g., if a Project's task1 and task3 have the specified type, then the String returned will be "13".
    // USAGE: dispFilteredTasks() and auxGetAverageTypeDurations().
    private static String auxFilterTypes(Project project, char type) {
        String matchingTasks = "";

        if (project != null) {
            for (int i = 1; i <= 3; i++) {
                if (project.getTask(i) != null
                && project.getTask(i).getTaskType() == type) {
                   matchingTasks = matchingTasks.concat(String.valueOf(i));
                }
            }
        }

        return matchingTasks;
    }

    // DESC: Calculates the average duration of Tasks within a Project that match a specified type, as inidicated by the
    // matches parameter. 
    // USAGE: auxGetAverageTypeDurations().
    private static double auxCalculateAverageDuration(Project project, String matches, int stopAtProject) {
        int sumDurations = 0;
        int amountMatching = 0;
        if (matches.contains("1")) { sumDurations += project.getTask(1).getTaskDuration(); amountMatching++; }
        if (matches.contains("2")) { sumDurations += project.getTask(2).getTaskDuration(); amountMatching++; }
        if (matches.contains("3")) { sumDurations += project.getTask(3).getTaskDuration(); amountMatching++; }
        // Here I do some funky stuff since I can't just return an array, so I need to be able to distinguish different
        // outcomes whilst not being able to have multiple return types for the method.
        // To differentiate I do this by multiplying the number by a defined power of 10 that is then checked by the
        // calling function to figure out what kind of return value it is from this function.
        if (stopAtProject != 0) {
            return (((double)sumDurations / (double)amountMatching) * SECRET_NUMBER);
        } else { return sumDurations; }
    }

    // Wrapper for auxCalculateAverageDuration().
    // DESC: It allows for both the calculating of average Task durations per a specified Type within a certain Project,
    // (indicated by the stopAtProject parameter) or for all Projects overall.
    // USAGE: dispAverageTaskDurations() and auxPrettyAverageTypeDurationsByProject().
    private static double auxGetAverageTypeDurations(char type, int stopAtProject) {
        int amountMatching = 0;
        int sumDurations = 0;

        // I wish I had arrays right now.
        String matches = auxFilterTypes(project1, type);
        double result   = auxCalculateAverageDuration(project1, matches, stopAtProject);
        // Cf. comments within auxCalculateAverageDuration to understand why I am doing these eccentric calculations on the return value.
        if (project1 != null && stopAtProject == project1.getProjectID()) {
            return (result / SECRET_NUMBER);
        }
        if (!matches.isEmpty()) {
                sumDurations += (int) result;
                amountMatching += matches.length();
        }

        matches = auxFilterTypes(project2, type);
        result          = auxCalculateAverageDuration(project2, matches, stopAtProject);
        if (project2 != null && stopAtProject == project2.getProjectID()) {
            return (result / SECRET_NUMBER);
        }
        if (!matches.isEmpty()) {
                sumDurations += (int) result;
                amountMatching += matches.length();
        }

        matches = auxFilterTypes(project3, type);
        result          = auxCalculateAverageDuration(project3, matches, stopAtProject);
        if (project3 != null && stopAtProject == project3.getProjectID()) {
            return (result / SECRET_NUMBER);
        }
        if (!matches.isEmpty()) {
                sumDurations += (int) result;
                amountMatching += matches.length();
        }

        return ((double) sumDurations / (double) amountMatching);
    }

    // DESC: Displays the average duration of Tasks within a Project, divided by type.
    // USAGE: dispAverageTypeDurations().
    private static void auxPrettyAverageTypeDurationsByProject(Project project, int num) {
        if (project != null) {
            System.out.println("Project ID " + project.getProjectID() + ":");
            if (!project.getAllTaskIDs().equals("")) {
                System.out.println("\t* Average task duration of administrative tasks is " + auxGetAverageTypeDurations('A', num) + ".");
                System.out.println("\t* Average task duration of logistics tasks is " + auxGetAverageTypeDurations('L', num) + ".");
                System.out.println("\t* Average task duration of support tasks is " + auxGetAverageTypeDurations('S', num) + ".");
            } else {
                System.out.println("\t* No created tasks to report.");
            }
        }
    }

    // DESC: Checks for common edge-cases regarding input, both integers and Strings.
    // NOTE: Multiple overloads of the method to handle different types of data being checked.
    // Yes data is a redundant variable that is unused, but I don't know how to make a method have a generic return
    // type, so I'm just going to leave them in there.
    // USAGE: auxDialogueGetProject(), dispFilteredTasks(), optionCreateProject(), optionCreateTask(), optionEditTask(), and main().
    private static String auxCheckInputValid(String data, Scanner input) {
        if (!input.hasNextLine()) {
            System.out.print("Value entered was not a valid name. Please try again: ");
            return SECRET_STRING;
        }
        String value = input.nextLine();
        if (value.isEmpty()) {
            System.out.print("Empty input given. Please try again: ");
            return SECRET_STRING;
        }
        return value;
    }
    private static int auxCheckInputValid(int data, Scanner input) {
        if (!input.hasNextInt()) {
            System.out.print("Value entered was not a valid number (integer). Please try again: ");
            input.nextLine();
            return -1;
        }
        int value = input.nextInt();
        if (value < 0) {
            System.out.print("Negative values are not permitted. Please try again: ");
            input.nextLine();
            return -1;
        }
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();
        return value;
    }



    // *** Display Submethods ***

    // DESC: Displaying All Project Details
    // USAGE: optionDisplay().
    private static void dispViewProjects() {
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

    // DESC: Displaying Completed Tasks
    // USAGE: optionDisplay().
    private static void dispCompleteTasks(Scanner input) {
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

    // DESC: Filtering Tasks by Type
    // USAGE: optionDisplay().
    private static void dispFilteredTasks(Scanner input) {
        String matchingTasks;

        System.out.print("Enter the type to filter by ([A]dministrative, [L]ogistical, or [S]upport): ");
        String filter;
        while (true) {
            filter = auxCheckInputValid("", input);
            if (filter.equals(SECRET_STRING)) { continue; }
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

    // DESC: Average Task Type Durations
    // USAGE: optionDisplay().
    private static void dispAverageTypeDurations() {
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



    // *** Methods invoked by user input ("Option Methods"). ***

    // DESC: Provides an interactive wizard to create a Project from.
    // USAGE: main()
    private static String optionCreateProject(Scanner input, int amountProjects, String IDsProjects) {
        // Variables
        String[] permittedTypes = {"SMALL", "MEDIUM", "LARGE"};
        Project tempProject = null;

        System.out.println("*** Project Wizard ***");

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
           if (name.equals(SECRET_STRING)) { continue; }
           break;
        }

        System.out.print("Enter type for new project (options: \"small\", \"medium\", or \"large\"): ");
        String type;
        while(true) {
            type = auxCheckInputValid("", input);
            if (type.equals(SECRET_STRING)) { continue; }

            for (int i = 0; i <= permittedTypes.length; i++) {
                if (tempProject != null && type.toUpperCase().equals(permittedTypes[i])) {
                    try {
                        tempProject = new Project(ID, name, permittedTypes[i]);
                        break;
                    } catch (Exception e){
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
            }
            if (tempProject == null) {
                System.out.println("Unknown Project type. Please try again: ");
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

    // DESC: Provides an interactive wizard to remove a Project from.
    // USAGE: main().
    private static void optionRemoveProject(Scanner input) {
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

    // DESC: Provides an interactive wizard to create a Task from.
    // USAGE: main().
    private static void optionCreateTask(Scanner input) {
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
            if (newDescription.equals(SECRET_STRING)) { continue; }
            break;
        }

        System.out.print("Enter the Task type ([A]dministrative, [L]ogistics, or [S]upport: ");
        String newType;
        while (true) {
            newType = auxCheckInputValid("", input);
            if (newType.equals(SECRET_STRING)) { continue; }
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

        try {
            certainProject.createTask(newID, newDescription, newType, newDuration, false);
            System.out.println("Created Task #" + newID + " of type " + newType + " lasting " + newDuration + "h, assigned to Project #" + newID + ".");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // DESC: Provides an interactive wizard to edit a Task from.
    // NOTE: Really just a "mark Task completed" function.
    // USAGE: main().
    private static void optionEditTask(Scanner input) {
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

        System.out.print("Would you like to edit the completed status of the selected Task? (currently: " + certainTask.status() + ") [y/n] ");
        String choice;
        while(true) {
            choice = auxCheckInputValid("", input);
            if (choice.equals(SECRET_STRING)) { continue; }
            choice = choice.toLowerCase();

            if (choice.equals("y")) {
                certainTask.setCompleted(!certainTask.getCompleted());
                System.out.println("Status of Task #" + certainTask.getTaskID()
                        + " of Project #" + certainProject.getProjectID()
                        + " is now " + certainTask.status() + ".");
            } else {
                System.out.println("Exhausted all attributes of Task to edit. Returning...");
            }
            return;
        }
    }

    // DESC: Provides an interactive wizard to remove a Task from.
    // USAGE: main().
    private static void optionRemoveTask(Scanner input) {
        Project certainProject = auxDialogueGetProject(input);
        if (certainProject == null) { return; }

        System.out.print("Please enter ID of Task to be removed: ");
        int TaskID;
        while (true) {
            TaskID = auxCheckInputValid(0, input);
            if (TaskID == -1) { continue; }
            break;
        }

        int projectID;
        try {
            projectID = certainProject.deleteTask(TaskID);
            System.out.println("Task #" + TaskID + " of Project #" + projectID + " deleted.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    // DESC: Interactive portal used to allow the user to interface with the 'disp' methods
    // USAGE: main().
    private static void optionDisplay(Scanner input) {
        String options = """ 
                        Options for Display:
                        \t *0: Print this dialogue again.
                        \t *1: Exit to Project Management System.
                        \t *2: All Project details.
                        \t *3: Completed Tasks within Project.
                        \t *4: Tasks of all Projects per type filter provided by user.
                        \t *5: Average duration of Tasks arranged by type.
                        """;

        System.out.println("*** Display Wizard ***");
        System.out.print(options);

        while (true) {
            System.out.print("? ");
            int line = auxCheckInputValid(0, input);
            if (line == -1) { continue; }
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



    // *** Main entry method. ***
    public static void main(String[] args) {
        // Debugging toggles.
        boolean init = false;
        boolean test = false;

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
        if (init) {
            IDsProjects = debugInitialise(IDsProjects);
            amountProjects = 3;
        }
        if (test) {
            if (debugTest()) {
                System.out.println("! Debug stress test done.");
            }
            project1 = null;
            project2 = null;
            project3 = null;
        }

        System.out.println("Project Management System");
        System.out.print(help);

        while (true) {
            System.out.print("> ");
            String line = auxCheckInputValid("", input);
            if (line.equals(SECRET_STRING)) { continue; }
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
