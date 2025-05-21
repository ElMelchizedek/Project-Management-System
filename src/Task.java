import java.util.Random;

public class Task {
    // Variables that can be accessed via setters/getters.
    private int taskID;
    private String description;
    private boolean completed;
    private char taskType;
    private int taskDuration;
    // Internal variables.
    public static final String[] permittedTypes = {"A", "L", "S"};

    // Constructors.
    public Task() {
        this.taskID = -1;
        this.description = "";
        this.completed = false;
        this.taskType = 'Z';
        this.taskDuration = -1;
    }
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
        Random random = new Random();

        for (Task task: project.getListTasks()) {
            if (task != null && task.getTaskID() == ID) {
                this.taskID = random.nextInt(1000);
                throw new Exception("Duplicate Task ID.");
            }
        }
        this.taskID = ID;
    }

    public void setTaskType(String type) throws Exception {
        boolean good = false;
        for (String realType: permittedTypes) {
            if (type.toUpperCase().equals(realType)) {
                good = true;
                break;
            }
        }
        if (!good) {
            throw new Exception("Task Type string is not either \"A\", \"L\", or \"S\"!");
        }
        this.taskType = type.charAt(0);
    }
    public void setDescription(String description) throws Exception {
        if (description.isEmpty()) {
            throw new Exception("Empty string cannot be set as description for Task.");
        }
        this.description = description;
    }
    public void setTaskDuration(int duration) { this.taskDuration = duration; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // Auxiliary Methods
    // Returns status of Task, per the "completed" variable.
    public String status() {
        if (completed) {
            return "Completed";
        } else { return "Pending"; }
    }

    public static Task createTask(Project project, int newID, String newDesc, String newType, int newDuration) throws Exception {
        Task tempTask = new Task();

        if (project.checkListTasksFull()) {
            throw new Exception("Project is full and cannot take any new Tasks.");
        }

        tempTask.setTaskID(newID, project);
        tempTask.setTaskType(newType);
        tempTask.setDescription(newDesc);
        tempTask.setTaskDuration(newDuration);
        tempTask.setCompleted(false);

        return tempTask;
    }

}
