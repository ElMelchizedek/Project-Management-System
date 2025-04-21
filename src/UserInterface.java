import java.util.Scanner;

public class UserInterface {
    // Private variables to be accessed and manipulated via setters and getters.
    private static Project project1 = null;
    private static Project project2 = null;
    private static Project project3 = null;

    // Auxiliary methods.
    // Used in optionCreateProject().
    private static String auxAssignProject(Project target, Project source, int ID, String type, String valueID, String IDsProjects) {
        System.out.println("Created Project #" + ID + " of type " + type + ".");
        return IDsProjects.concat(valueID + ",");
    }

    // Used in auxViewProject().
    public static void auxTaskPrettyInfo(Task task) {
        if (task != null) {
            System.out.println("\t* Task ID: " + task.getTaskID() +
                    ", Description: " + task.getDescription() +
                    ", Type: " + task.getTaskType() +
                    ", Duration: " + task.getTaskDuration() +
                    ", Status: " + task.status());
        }
    }

    // Used in optionViewProjects().
    private static void auxViewProject(Project project) {
        if (project != null) {
            int amountTasks = project.amountTasks();
            System.out.println("Project ID: " + project.getProjectID() +
                    ", Project Name: " + project.getProjectName() +
                    ", Project Type: " + project.getProjectType() +
                    ", Num of tasks: " + amountTasks);
            System.out.println("Tasks:");
            auxTaskPrettyInfo(project.getTask(1));
            auxTaskPrettyInfo(project.getTask(2));
            auxTaskPrettyInfo(project.getTask(3));
        }
    }

    // Methods invoked by user input ("Option Methods").
    public static String optionCreateProject(Scanner input, int amountProjects, String IDsProjects) {
        System.out.println("*** Project Wizard ***");
        Project tempProject = null;

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
            return auxAssignProject(project1, tempProject, ID, type, valueID, IDsProjects);
        } else if (project2 == null) {
            project2 = tempProject;
            return auxAssignProject(project2, tempProject, ID, type, valueID, IDsProjects);
        } else if (project3 == null) {
            project3 = tempProject;
            return auxAssignProject(project3, tempProject, ID, type, valueID, IDsProjects);
        }

        return "";
    }

    public static void optionViewProjects() {
        if (project1 != null) {
            auxViewProject(project1);
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        if (project2 != null) {
            auxViewProject(project2);
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        if (project3 != null) {
            auxViewProject(project3);
        }
    }

    public static void optionCreateTask(Scanner input) {
        System.out.println("*** Task Wizard ***");
        Task tempTask = null;
        Project certainProject = null;

        System.out.print("Enter ID of Project you would like to a create a Task for: ");
        int ProjectID = input.nextInt();
        // Need to do this otherwise it won't be able to read the input for the name because of a hanging newline.
        input.nextLine();
        if (project1 != null && project1.getProjectID() == ProjectID) {
            certainProject = project1;
        } else if (project2 != null && project2.getProjectID() == ProjectID) {
            certainProject = project2;
        } else if (project3 != null && project3.getProjectID() == ProjectID) {
            certainProject = project3;
        };
        if (certainProject == null) { System.out.println("ERROR: Inputted Project ID does not match any existing Project. Aborting..."); return; };

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
        return;
    }

    // Main entry method.
    public static void main(String[] args) {
        int amountProjects = 0;
        String IDsProjects = "";

        Scanner input = new Scanner(System.in);

        System.out.println("Project Management System");
        System.out.print("""
                Options:
                \t * Q: quit\s
                \t* CP: create project
                \t* VP: view projects
                \t* CT: create task
                """);

        while (true) {
            System.out.print("> ");
            String line = input.nextLine();
            if (line != null) {
                switch (line.toUpperCase()) {
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
                    // View Projects.
                    case "VP":
                        optionViewProjects();
                        break;
                    // Create Task.
                    case "CT":
                        optionCreateTask(input);
                        break;
                    default:
                        System.out.println("Unknown option.");
                        break;
                }
            }

        }

    }
}
