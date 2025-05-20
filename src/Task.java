public class Task {
    // Variables that can be accessed via setters/getters.
    private int taskID;
    private String description;
    private boolean completed;
    private char taskType;
    private int taskDuration;
    // Internal variables.
    public static final String permittedTypes = "ALS";

    // Constructors.
    public Task(int ID, String description, String type, int duration, boolean completed, Project project) throws Exception {
        setTaskID(ID, project);
        setDescription(description);
        setTaskType(type);
        setTaskDuration(duration);
        setCompleted(completed);
    }

    // Getters.
    public int getTaskID() { return this.taskID; }
    public String getDescription() { return this.description; }
    public boolean getCompleted() { return this.completed; }
    public char getTaskType() { return this.taskType; }
    public int getTaskDuration() { return this.taskDuration; }

    // Setters.
    public void setTaskID(int ID, Project project) throws Exception {
        String valueNewID = String.valueOf(ID);
        if (project.getAllTaskIDs() != null && project.getAllTaskIDs().contains(valueNewID + ",")) {
            throw new Exception("Duplicate Task ID!");
        }

        this.taskID = ID;
        project.addTaskID(valueNewID);
    }
    public void setTaskType(String type) throws Exception {
        type = type.toUpperCase();
        if (type.length() != 1) {
            throw new Exception("Task Type string is not of length 1!");
        }
        if (!permittedTypes.contains(type)) {
            throw new Exception("Task Type string is not either \"A\", \"L\", or \"S\"!");
        }

        this.taskType = type.charAt(0);
    }
    public void setDescription(String description) { this.description = description; }
    public void setTaskDuration(int duration) { this.taskDuration = duration; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // Auxiliary Methods
    // Returns status of Task, per the "completed" variable.
    public String status() {
        if (completed) {
            return "Completed";
        } else { return "Pending"; }
    }

}
