import javax.sound.midi.SysexMessage;

public class Tests {

    private static void debugAttemptProjectCreate(Project[] listProjects, int ID, String name, String type) {
        try {
            listProjects = UserInterface.auxAddToArray(
                    listProjects,
                    Project.createProject(listProjects, ID, name, type));
            System.out.println("✅");
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void debugAttemptTaskCreate(Project project, int ID, String description, String type, int duration, Project[] listProjects) {
        try {
            project.createTask(ID, description, type, duration, listProjects);
            System.out.println("✅");
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static Project[] debugProjectCreate(Project[] listProjects) {
        System.out.println("*** PROJECT CREATION TEST ***");

        // Valid project
        System.out.print("Happy Project ");
        debugAttemptProjectCreate(listProjects, 1, "Happy", "Small");
        // Duplicate ID
        System.out.print("Duplicate ID ");
        debugAttemptProjectCreate(listProjects, 1, "Duplicate", "Small");
        // Invalid Type
        System.out.print("Invalid Type ");
        debugAttemptProjectCreate(listProjects, 2, "Invalid", "Huge");
        // Empty Name
        System.out.print("Empty Name ");
        debugAttemptProjectCreate(listProjects, 3, "", "Small");
        // Negative ID
        System.out.print("Negative ID ");
        debugAttemptProjectCreate(listProjects, -1, "Negative", "Small");
        // Medium type
        System.out.print("Medium Type ");
        debugAttemptProjectCreate(listProjects, 4, "TypeMedium", "Medium");
        // Large type
        System.out.print("Large Type ");
        debugAttemptProjectCreate(listProjects, 5, "TypeLarge", "Large");
        // Boundary check
        System.out.print("Boundary check ");
        try {
            for (int i = 10; (i - 10) <= 11; i++) {
                listProjects = UserInterface.auxAddToArray(
                        listProjects,
                        Project.createProject(listProjects, i, "Boundary", "Small"));
            }
            System.out.println("✅");
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("ERROR: " + e.getMessage());
        }

        listProjects = new Project[10];
        System.out.print("\n\n");
        return listProjects;
    }

    public static Project[] debugTaskCreate(Project[] listProjects) {
        System.out.println("*** TASK CREATION TEST ***");
        // Necessary Projects
        try {
            listProjects = UserInterface.auxAddToArray(
                    listProjects,
                    Project.createProject(listProjects, 1, "ProjectSmall", "Small")
            );
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        try {
            listProjects = UserInterface.auxAddToArray(
                    listProjects,
                    Project.createProject(listProjects, 2, "ProjectMedium", "Medium")
            );
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        try {
            listProjects = UserInterface.auxAddToArray(
                    listProjects,
                    Project.createProject(listProjects, 3, "ProjectLarge", "Large")
            );
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        try {
            listProjects = UserInterface.auxAddToArray(
                    listProjects,
                    Project.createProject(listProjects, 4, "ProjectPlaceholder", "Large")
            );
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        Project projectSmall = listProjects[0];
        Project projectMedium = listProjects[1];
        Project projectLarge = listProjects[2];
        Project projectPlaceholder = listProjects[3];


        // Valid tasks for each Project type
        System.out.print("Happy Tasks (Project Type) ");
        debugAttemptTaskCreate(projectSmall, 0, "TaskSmall", "A", 1, listProjects);
        debugAttemptTaskCreate(projectMedium, 1, "TaskMedium", "A", 1, listProjects);
        debugAttemptTaskCreate(projectLarge, 2, "TaskLarge", "A", 1, listProjects);
        // Valid tasks for each Task type
        debugAttemptTaskCreate(projectPlaceholder, 11, "TaskL", "L", 1, listProjects);
        debugAttemptTaskCreate(projectPlaceholder, 12, "TaskS", "S", 1, listProjects);
        // Non-existent project
        System.out.print("Non-existent Project ");
        debugAttemptTaskCreate(null, 3, "NonExistent", "A", 1, listProjects);
        // Invalid type
        System.out.print("Invalid Type ");
        debugAttemptTaskCreate(projectPlaceholder, 4, "Invalid", "X", 1, listProjects);
        // Duplicate ID
        System.out.print("Duplicate ID ");
        debugAttemptTaskCreate(projectLarge, 2, "Duplicate", "A", 1, listProjects);
        // Empty Description
        System.out.print("Empty Description ");
        debugAttemptTaskCreate(projectPlaceholder, 5, "", "A", 1, listProjects);
        // Negative Duration
        System.out.print("Negative Duration ");
        debugAttemptTaskCreate(projectPlaceholder, 6, "Negative", "A", -1, listProjects);
        // Boundary check for each type
        System.out.print("Boundary Check ");
        debugAttemptTaskCreate(projectSmall, 7, "BoundarySmall", "A", 1, listProjects);
        debugAttemptTaskCreate(projectMedium, 8, "BoundaryMedium1", "A", 1, listProjects);
        debugAttemptTaskCreate(projectMedium, 9, "BoundaryMedium2", "A", 1, listProjects);
        debugAttemptTaskCreate(projectLarge, 10, "BoundaryLarge1", "A", 1, listProjects);
        debugAttemptTaskCreate(projectLarge, 11, "BoundaryLarge2", "A", 1, listProjects);
        debugAttemptTaskCreate(projectLarge, 12, "BoundaryLarge3", "A", 1, listProjects);

        listProjects = new Project[10];
        System.out.print("\n\n");
        return listProjects;
    }

    public static Project[] debugViewProjects(Project[] listProjects) {
        System.out.println("*** VIEW PROJECTS TEST ***");
        // No projects
        System.out.println("+ No Projects");
        UserInterface.dispViewProjects(listProjects);
        // Project but no tasks
        System.out.println("+ Project (Empty)");
        try {
            listProjects = UserInterface.auxAddToArray(
                    listProjects,
                    Project.createProject(listProjects, 1, "NoTasks", "Small"));
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        UserInterface.dispViewProjects(listProjects);
        // Project full of tasks
        System.out.println("+ Project (Full)");
        try {
            listProjects[0].createTask(1, "Full", "A", 1, listProjects);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        UserInterface.dispViewProjects(listProjects);
        return listProjects;
    }
}
