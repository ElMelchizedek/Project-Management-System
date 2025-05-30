import java.util.Random;

public class Task {
    // Variables that can be accessed via setters/getters.
    private int taskId;
    private String description;
    private boolean completed;
    private char taskType;
    private int taskDuration;
    // Internal variables.
    public static final String[] permittedTypes = {"A", "L", "S"};

    // Constructors.
    public Task() {
        this.taskId = -1;
        this.description = "";
        this.completed = false;
        this.taskType = 'Z';
        this.taskDuration = -1;
    }

    // Getters.
    public int getTaskId() { return this.taskId; }
    public String getDescription() { return this.description; }
    public boolean getCompleted() { return this.completed; }
    public char getTaskType() { return this.taskType; }
    public int getTaskDuration() { return this.taskDuration; }

    // Setters.
    public void setTaskId(int ID, Project project, Project[] listProjects) throws Exception {
        Random random = new Random();

        if (ID < 0) {
            throw new Exception("Inputted ID is a negative which is forbidden.");
        }

        for (Task task: project.getTasks()) {
            if (task != null && task.getTaskId() == ID) {
                do {
                    this.taskId = random.nextInt(1000);
                } while (UserInterface.auxCheckIDExistsInProjectList(listProjects, this.taskId));
                throw new Exception("Duplicate Task ID, randomly generated ID used instead.");
            }
        }
        this.taskId = ID;
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
        this.taskType = type.toUpperCase().charAt(0);
    }
    public void setDescription(String description) throws Exception {
        if (description.isEmpty()) {
            throw new Exception("Empty string cannot be set as description for Task.");
        }
        this.description = description;
    }
    public void setTaskDuration(int duration) throws Exception {
        if (duration < 0) {
            throw new Exception("Inputted duration is a negative which is forbidden.");
        }

        this.taskDuration = duration;
    }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // ! Auxilliary Methods !
    // Returns status of Task, per the "completed" variable.
    public String status() {
        if (completed) {
            return "Completed";
        } else { return "Pending"; }
    }

    // The real, proper Task creation method invoked by Project.createTask().
    public static Task createTask(Project project, int newID, String newDesc, String newType, int newDuration, Project[] listProjects, boolean completed) throws Exception {
        Task tempTask = new Task();

        if (project.checkListTasksFull()) {
            throw new Exception("Project is full and cannot take any new Tasks.");
        }

        tempTask.setTaskId(newID, project, listProjects);
        tempTask.setTaskType(newType);
        tempTask.setDescription(newDesc);
        tempTask.setTaskDuration(newDuration);
        tempTask.setCompleted(completed);

        return tempTask;
    }

    // Returns what the type actually is (S: "Support", for example), for purposes of printing to the user.
    public String typeInEnglish() {
        String string = "";
        switch (taskType) {
            case 'A' -> string = "Administrative";
            case 'L' -> string = "Logistics";
            case 'S' -> string = "Support";
        }
        return string;
    }
}
