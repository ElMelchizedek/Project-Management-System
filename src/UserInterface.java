import java.util.Scanner;

public class UserInterface {
    // Private variables to be accessed and manipulated via setters and getters.
    private static Project project1 = null;
    private static Project project2 = null;
    private static Project project3 = null;

    // Auxiliary methods.

    // Methods invoked by user input.
    public static void createProject(Scanner input, int amountProjects, String IDsProjects) {
        System.out.println("*** Project Wizard ***");
        Project tempProject = null;

        if (amountProjects >= 3) {
            System.out.println("ERROR: Maximum amount of concurrent projects already reached! Aborting project creation process...");
            return;
        }

        System.out.print("Enter ID for new project: ");
        int ID = input.nextInt();
        String valueID = String.valueOf(ID);
        if (IDsProjects.contains(valueID + ",")) {
            System.out.println("ERROR: Duplicate project ID! Aborting project creation process...");
            return;
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
                return;
        }

       if (project1 != null) { project1 = tempProject; System.out.println("Created Project #" + ID + " of type " + type + "."); IDsProjects = IDsProjects.concat(valueID + ","); return; }
       if (project2 != null) { project2 = tempProject; System.out.println("Created Project #" + ID + " of type " + type + "."); IDsProjects = IDsProjects.concat(valueID + ","); return; }
       if (project3 != null) { project3 = tempProject; System.out.println("Created Project #" + ID + " of type " + type + "."); IDsProjects = IDsProjects.concat(valueID + ","); return; }

       System.out.println("ERROR: Unknown error. Aborting project creation process...");
    }

    public static void viewProjects() {
        if (project1 != null) {

        }
    }

    // Main entry method.
    public static void main(String[] args) {
        int amountProjects = 0;
        String IDsProjects = "";

        Scanner input = new Scanner(System.in);

        System.out.println("Project Management System");
        System.out.print("""
                Options:\

                \t * Q: quit \

                \t* CP: create project\

                \t* VP: view projects""");

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
                        createProject(input, amountProjects, IDsProjects);
                        break;
                    // View Projects.
                    case "VP":
                        break;
                    default:
                        System.out.println("Unknown option.");
                        break;
                }
            }

        }

    }
}
