import java.util.Random;

public class Project {
    // Variables that can be accessed via setters/getters.
    private int projectID;
    private String projectName;
    private Task task1 = null, task2 = null, task3 = null;
    private String projectType;
    // Project Types:   "Small" = Max 1 task.
    //                  "Medium" = Max 2 tasks.
    //                  "Large" = Max 3 tasks.

    // Internal variables.
    private String allTaskIDs = "";
    private final String[] permittedTypes = {"SMALL", "MEDIUM", "LARGE"};

    // Constructor.
    public Project() {
        this.projectID = -1;
        this.projectName = "";
        this.projectType = "";
    }
    public Project(int ID, String name, String type) throws Exception {
        // Make sure that the soon-to-be project does not have an ID already in use.
        setProjectID(ID);
        setProjectName(name);
        setProjectType(type);
    }

    // Getters.
    public String getProjectName() { return projectName; }
    public int getProjectID() { return projectID; }
    public String getAllTaskIDs() { return allTaskIDs; }
    public String getProjectType() { return projectType; }
    public Task getTask(int number) {
        if (number == 1) {
            return task1;
        } else if (number == 2) {
            return task2;
        } else if (number == 3) {
            return task3;
        } else {
            return null;
        }
    }

    // Setters.
    public void setProjectID( int ID, Project[] listProjects ) throws Exception {
        Random random = new Random();

        for (Project project: listProjects) {
            if (project.getProjectID() == ID) {
                this.projectID = random.nextInt(1000);
                throw new Exception("Project ID already in use. Assigned a randomly generated ID instead.");
            }
        }
        this.projectID = ID;
    }
    public void setProjectName( String name ) throws Exception {
        if (name.isEmpty()) {
            throw new Exception("Empty strings are not permitted to be Project names.");
        }
        this.projectName = name;
    }
    public void setProjectType( String type ) throws Exception {
        type = type.toUpperCase();
        boolean isCorrect = false;
        for (String realType : permittedTypes) {
             if (type.equals(realType)) {
                 isCorrect = true;
                 break;
             }
        }
        if (!isCorrect) {
            throw new Exception("Inputted type is not permitted.");
        }
        String lowercase = type.toLowerCase();
        this.projectType = (Character.toUpperCase(lowercase.charAt(0)) + lowercase.substring(1));
}

    // Methods relating to Tasks.
    public void createTask(int ID, String description, String type, int duration, boolean completed) throws Exception {
        if (task1 == null) {
            task1 = new Task(ID, description, type, duration, completed, this);
        } else if ((task2 == null) && (!projectType.equalsIgnoreCase("SMALL"))) {
            task2 = new Task(ID, description, type, duration, completed, this);
        } else if ((task3 == null) && (projectType.equalsIgnoreCase("LARGE"))) {
            task3 = new Task(ID, description, type, duration, completed, this);
        } else {
            throw new Exception("Maximum amount of concurrent tasks for Project #" + this.projectID + " has already been reached!");
        }

    }
    public void addTaskID(String ID) {
        if (this.allTaskIDs != null) {
            this.allTaskIDs = allTaskIDs.concat(ID + ",");
        } else {
            this.allTaskIDs = ID.concat(",");
        }
    }
    private void clearAllTaskIDs() { allTaskIDs = ""; }

    // Other methods relating to the Project itself and its Tasks.
    public int deleteTask(int ID) throws Exception {
        Task removedTask;
        if ((task1 != null) && (task1.getTaskID() == ID)) {
            removedTask = task1;
            task1 = null;
        } else if ((task2 != null) && (task2.getTaskID() == ID)) {
            removedTask = task2;
            task2 = null;
        } else if ((task3 != null) && (task3.getTaskID() == ID)) {
            removedTask = task3;
            task3 = null;
        } else {
            throw new Exception("Invalid Task ID used to request deletion from Project #" + this.projectID + "!");
        }
        // Removes removed task's ID from allTaskIDs.
        clearAllTaskIDs();
        if ( removedTask != task1 ) { addTaskID(Integer.toString(task1.getTaskID())); }
        if ( removedTask != task2 ) { addTaskID(Integer.toString(task2.getTaskID())); }
        if ( removedTask != task3 ) { addTaskID(Integer.toString(task3.getTaskID())); }

        return this.projectID;
    }

    public int amountTasks() {
        int amountTasks = 0;
        String IDs = getAllTaskIDs();
        if (IDs != null) {
            for (int i = 0; i < IDs.length(); i++) {
                if (IDs.charAt(i) == ',') {
                    amountTasks++;
                }
            }
            return amountTasks;
        }
        return 0;
    }

    public Task retrieveTaskByID(int ID) {
        if (task1.getTaskID() == ID) {
            return getTask(1);
        } else if (task2.getTaskID() == ID) {
            return getTask(2);
        } else if (task3.getTaskID() == ID) {
            return getTask(3);
        }
        return null;
    }

    public static Project createProject(Project[] listProjects, int newID, String newName, String newType) throws Exception {
        Project tempProject = new Project();

        if (listProjects.length > 10) {
            throw new Exception("Maximum amount of concurrent projects already reached! Aborting project creation process...");
        }

        tempProject.setProjectID(newID, listProjects);
        tempProject.setProjectName(newName);
        tempProject.setProjectType(newType);

        return tempProject;
    }
}
