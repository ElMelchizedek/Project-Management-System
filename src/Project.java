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
    private String allTaskIDs;
    private final String permittedTypes = "SMALLMEDIUMLARGE";

    // Constructor.
    public Project(int ID, String name, String type) {
        // Make sure that the soon-to-be project does not have an ID already in use.
        setProjectID(ID);
        setProjectName(name);
        setProjectType(type);
    }

    // Getters.
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
    public void setProjectID( int ID ) { this.projectID = ID; }
    public void setProjectName( String name ) { this.projectName = name; }
    public void setProjectType( String type ) {
        type = type.toUpperCase();
        if (!permittedTypes.contains(type)) {
            System.out.println("ERROR: Specified Task Type is not permitted!");
            return;
        }

        this.projectType = type;
    }

    // Methods relating to Tasks.
    public void createTask(int ID, String description, String type, int duration, boolean completed) {
        if (task1 == null) {
            task1 = new Task(ID, description, type, duration, completed, this);
        } else if ((task2 == null) && (projectType.equals("MEDIUM"))) {
            task2 = new Task(ID, description, type, duration, completed, this);
        } else if ((task3 == null) && (projectType.equals("LARGE"))) {
            task3 = new Task(ID, description, type, duration, completed, this);
        } else {
            System.out.println("ERROR: Maximum amount of concurrent tasks for project #" + this.projectID + " has already been reached!");
            return;
        }

        return;
    }
    public void addTaskID(String ID) {
        this.allTaskIDs = allTaskIDs.concat(ID + ",");
    }

    // Other methods relating to the Project itself and its Tasks.
    public void deleteTask(int ID) {
        if ((task1 != null) && (task1.getTaskID() == ID)) {
            task1 = null;
        } else if ((task2 != null) && (task2.getTaskID() == ID)) {
            task2 = null;
        } else if ((task3 != null) && (task3.getTaskID() == ID)) {
            task3 = null;
        } else {
            System.out.println("ERROR: Invalid Task ID used to request deletion from Project #" + this.projectID + "!");
            return;
        }
        System.out.println("Task #" + String.valueOf(ID) + " of Project #" + this.projectID + " deleted.");
        return;
    }

    public void markTaskComplete(int ID) {
        if ((task1 != null) && (task1.getTaskID() == ID)) {
            task1.setCompleted(true);
        } else if ((task2 != null) && (task2.getTaskID() == ID)) {
            task2.setCompleted(true);
        } else if ((task3 != null) && (task3.getTaskID() == ID)) {
            task3.setCompleted(true);
        } else {
            System.out.println("ERROR: Invalid Task ID used to request marking of completion from Project #" + this.projectID + "!");
            return;
        }
        System.out.println("Task #" + String.valueOf(ID) + " of Project #" + this.projectID + " marked completed.");
        return;
    }

    public int amountTasks() {
        int amountTasks = 0;
        String IDs = getAllTaskIDs();
        for (int i =0; i < IDs.length(); i++) {
            if (IDs.charAt(i) == ',') {
                amountTasks++;
            }
        }
        return amountTasks;
    }

}
