

public class Tests {

    private static void debugAttemptProjectCreate(Project[] listProjects, int ID, String name, String type, boolean print) {
        try {
            listProjects = UserInterface.auxAddToArray(
                    listProjects,
                    Project.createProject(listProjects, ID, name, type));
            if (print) {
                System.out.println("✅");
            }
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    private static void debugAttemptTaskCreate(Project project, int ID, String description, String type, int duration, Project[] listProjects, boolean print) {
        try {
            project.createTask(ID, description, type, duration, listProjects, false);
            if (print) {
                System.out.println("✅");
            }
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static Project[] debugProjectCreate(Project[] listProjects) {
        System.out.println("*** PROJECT CREATION TEST ***");

        // Valid project
        System.out.print("Happy Project ");
        debugAttemptProjectCreate(listProjects, 1, "Happy", "Small", true);
        // Duplicate ID
        System.out.print("Duplicate ID ");
        debugAttemptProjectCreate(listProjects, 1, "Duplicate", "Small", true);
        // Invalid Type
        System.out.print("Invalid Type ");
        debugAttemptProjectCreate(listProjects, 2, "Invalid", "Huge", true);
        // Empty Name
        System.out.print("Empty Name ");
        debugAttemptProjectCreate(listProjects, 3, "", "Small", true);
        // Negative ID
        System.out.print("Negative ID ");
        debugAttemptProjectCreate(listProjects, -1, "Negative", "Small", true);
        // Medium type
        System.out.print("Medium Type ");
        debugAttemptProjectCreate(listProjects, 4, "TypeMedium", "Medium", true);
        // Large type
        System.out.print("Large Type ");
        debugAttemptProjectCreate(listProjects, 5, "TypeLarge", "Large", true);
        // Boundary check
        System.out.print("Boundary check ");
        try {
            for (int i = 10; (i - 10) <= 11; i++) {
                debugAttemptProjectCreate(listProjects, i, "Boundary", "Small", true);
                System.out.println("✅");
            }
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
        debugAttemptProjectCreate(listProjects, 1, "ProjectSmall", "Small", false);
        debugAttemptProjectCreate(listProjects, 2, "ProjectMedium", "Medium", false);
        debugAttemptProjectCreate(listProjects, 3, "ProjectLarge", "Large", false);
        debugAttemptProjectCreate(listProjects, 4, "ProjectPlaceholder", "Large", false);

        Project projectSmall = listProjects[0];
        Project projectMedium = listProjects[1];
        Project projectLarge = listProjects[2];
        Project projectPlaceholder = listProjects[3];


        // Valid tasks for each Project type
        System.out.print("Happy Tasks (Project Type) ");
        debugAttemptTaskCreate(projectSmall, 0, "TaskSmall", "A", 1, listProjects, true);
        debugAttemptTaskCreate(projectMedium, 1, "TaskMedium", "A", 1, listProjects, true);
        debugAttemptTaskCreate(projectLarge, 2, "TaskLarge", "A", 1, listProjects, true);
        // Valid tasks for each Task type
        debugAttemptTaskCreate(projectPlaceholder, 11, "TaskL", "L", 1, listProjects, true);
        debugAttemptTaskCreate(projectPlaceholder, 12, "TaskS", "S", 1, listProjects, true);
        // Non-existent project
        System.out.print("Non-existent Project ");
        debugAttemptTaskCreate(null, 3, "NonExistent", "A", 1, listProjects, true);
        // Invalid type
        System.out.print("Invalid Type ");
        debugAttemptTaskCreate(projectPlaceholder, 4, "Invalid", "X", 1, listProjects, true);
        // Duplicate ID
        System.out.print("Duplicate ID ");
        debugAttemptTaskCreate(projectLarge, 2, "Duplicate", "A", 1, listProjects, true);
        // Empty Description
        System.out.print("Empty Description ");
        debugAttemptTaskCreate(projectPlaceholder, 5, "", "A", 1, listProjects, true);
        // Negative Duration
        System.out.print("Negative Duration ");
        debugAttemptTaskCreate(projectPlaceholder, 6, "Negative", "A", -1, listProjects, true);
        // Boundary check for each type
        System.out.print("Boundary Check ");
        debugAttemptTaskCreate(projectSmall, 7, "BoundarySmall", "A", 1, listProjects, true);
        debugAttemptTaskCreate(projectMedium, 8, "BoundaryMedium1", "A", 1, listProjects, true);
        debugAttemptTaskCreate(projectMedium, 9, "BoundaryMedium2", "A", 1, listProjects, true);
        debugAttemptTaskCreate(projectLarge, 10, "BoundaryLarge1", "A", 1, listProjects, true);
        debugAttemptTaskCreate(projectLarge, 11, "BoundaryLarge2", "A", 1, listProjects, true);
        debugAttemptTaskCreate(projectLarge, 12, "BoundaryLarge3", "A", 1, listProjects, true);

        listProjects = new Project[10];
        System.out.print("\n\n");
        return listProjects;
    }

    public static Project[] debugViewProjects(Project[] listProjects) {
        System.out.println("*** VIEW PROJECTS TEST ***");
        // No projects
        System.out.println("+ No Projects");
        // Project but no tasks
        System.out.println("+ Project (Empty)");
        debugAttemptProjectCreate(listProjects, 1, "NoTasks", "Small", false);
        try {
            UserInterface.dispViewProjects(listProjects);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        // Project full of tasks
        System.out.println("+ Project (Full)");
        debugAttemptTaskCreate(listProjects[0], 1, "Full", "A", 1, listProjects, false);
        try {
            UserInterface.dispViewProjects(listProjects);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        listProjects = new Project[10];
        System.out.print("\n\n");
        return listProjects;
    }

    public static Project[] debugViewCompleteTasks(Project[] listProjects) {
        System.out.println("*** VIEW COMPLETE TASKS TEST ***");
        // No tasks
        System.out.println("+ No Tasks");
        debugAttemptProjectCreate(listProjects, 1, "NoTasks", "Medium", false);
        UserInterface.auxViewCompleteTasks(listProjects[0]);
        // Only pending tasks
        System.out.println("+ Only Pending Tasks");
        debugAttemptTaskCreate(listProjects[0], 1, "Pending", "A", 1, listProjects, false);
        UserInterface.auxViewCompleteTasks(listProjects[0]);
        // Mixed pending-complete
        System.out.println("+ Mixed Task Completeness");
        debugAttemptTaskCreate(listProjects[0], 2, "Complete", "A", 1, listProjects, false);
        listProjects[0].getTasks()[1].setCompleted(true);
        UserInterface.auxViewCompleteTasks(listProjects[0]);

        listProjects = new Project[10];
        System.out.print("\n\n");
        return listProjects;
    }

    public static Project[] debugViewFilteredTasks(Project[] listProjects) {
        System.out.println("*** VIEW FILTERED TASKS TEST ***");
        // Preliminary creation
        // Create project
        debugAttemptProjectCreate(listProjects, 0, "Container", "Large", false);
        // Create tasks
        debugAttemptTaskCreate(listProjects[0], 1, "Admin", "A", 1, listProjects, false);
        debugAttemptTaskCreate(listProjects[0], 2, "Logi", "L", 1, listProjects, false);
        debugAttemptTaskCreate(listProjects[0], 3, "Support", "S", 1, listProjects, false);

        // +++ ACTUAL TESTS
        // Each Filter
        System.out.println("+ Filters");
        UserInterface.auxViewFilteredTasks("A", listProjects);
        UserInterface.auxViewFilteredTasks("L", listProjects);
        UserInterface.auxViewFilteredTasks("S", listProjects);
        // No Matching Type
        System.out.println("+ No Matching Type");
        try {
            listProjects[0].deleteTask(3);
        } catch (Exception e) {
           System.out.println("ERROR: " + e.getMessage());
        }
        UserInterface.auxViewFilteredTasks("S", listProjects);

        listProjects = new Project[10];
        System.out.print("\n\n");
        return listProjects;
    }

    public static Project[] debugViewAverageTypeDurations(Project[] listProjects) {
        System.out.println("*** VIEW AVERAGE TYPE DURATIONS TEST ***");
        // Tasks

        // +++ ACTUAL TESTS
        // Project with no tasks
        System.out.println("+ Project (Empty)");
        debugAttemptProjectCreate(listProjects, 3, "ContainerEmpty", "Small", false);
        try {
            UserInterface.dispAverageTypeDurations(listProjects);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.print("\n\n");

        // Tasks of only one type
        listProjects = new Project[10];
        System.out.println("+ All Projects Same Type");
        debugAttemptProjectCreate(listProjects, 0, "Container1", "Small", false);
        debugAttemptProjectCreate(listProjects, 1, "Container2", "Small", false);
        debugAttemptProjectCreate(listProjects, 2, "Container3", "Small", false);
        debugAttemptTaskCreate(listProjects[0], 4, "Task1", "A", 1, listProjects, false);
        debugAttemptTaskCreate(listProjects[1], 5, "Task2", "A", 1, listProjects, false);
        debugAttemptTaskCreate(listProjects[2], 6, "Task3", "A", 1, listProjects, false);
        try {
            UserInterface.dispAverageTypeDurations(listProjects);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.print("\n\n");

        // Mixed Tasks
        System.out.println("+ Mixed Tasks");
        try {
            listProjects[1].getTasks()[0].setTaskType("L");
        } catch (Exception e) {
//            System.out.println("ERROR: " + e.getMessage());
        }
        try {
            listProjects[2].getTasks()[0].setTaskType("S");
        } catch (Exception e) {
//            System.out.println("ERROR: " + e.getMessage());
        }
        try {
            UserInterface.dispAverageTypeDurations(listProjects);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        listProjects = new Project[10];
        System.out.print("\n\n");
        return listProjects;
    }
}
