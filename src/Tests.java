public class Tests {

    // *** Debug Methods ***
    // DESC: Initialises three Projects, each being assigned exclusively one of the three sizes available
    // And then having Tasks assigned to themselves.
    // USAGE: main().
    public static Project[] debugInitialise(String IDsProjects) {
        String[][] projectInputs = { {"1", "2", "3"}, {"Borges", "Spencer", "Wolfe"}, {"SMALL", "MEDIUM", "LARGE"} };
        String[][] taskInputs = {
                {"11", "21", "22", "31", "32", "33"},
                {"Hegel", "Bauer", "Feurbach", "Marx", "Engels", "Lenin"},
                {"A", "A", "L", "A", "L", "S"},
                {"1", "2", "3", "4", "5", "6"},
        };
        Project[] projects = new Project[3];
        Task[] tasks = new Task[6];

        System.out.println("*** INITIALISING ***");
        // Initialise Projects  .
        System.out.println("DEBUG: INITIALISING");

        for (int i = 0; i <= 2; i++) {
            try {
//                projects[i] = new Project(Integer.parseInt(projectInputs[i][0]), projectInputs[i][1], projectInputs[i][2]);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        // Generate Tasks.
        int index = -1;
        for (int i = 0; i <= 5; i++) {
                if (i == 0) {
                    index = 0;
                } else if (i > 0 && i < 3) {
                    index = 1;
                } else {
                    index = 2;
                }
            try {
                projects[index].createTask(Integer.parseInt(taskInputs[i][0]), taskInputs[i][1], taskInputs[i][2], Integer.parseInt(taskInputs[i][3]));
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        return projects;
    }

    // DESC: Attempts to perform a stress test, however too late into development did I realise I exclusively
    // wrote many of the methods regarding the management of Projects and Tasks to require human input,
    // and not wanting to refactor the whole codebase I simply accepted this lackluster version of a "test".
    // USAGE: main().
    public static boolean debugTest() {
        Project project1 = null;
        Project project2 = null;
        String[][] taskInputs = {
                {"1", "Valid ID", "A", "1"},
                {"2", "Test capacity", "A", "1"},
                {"3", "Test capacity", "A", "1"},
                {"4", "Test capacity", "A", "1"},
        };

        System.out.println("*** STRESS TEST ***");
        try {
//            project1 = new Project(0, "Test project", "Large");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        if (project1 != null) {
            for (int i = 0; i < taskInputs.length; i++) {
                try {
                    project1.createTask(Integer.parseInt(taskInputs[i][0]), taskInputs[i][1], taskInputs[i][2], Integer.parseInt(taskInputs[i][3]));
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
            try {
                project1.deleteTask(999);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        try {
//            project2 = new Project(99, "Empty", "Medium");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        UserInterface.auxPrettyAverageTypeDurationsByProject(project2, 99);
        return true;
    }
}
