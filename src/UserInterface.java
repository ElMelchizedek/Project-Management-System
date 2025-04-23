import java.util.Scanner;

public class UserInterface {
    // *** Private variables to be accessed and manipulated via setters and getters. ***
    private static Project project1 = null;
    private static Project project2 = null;
    private static Project project3 = null;

    // Constant needed for auxCalculateAverageDuration().
    public static final double SECRET_NUMBER = 100000000;
    public static final double SECRET_DIGITS = 9;

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
        }
        System.out.print("\n");
    }

    // Used in optionViewProjects() and dispFilteredTasks().
    private static void auxViewProject(Project project, String chosenTasks) {
        if (project != null) {
            int amountTasks = project.amountTasks();
            System.out.println("Project ID: " + project.getProjectID() +
                    ", Project Name: " + project.getProjectName() +
                    ", Project Type: " + project.getProjectType() +
                    ", Num of tasks: " + amountTasks);
            System.out.println("Tasks:");
            if (chosenTasks.contains("1")) { auxTaskPrettyInfo(project.getTask(1), true); }
            if (chosenTasks.contains("2")) { auxTaskPrettyInfo(project.getTask(2), true); }
            if (chosenTasks.contains("3")) { auxTaskPrettyInfo(project.getTask(3), true); }
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
        int ProjectID = input.nextInt();
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();
        certainProject = auxGetProjectByID(ProjectID);
        if (certainProject == null) { System.out.println("ERROR: Entered Project ID does not match any existing Project. Aborting..."); return null; }
        return certainProject;
    }

    // Used in dispFilteredTasks() and auxGetAverageTypeDurations().
    public static String auxFilterTypes(Project project, char type) {
        String matchingTasks = "";
        if (project.getTask(1).getTaskType() == type) { matchingTasks = matchingTasks.concat("1"); }
        if (project.getTask(2).getTaskType() == type) { matchingTasks = matchingTasks.concat("2"); }
        if (project.getTask(3).getTaskType() == type) { matchingTasks = matchingTasks.concat("3"); }

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



    // *** Methods invoked by user input ("Option Methods"). ***
    public static String optionCreateProject(Scanner input, int amountProjects, String IDsProjects) {
        System.out.println("*** Project Wizard ***");
        Project tempProject;

        if (amountProjects >= 3) {
            System.out.println("ERROR: Maximum amount of concurrent projects already reached! Aborting project creation process...");
            return "";
        }

        System.out.print("Enter ID for new Project: ");
        int ID = input.nextInt();
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();
        String valueID = String.valueOf(ID);
        if (IDsProjects.contains(valueID + ",")) {
            System.out.println("ERROR: Duplicate project ID! Aborting project creation process...");
            return "";
        }

        System.out.print("Enter name for new project: ");
        String name = input.nextLine();

        System.out.print("Enter type for new project (options: \"small\", \"medium\", or \"large\"): ");
        String type = input.nextLine();
        switch(type.toUpperCase()) {
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
                System.out.println("ERROR: Unknown project type. Aborting project creation process...");
                return "";
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
        int ID = input.nextInt();
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();

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
        int ProjectID = input.nextInt();
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();
        certainProject = auxGetProjectByID(ProjectID);
        if (certainProject == null) { System.out.println("ERROR: Inputted Project ID does not match any existing Project. Aborting..."); return; }


        System.out.print("Enter ID for new Task: ");
        int newID = input.nextInt();
        input.nextLine();
        String valueID = String.valueOf(newID);
        if (certainProject.getAllTaskIDs() != null && certainProject.getAllTaskIDs().contains(valueID + ",")) {
            System.out.println("ERROR: Duplicate Task ID! Aborting...");
            return;
        }

        System.out.print("Enter a brief description of the new Task: ");
        String newDescription = input.nextLine();

        System.out.print("Enter the Task type ([A]dministrative, [L]ogistics, or [S]upport: ");
        String newType = input.nextLine().toUpperCase();
        if (newType.length() != 1) {
            System.out.println("ERROR: Task type was not specified with a single character. Aborting...");
            return;
        }
        if (!"ALS".contains(newType)) {
            System.out.println("ERROR: Specified Task type is not [A]dministrative, [L]ogistics, or [S]upport. Aborting...");
            return;
        }

        System.out.print("Enter estimated or actual type spent on new Task: ");
        int newDuration = input.nextInt();
        input.nextLine();

        certainProject.createTask(newID, newDescription, newType, newDuration, false);
    }

    // Really just the "mark task completed" function.
    public static void optionEditTask(Scanner input) {
        Task certainTask;
        Project certainProject = auxDialogueGetProject(input);
        if (certainProject == null) { return; }

        System.out.print("Enter ID of Task to be edited: ");
        int TaskID = input.nextInt();
        input.nextLine();
        certainTask = certainProject.retrieveTaskByID(TaskID);
        if (certainTask == null) { System.out.println("ERROR: Entered Task ID does not match any existing Task. Aborting..."); return; }

        System.out.print("Would you like to edit the completed status of the selected Task? (currently: " + certainTask.status() + ") [y/n]");
        String choice = input.nextLine().toLowerCase();
        if (choice.equals("y")) {
            certainTask.setCompleted(!certainTask.getCompleted());
            System.out.print("Status of Task #" + certainTask.getTaskID() + " of Project #" + certainProject.getProjectID() + " is now " + certainTask.status() + ".");
        } else {
            System.out.println("Exhausted all attributes of Task to edit. Returning...");
        }
    }

    public static void optionRemoveTask(Scanner input) {
        Project certainProject = auxDialogueGetProject(input);
        if (certainProject == null) { return; }

        System.out.print("Please enter ID of Task to be removed: ");
        int TaskID = input.nextInt();
        input.nextLine();
        certainProject.deleteTask(TaskID);
    }

    public static void optionDisplay(Scanner input) {
        String options = """ 
                        Options for Display:
                        \t *0: Exit to Project Management System.
                        \t *1: All Project details.
                        \t *2: Completed Tasks within Project.
                        \t *3: Tasks of all Projects filtered by type.
                        \t *4: Average duration of Tasks filtered by type.
                        """;

        System.out.println("*** Display Wizard ***");
        System.out.print(options);

        while (true) {
            System.out.print("? ");
            int line = input.nextInt();
            input.nextLine();
            switch (line) {
                case 1:
                    dispViewProjects();
                    break;
                case 2:
                    dispCompleteTasks(input);
                    break;
                case 3:
                    dispFilteredTasks(input);
                    break;
                case 4:
                    dispAverageTypeDurations();
                    break;
                case 0:
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
        if (task1.getCompleted()) { auxTaskPrettyInfo(task1, false); }
        if (task2.getCompleted()) { auxTaskPrettyInfo(task2, false); }
        if (task3.getCompleted()) { auxTaskPrettyInfo(task3, false); }

    }

    // Filtering Tasks by Type
    public static void dispFilteredTasks(Scanner input) {
        String matchingTasks;
        
        System.out.print("Enter the type to filter by ([A]dministrative, [L]ogistical, or [S]upport): ");
        String filter = input.nextLine();

        if (filter.length() == 1 || !Task.permittedTypes.contains(filter)) {
            System.out.print("ERROR: Invalid filter type entered. Aborting...");
            return;
        }
    
        matchingTasks = auxFilterTypes(project1, filter.charAt(0));
        auxViewProject(project1, matchingTasks);
        System.out.println("--------------------------------------------------------------------------------------------");
        matchingTasks = auxFilterTypes(project2, filter.charAt(0));
        auxViewProject(project2, matchingTasks);
        System.out.println("--------------------------------------------------------------------------------------------");
        matchingTasks = auxFilterTypes(project3, filter.charAt(0));
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
                \t* DP: display info
                \t* RP: remove project
                \t* CT: create task
                \t* ET: edit task
                \t* RT: remove task
                """;

        Scanner input = new Scanner(System.in);

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
