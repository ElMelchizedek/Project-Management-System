import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class UserInterface {
    /*
    !!! GLOBAL VARIABLES !!!
     */

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

    // *** Auxiliary methods. ***

    public static Project[] auxAddToArray(Project[] array, Project item) throws Exception {
        if (array[array.length - 1] != null) {
            throw new Exception("Array parameter passed to auxAddToArray() is full.");
        }

        int i = 0;
        for (; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = item;
                return array;
            }
        }

        return null;
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
    private static void auxViewProject(Project project, int[] chosenTasks) {
        if (project != null) {
            int amountTasks = project.amountTasks();
            if (amountTasks != 0) {
                System.out.println("Project ID: " + project.getProjectID() +
                        ", Project Name: " + project.getProjectName() +
                        ", Project Type: " + project.getProjectType() +
                        ", Num of tasks: " + amountTasks);
                System.out.println("Tasks:");

                for (Task task: project.getListTasks()) {
                    for (int ID: chosenTasks) {
                        if (task.getTaskID() == ID) {
                            auxTaskPrettyInfo(task, true);
                        }
                    }
                }

            } else { System.out.println("No Tasks to report for Project #" + project.getProjectID() + "."); }
        }
    }

    // DESC: Gets one of the three Project variables required by the specifications, using its ID.
    // USAGE: optionCreateTask().
    private static Project auxGetProjectByID(int ProjectID, Project[] listProjects) {
        Project certainProject = null;
        for (Project project: listProjects) {
            if (project != null && project.getProjectID() == ProjectID) {
                certainProject = project;
            }
        }

        return certainProject;
    }

    // DESC: Typical prompt request grabbing Project by ID, isolated due to being repeated in multiple methods.
    // USAGE: optionEditTask(), optionRemoveTask(), and dispCompleteTasks().
    private static Project auxDialogueGetProject(Scanner input, Project[] listProjects) {
        Project certainProject;
        System.out.print("Enter ID of Project: ");
        int ProjectID;
        while (true) {
            ProjectID = auxCheckInputValid(0, input);
            if (ProjectID == -1) { continue; }

            certainProject = auxGetProjectByID(ProjectID, listProjects);
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
    private static int[] auxFilterTypes(Project project, char type) throws Exception {
        int[] matchingTasks = null;

        int i = 0;
        if (project != null) {
            for (int j = 0; j < project.getListTasks().length; j++) {
                if (project.getListTasks()[j] != null && project.getListTasks()[j].getTaskType() == type) {
                    matchingTasks[i] = j;
                    i++;
                }
            }
        }

        if (matchingTasks == null) {
            throw new Exception("Could not find any matching Tasks in auxFilterTypes() for Project #" + project.getProjectID() + ".");
        }

        return matchingTasks;
    }

    // Apodictic.
    private static Task[] auxGetAllTasksMatchingType(Project project, String type) throws Exception {
        // Collate all Tasks matching type into one super-array.
        Task[] allTasks = new Task[project.getListTasks().length];
        int metaIncrement = 0;
        for (int i = 0; i < project.getListTasks().length; i++) {
            if (project.getListTasks()[i].getTaskType() == type.toUpperCase().charAt(0)) {
                allTasks[metaIncrement] = project.getListTasks()[i];
                metaIncrement++;
            }
        }
        if (metaIncrement == 0) {
            throw new Exception("No tasks matching type " + type + " were found in Project #" + project.getProjectID());
        }

        return allTasks;
    }

    // Gets the Sum of the specified tasks in the passed array.
    private static int auxGetSumOfMultipleTaskDurations(Task[] tasks) throws Exception {
        int sum = 0;
        for (Task task: tasks) {
            sum += task.getTaskDuration();
        }
        if (sum == 0) {
            throw new Exception("All tasks have no duration and so could not determine sum of their durations.");
        }
        return sum;
    }

    // Calculates the average duration of all Tasks of a certain type in a specific Project.
    private static double auxSpecificGetAverageTypeDurations(Project project, String type) throws Exception {
        Task[] matchingTasks = null;
        try {
            matchingTasks = auxGetAllTasksMatchingType(project, type);
        } catch (Exception e) {
//            System.out.println("ERROR: " + e.getMessage());
            return -1;
        }

        int sum = auxGetSumOfMultipleTaskDurations(matchingTasks);

        return ((double) sum / (double) matchingTasks.length);
    }

    // Calculates the average duration for all Tasks of a certain type across all Projects.
    private static double auxOverallGetAverageTypeDurations(Project[] listProjects, String type) throws Exception {
        // Calculate the amount of Projects in the Project list.
        int amountProjects = 0;
        for (int i = 0; i < listProjects.length; i++) {
            if (listProjects[i] != null) {
                amountProjects++;
            }
        }
        if (amountProjects == 0) {
            throw new Exception("No Projects to calculate from.");
        }

        // Generate array of average Task durations for each Project.
        double[] averagesArray = new double[amountProjects];
        int metaIncrement = 0;
        for (Project project: listProjects) {
            if (project != null) {
                double dummy = auxSpecificGetAverageTypeDurations(project, type);
                if (dummy != -1) {
                    averagesArray[metaIncrement] = dummy;
                    metaIncrement++;
                }
            }
        }
        if (metaIncrement == 0) {
//            throw new Exception("metaIncrement resulted in 0 in auxOverallGetAverageTypeDurations() for type " + type + ".");
            return Float.NaN;
        }

        // Calculate Absolute Overall duration for all Tasks for specified type across all Projects.
        double sumOfAverages = 0.0;
        for (int i = 0; i < metaIncrement; i++) {
            sumOfAverages += averagesArray[i];
        }
        if (sumOfAverages == 0) {
//            throw new Exception("sumOfAverages resulted in 0 in auxOverallGetAverageTypeDurations() for type " + type + ".");
            return Float.NaN;
        }

        return (sumOfAverages / (double) metaIncrement);

    }

    // DESC: Displays the average duration of Tasks within a Project, divided by type.
    // USAGE: dispAverageTypeDurations().
    public static void auxPrettyAverageTypeDurationsByProject(Project project) {
        String[][] typesCombo = {{"A", "administrative"}, {"L", "logistics"}, {"S", "support"}};

        if (project != null) {
            System.out.println("Project ID " + project.getProjectID() + ":");
            if (project.getListTasks().length != 0) {
                for (String[] combo : typesCombo) {
                    try {
                        double average = auxSpecificGetAverageTypeDurations(project, combo[0]);
                        if (average != -1.0) {
                            System.out.println("\t* Average task duration of the " + combo[1] + " Tasks is " + auxSpecificGetAverageTypeDurations(project, combo[0]) + ".");
                        } else {
                            System.out.println("\t* There are no " + combo[1] + " Tasks in the Project.");
                        }
                    } catch (Exception e) {
                        System.out.print("ERROR: " + e.getMessage());
                    }
                }
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
        } if (value > Integer.MAX_VALUE) {
            System.out.print("Inputted value was greater than the maximum value allowed for integers. Please try again: ");
            input.nextLine();
            return -1;
        }
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();
        return value;
    }

    public static boolean auxCheckIDExistsInProjectList(Project[] listProjects, int ID) {
        boolean found = false;

        for (Project project: listProjects) {
            if (project != null && project.getProjectID() == ID) {
                found = true;
                break;
            }
        }

        return found;
    }

    public static void auxViewCompleteTasks(Project certainProject) {
        System.out.println("Completed Tasks in Project #" + certainProject.getProjectID() + ".");
        for (Task task: certainProject.getListTasks()) {
            if (task != null && task.getCompleted()) {
                auxTaskPrettyInfo(task, false);
            }
        }
    }

    public static void auxViewFilteredTasks(String filter, Project[] listProjects) {
        int [] matchingTasks;

        for (Project project: listProjects) {
            if (project != null) {
                try {
                    matchingTasks = auxFilterTypes(project, filter.toUpperCase().charAt(0));
                    auxViewProject(project, matchingTasks);
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        }

    }

    public static Project[] auxLoadFile(String name) throws Exception {
        Scanner inputStream;
        String[] contents = new String[256];
        Project[] listProjects = new Project[10];

        System.out.println("Attempting to load file " + name + " into memory...");
        int i = 0;
        try {
            inputStream = new Scanner(new File(name));
            while (inputStream.hasNextLine()) {
                contents[i] = inputStream.nextLine();
                i++;
            }
            inputStream.close();
        } catch (Exception e) {
           System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }

        if (i == 0) {
            throw new Exception("Could not read any text from specified file.");
        }

        System.out.println("Attempting to parse file contents...");
        int currentProjectIndex = 0;
        for (String line: contents) {
            if (line != null) {
                String[] items = line.split(",");
                // Project
                if (items.length == 3) {
                    Project newProject = null;
                    try {
                        newProject = Project.createProject(listProjects,
                                Integer.parseInt(items[0]),
                                items[1],
                                items[2]);
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                        System.exit(1);
                    }
                    try {
                        listProjects = auxAddToArray(listProjects, newProject);
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                        System.exit(1);
                    }
                    currentProjectIndex++;
                // Task
                } else if (items.length == 4) {
                    try {
                        boolean completed;
                        String normalised = items[3].toUpperCase();
                        if (normalised.equals("TRUE")) {
                            completed = true;
                        } else if (normalised.equals("FALSE")) {
                            completed = false;
                        } else {
                            throw new Exception("Value for Task completion status in line of selected file is invalid.");
                        }
                        listProjects[currentProjectIndex - 1].createTask(Integer.parseInt(items[0]),
                                "No description",
                                items[1],
                                Integer.parseInt(items[2]),
                                listProjects,
                                completed);
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                        System.exit(1);
                    }
                }
            }
        }

        if (listProjects[0] == null) {
            throw new Exception("Could not load any Projects from specified file.");
        }

        return listProjects;
    }

    public static void auxSaveFile(String name, Project[] listProjects) throws Exception {
        try (PrintWriter outputStream = new PrintWriter(name)) {
            for (Project project: listProjects) {
                if (project == null) { continue; }
                outputStream.println(project.getProjectID() + "," + project.getProjectName() + "," + project.getProjectType());
                for (Task task: project.getListTasks()) {
                    if (task == null) { continue; }
                    outputStream.println(task.getTaskID() + "," + task.getTaskType() + "," + task.getTaskDuration() + "," + task.getCompleted());
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
    }


    // *** Display Submethods ***

    // DESC: Displaying All Project Details
    // USAGE: optionDisplay().
    public static void dispViewProjects(Project[] listProjects) {
        for (Project project : listProjects) {
            if (project != null) {
                int length = project.amountTasks();
                int[] IDs = new int[length];
                for (int i = 0; i < length; i++) {
                    IDs[i] = project.getListTasks()[i].getTaskID();
                }
                auxViewProject(project, IDs);
            }
        }
        System.out.print("\n");
    }

    // DESC: Displaying Completed Tasks
    // USAGE: optionDisplay().
    private static void dispCompleteTasks(Scanner input, Project[] listProjects) {
        Project certainProject = auxDialogueGetProject(input, listProjects);
        if (certainProject == null || certainProject.amountTasks() == 0) { return; }

        auxViewCompleteTasks(certainProject);
    }

    // DESC: Filtering Tasks by Type
    // USAGE: optionDisplay().
    private static void dispFilteredTasks(Scanner input, Project[] listProjects) {

        System.out.print("Enter the type to filter by ([A]dministrative, [L]ogistical, or [S]upport): ");
        String filter;
        while (true) {
            filter = auxCheckInputValid("", input);
            if (filter.equals(SECRET_STRING)) { continue; }

            boolean permissible = false;
            for (String realType: Task.permittedTypes) {
                if (filter.toUpperCase().equals(realType)) {
                    permissible = true;
                    break;
                }
            }
            if (!permissible) {
                System.out.print("ERROR: Invalid filter type entered. Please try again: ");
                continue;
            }
            break;
        }

        auxViewFilteredTasks(filter, listProjects);
    }

    // DESC: Average Task Type Durations
    // USAGE: optionDisplay().
    public static void dispAverageTypeDurations(Project[] listProjects) {
        String[][] types = {{"A", "administrative"}, {"L", "logistics"}, {"S", "support"}};

        System.out.println("---Average Task Duration---");
        System.out.println("Average task duration of all task types across all projects:");
        for (String[] type : types) {
            double duration = Float.NaN;
            try {
                duration = auxOverallGetAverageTypeDurations(listProjects, type[0]);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
            if (!Double.isNaN(duration)) {
                System.out.println("\t* Average task duration of all " + type[1] + " is " + duration + " hours.");
            } else {
                System.out.println("\t* There is no " + type[1] + " Tasks.");
            }
        }

        System.out.println("---Breakdown by Project---");
        for (Project project: listProjects) {
            auxPrettyAverageTypeDurationsByProject(project);
        }
    }



    // *** Methods invoked by user input ("Option Methods"). ***

    // DESC: Provides an interactive wizard to create a Project from.
    // USAGE: main()
    private static Project optionCreateProject(Scanner input, Project[] projects) throws Exception {
        // Variables
        Project tempProject = null;
        boolean finished = false;
        // Used to contain already inputted data in case of thrown exceptions from Project.createProject()
        int ID = -1;
        String name = SECRET_STRING;
        String type = SECRET_STRING;

        // Preliminary checks
        if (projects == null) {
            throw new Exception("Passed Projects list argument to optionCreateProject() has value null.");
        } else if (projects[9] != null) {
            throw new Exception("Passed Projects list argument to optionCreateProject() is full.");
        }

        System.out.println("*** Project Wizard ***");

        while (!finished) {
            // Input work.
            if (ID == -1) {
                System.out.print("Enter ID for new Project: ");
                while (true) {
                    ID = auxCheckInputValid(0, input);
                    if (ID == -1) {
                        continue;
                    }
                    break;
                }
            }

            if (name.equals(SECRET_STRING)) {
                System.out.print("Enter name for new project: ");
                while (true) {
                    name = auxCheckInputValid("", input);
                    if (name.equals(SECRET_STRING)) {
                        continue;
                    }
                    break;
                }
            }

            if (type.equals(SECRET_STRING)) {
                System.out.print("Enter type for new project (options: \"small\", \"medium\", or \"large\"): ");
                while (true) {
                    type = auxCheckInputValid("", input);
                    if (type.equals(SECRET_STRING)) {
                        continue;
                    }
                    break;
                }
            }

            // Actual creation, making sure to check for exceptions.
            try {
                tempProject = Project.createProject(projects, ID, name, type);
                finished = true;
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                System.out.println("Re-attempting creation...");
            }
        }

        return tempProject;
    }

    // DESC: Provides an interactive wizard to remove a Project from.
    // USAGE: main().
    private static Project[] optionRemoveProject(Scanner input, Project[] listProjects) throws Exception {
        // Preliminary checks
        if (listProjects == null || listProjects[0] == null) {
            throw new Exception("Projects list parameter passed to optionRemoveProject() is empty.");
        }

        Project[] newList = new Project[10];

        System.out.println("*** Project Removal Wizard ***");

        System.out.print("Please enter ID of Project to be removed: ");
        int ID;
        while (true) {
            ID = auxCheckInputValid(0, input);
            if (ID == -1) { continue; }
            // Check that Project with said ID in fact exists.
            if (auxGetProjectByID(ID, listProjects) == null) {
                System.out.print("ERROR: No know Project has inputted ID. Please try again: ");
                continue;
            }
            break;
        }

        int j = 0;
        for (Project project: listProjects) {
            int index = -1;

            if (project != null) {
                if (project.getProjectID() != ID) {
                    newList[j] = project;
                    j++;
                }
            }
        }

        System.out.println("Project #" + ID + " removed successfully.");

        return newList;
    }

    // DESC: Provides an interactive wizard to create a Task from.
    // USAGE: main().
    private static void optionCreateTask(Scanner input, Project[] listProjects) {
        Project certainProject = null;
        boolean finished = false;
        // Pre-initialised parameter for Project.createTask().
        int newID = -1;
        String newDescription = SECRET_STRING;
        String newType = SECRET_STRING;
        int newDuration = -1;
        int ProjectID = -1;

        System.out.println("*** Task Wizard ***");

        while (!finished) {

            if (ProjectID == -1) {
                System.out.print("Enter ID of Project you would like to a create a Task for: ");
                while (true) {
                    ProjectID = auxCheckInputValid(0, input);
                    if (ProjectID == -1) {
                        continue;
                    }
                    certainProject = auxGetProjectByID(ProjectID, listProjects);
                    if (certainProject == null) {
                        System.out.print("ERROR: Inputted Project ID does not match any existing Project. Please try again: ");
                        continue;
                    } else if (certainProject.checkListTasksFull()) {
                        System.out.println("ERROR: Inputted Project ID corresponds to Project which cannot contain any additional Tasks. Aborting...");
                        return;
                    }
                    break;
                }
            }

            if (newID == -1) {
                System.out.print("Enter ID for new Task: ");
                while (true) {
                    newID = auxCheckInputValid(0, input);
                    if (newID == -1) {
                        continue;
                    }
                    break;
                }
            }

            if (newDescription.equals(SECRET_STRING)) {
                System.out.print("Enter a brief description of the new Task: ");
                while (true) {
                    newDescription = auxCheckInputValid("", input);
                    if (newDescription.equals(SECRET_STRING)) {
                        continue;
                    }
                    break;
                }
            }

            if (newType.equals(SECRET_STRING)) {
                System.out.print("Enter the Task type ([A]dministrative, [L]ogistics, or [S]upport: ");
                while (true) {
                    newType = auxCheckInputValid("", input);
                    if (newType.equals(SECRET_STRING)) {
                        continue;
                    }
                    break;
                }
            }

            if (newDuration == -1) {
                System.out.print("Enter estimated or actual type spent on new Task: ");
                while (true) {
                    newDuration = auxCheckInputValid(0, input);
                    if (newDuration == -1) {
                        continue;
                    }
                    break;
                }
            }


            try {
                certainProject.createTask(newID, newDescription, newType, newDuration, listProjects, false);
                finished = true;
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

    }

    // DESC: Provides an interactive wizard to edit a Task from.
    // NOTE: Really just a "mark Task completed" function.
    // USAGE: main().
    private static void optionEditTask(Scanner input, Project[] listProjects) {
        Task certainTask = null;
        Project certainProject = null;
        boolean finished = false;

        int ProjectID = -1;
        int TaskID = -1;

        while (!finished) {
            if (ProjectID == -1) {
                System.out.print("Please enter ID of Project containing Task to be edited: ");
                while (true) {
                    ProjectID = auxCheckInputValid(0, input);
                    if (ProjectID == -1) {
                        continue;
                    }
                    certainProject = auxGetProjectByID(ProjectID, listProjects);
                    if (certainProject == null) {
                        System.out.print("ERROR: Inputted Project ID does not match any existing Project. Please try again: ");
                        continue;
                    } else if (certainProject.amountTasks() == 0) {
                        System.out.print("ERROR: Inputted Project ID references Project that does not contain any Tasks. Please try again: ");
                        continue;
                    }
                    break;
                }
            }

            if (TaskID == -1) {
                System.out.print("Enter ID of Task to be edited: ");
                while (true) {
                    TaskID = auxCheckInputValid(0, input);
                    if (TaskID == -1) {
                        continue;
                    }

                    try {
                        certainTask = certainProject.retrieveTaskByID(TaskID);
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                        return;
                    }
                    break;
                }
            }

            System.out.print("Would you like to edit the completed status of the selected Task? (currently: " + certainTask.status() + ") [y/n] ");
            String choice;
            while (true) {
                choice = auxCheckInputValid("", input);
                if (choice.equals(SECRET_STRING)) {
                    continue;
                }
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
    }

    // DESC: Provides an interactive wizard to remove a Task from.
    // USAGE: main().
    private static void optionRemoveTask(Scanner input, Project[] listProjects) {
        Project certainProject = auxDialogueGetProject(input, listProjects);
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
    private static void optionDisplay(Scanner input, Project[] listProjects) {
        String options = """ 
                        Options for Display:
                        \t *1: Exit to Project Management System.
                        \t *2: All Project details.
                        \t *3: Completed Tasks within Project.
                        \t *4: Tasks of all Projects per type filter provided by user.
                        \t *5: Average duration of Tasks arranged by type.
                        """;

        System.out.println("*** Display Wizard ***");

        while (true) {
            System.out.print(options);
            System.out.print("? ");
            int line = auxCheckInputValid(0, input);
            if (line == -1) { continue; }
            switch (line) {
                case 2:
                    dispViewProjects(listProjects);
                    break;
                case 3:
                    dispCompleteTasks(input, listProjects);
                    break;
                case 4:
                    dispFilteredTasks(input, listProjects);
                    break;
                case 5:
                    dispAverageTypeDurations(listProjects);
                    break;
                case 1:
                    return;
                default:
                    System.out.println("Unknown option.");
                    break;
            }
        }
    }

    private static Project[] optionLoad(Scanner input) throws Exception {
        System.out.println("*** FILE LOAD WIZARD ***");
        Project[] listProjects = new Project[10];

        // Get file name
        System.out.print("Please enter the file name (extension included): ");
        String name = "";
        while (true) {
            name = auxCheckInputValid("", input);
            if (name.equals(SECRET_STRING)) {
                continue;
            }
            break;
        }

        try {
            listProjects = auxLoadFile(name);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }

        if (listProjects == null) {
            throw new Exception("listProjects returned null from auxLoadFile().");
        }

        System.out.print("\n");
        return listProjects;
    }

    private static void optionSave(Scanner input, Project[] listProjects) {
        System.out.println("*** FILE SAVE WIZARD ***");

        // Get chosen file name
        System.out.print("Please enter the name for the file that is to be saved (extension included): ");
        String name = "";
        while (true) {
            name = auxCheckInputValid("", input);
            if (name.equals(SECRET_STRING)) {
                continue;
            }
            break;
        }

        try {
            auxSaveFile(name, listProjects);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }

        System.out.print("\n");
    }


    // *** Main entry method. ***
    public static void main(String[] args) {
        // Debugging toggles.
        boolean debug = false;
        boolean projectCreate = false;
        boolean taskCreate = false;
        boolean viewProjects = false;
        boolean viewCompleteTasks = false;
        boolean viewFilteredTasks = false;
        boolean viewAverageTypeDurations = false;

        Project[] projects = new Project[10];

        String help = """
                Options (case-insensitive):
                \t * Q: quit\s
                \t* CP: create project
                \t* D: display info
                \t* RP: remove project
                \t* CT: create task
                \t* ET: edit task
                \t* RT: remove task
                \t* LF: load file
                \t* SF: save file
                """;

        Scanner input = new Scanner(System.in);

        // Debug stuff
        if (debug) {
            if (projectCreate) {
                projects = Tests.debugProjectCreate(projects);
            }
            if (taskCreate) {
                projects = Tests.debugTaskCreate(projects);
            }
            if (viewProjects) {
                projects = Tests.debugViewProjects(projects);
            }
            if (viewCompleteTasks) {
                projects = Tests.debugViewCompleteTasks(projects);
            }
            if (viewFilteredTasks) {
                projects = Tests.debugViewFilteredTasks(projects);
            }
            if (viewAverageTypeDurations) {
                projects = Tests.debugViewAverageTypeDurations(projects);
            }
        }

        System.out.println("———Project Management System———");

        while (true && (!debug)) {
            System.out.print(help);
            System.out.print("> ");
            String line = auxCheckInputValid("", input);
            if (line.equals(SECRET_STRING)) { continue; }
            if (line != null) {
                switch (line.toUpperCase()) {
                    // Quit.
                    case "Q":
                        System.exit(0);
                        break;
                    // Create Project.
                    case "CP":
                        Project newProject = null;
                        try {
                            newProject = optionCreateProject(input, projects);
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e.getMessage());
                        }
                        if (newProject == null) {
                            System.out.println("ERROR: Unknown error failed Project creation.");
                        } else {
                            Project[] tempProjects;
                            try {
                                tempProjects = auxAddToArray(projects, newProject);
                            } catch (Exception e) {
                                System.out.println("ERROR: " + e.getMessage());
                                return;
                            }
                            if (tempProjects == null) {
                                System.out.println("ERROR: Failed to add new Project to array.");
                            }
                        }
                        break;
                    // Display Info.
                    case "D":
                        optionDisplay(input, projects);
                        break;
                    // Remove Project.
                    case "RP":
                        try {
                            projects = optionRemoveProject(input, projects);
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e.getMessage());
                        }
                        break;
                    // Create Task.
                    case "CT":
                        optionCreateTask(input, projects);
                        break;
                    // Edit Task.
                    case "ET":
                        optionEditTask(input, projects);
                        break;
                    // Remove Task.
                    case "RT":
                        optionRemoveTask(input, projects);
                        break;
                    // Load File.
                    case "LF":
                        try {
                            projects = optionLoad(input);
                        } catch (Exception e) {
                            System.out.println("ERROR: " + e.getMessage());
                            System.exit(1);
                        }
                        break;
                    // Save File.
                    case "SF":
                        optionSave(input, projects);
                        break;
                    default:
                        System.out.println("Unknown option.");
                        break;
                }
            }

        }

    }
}
