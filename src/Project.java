import java.util.Random;

public class Project {
    // Variables that can be accessed via setters/getters.
    private Task[] tasks;
    private int projectId;
    private String projectName;
    private String projectType;
    // Project Types:   "Small" = Max 1 task.
    //                  "Medium" = Max 2 tasks.
    //                  "Large" = Max 3 tasks.

    // Internal variables.
    private final String[] permittedTypes = {"SMALL", "MEDIUM", "LARGE"};

    // Constructor.
    public Project() {
        this.projectId = -1;
        this.projectName = "";
        this.projectType = "";
        this.tasks = new Task[0];
    }

    // Getters.
    public String getProjectName() { return projectName; }
    public int getProjectId() { return projectId; }
    public String getProjectType() { return projectType; }
    public Task[] getTasks() { return tasks; }

    // Setters.
    public void setProjectId(int ID, Project[] listProjects ) throws Exception {
        Random random = new Random();

        if (ID < 0) {
            throw new Exception("Inputted ID is a negative which is forbidden.");
        }

        for (Project project: listProjects) {
            if (project!= null && project.getProjectId() == ID) {
                do {
                    this.projectId = random.nextInt(1000);
                } while (UserInterface.auxCheckIDExistsInProjectList(listProjects, this.projectId));
                throw new Exception("Project ID already in use. Assigned a randomly generated ID instead.");
            }
        }
        this.projectId = ID;
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
    public void setTasks(String type ) throws Exception {
        switch (type) {
            case "SMALL" -> this.tasks = new Task[1];
            case "MEDIUM" -> this.tasks = new Task[2];
            case "LARGE" -> this.tasks = new Task[3];
            default -> throw new Exception("Unknown Project type passed to Project.setListTasks().");
        }
    }

    // Methods relating to Tasks.
    public void createTask(int ID, String description, String type, int duration, Project[] listProjects, boolean completed) throws Exception {
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] == null) {
                tasks[i] = Task.createTask(this, ID, description, type, duration, listProjects, completed);
                return;
            }
        }

    }

    // Other methods relating to the Project itself and its Tasks.
    public int deleteTask(int ID) throws Exception {

        boolean found = false;
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] != null && tasks[i].getTaskId() == ID) {
                tasks[i] = null;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Could not find Task for deletion from inputted Project ID.");
        }

        return this.projectId;
    }

    public Task retrieveTaskByID(int ID) throws Exception {
        for (Task task: tasks) {
            if (task.getTaskId() == ID) {
                return task;
            }
        }

        throw new Exception("Could not retrieve Task by specified ID.");
    }

    public int amountTasks() {
        int amount = 0;
        for (Task task: tasks) {
            if (task != null) { amount++; }
        }
        return amount;
    }

    public static Project createProject(Project[] listProjects, int newID, String newName, String newType) throws Exception {
        Project tempProject = new Project();

        if (listProjects[9] != null) {
            throw new Exception("Maximum amount of concurrent projects already reached! Aborting project creation process...");
        }

        tempProject.setProjectId(newID, listProjects);
        tempProject.setProjectName(newName);
        tempProject.setProjectType(newType);
        tempProject.setTasks(newType.toUpperCase());

        return tempProject;
    }

    // Imperative to not fill up listTasks out of order: only sequentially please.
    public boolean checkListTasksFull() {
        return (projectType.equals("Small") && tasks[0] != null) ||
                (projectType.equals("Medium") && tasks[1] != null) ||
                (projectType.equals("Large") && tasks[2] != null);
    }

}
