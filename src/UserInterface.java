import java.util.Scanner;

public class UserInterface {
    // Private variables to be accessed and manipulated via setters and getters.
    private static Project project1 = null;
    private static Project project2 = null;
    private static Project project3 = null;

    // Auxiliary methods.
    // Used in optionCreateProject().
    private static String auxAssignProject(Project target, Project source, int ID, String type, String valueID, String IDsProjects) {
        if (target == null) {
            target = source;
            System.out.println("Created Project #" + ID + " of type " + type + ".");
            return IDsProjects.concat(valueID + ",");
        }
        return "";
    }

    // Used in auxViewProject().
    public static void auxTaskPrettyInfo(Task task) {
        if (task != null) {
            System.out.print("\t* Task ID: " + task.getTaskID() +
                    ", Description: " + task.getDescription() +
                    ", Type: " + task.getTaskType() +
                    ", Duration: " + task.getTaskDuration() +
                    ", Status: " + task.status());
        } else {
            System.out.println("ERROR: Task sent as parameter to auxTaskPrettyInfo is null!");
        }
    }

    // Used in optionViewProjects().
    private static void auxViewProject(Project project) {
        if (project != null) {
            int amountTasks = project.amountTasks();
            System.out.println("Project ID: " + project1.getProjectID() + ", Project Name: " + project1.getProjectType() + ", Num of tasks: " + amountTasks);
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

        System.out.print("Enter ID for new project: ");
        int ID = input.nextInt();
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

        if (project1 != null) {
            return auxAssignProject(project1, tempProject, ID, type, valueID, IDsProjects);
        } else if (project2 != null) {
            return auxAssignProject(project2, tempProject, ID, type, valueID, IDsProjects);
        } else if (project3 != null) {
            return auxAssignProject(project3, tempProject, ID, type, valueID, IDsProjects);
        }

        return "";
    }

    public static void optionViewProjects() {
        auxViewProject(project1);
        System.out.println("-----------------------------------------------");
        auxViewProject(project2);
        System.out.println("-----------------------------------------------");
        auxViewProject(project3);
        System.out.println("-----------------------------------------------");
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
                    default:
                        System.out.println("Unknown option.");
                        break;
                }
            }

        }

    }
}
