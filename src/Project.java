import java.util.Random;

public class Project {
    // Variables that can be accessed via setters/getters.
    private Task[] listTasks;
    private int projectID;
    private String projectName;
    private String projectType;
    // Project Types:   "Small" = Max 1 task.
    //                  "Medium" = Max 2 tasks.
    //                  "Large" = Max 3 tasks.

    // Internal variables.
    private final String[] permittedTypes = {"SMALL", "MEDIUM", "LARGE"};

    // Constructor.
    public Project() {
        this.projectID = -1;
        this.projectName = "";
        this.projectType = "";
        this.listTasks = new Task[0];
    }
    public Project(int ID, String name, String type, Project[] listProjects) throws Exception {
        // Make sure that the soon-to-be project does not have an ID already in use.
        setProjectID(ID, listProjects);
        setProjectName(name);
        setProjectType(type);
    }

    // Getters.
    public String getProjectName() { return projectName; }
    public int getProjectID() { return projectID; }
    public String getProjectType() { return projectType; }
    public Task[] getListTasks() { return listTasks; }

    // Setters.
    public void setProjectID( int ID, Project[] listProjects ) throws Exception {
        Random random = new Random();

        if (ID < 0) {
            throw new Exception("Inputted ID is a negative which is forbidden.");
        }

        for (Project project: listProjects) {
            if (project!= null && project.getProjectID() == ID) {
                do {
                    this.projectID = random.nextInt(1000);
                } while (UserInterface.auxCheckIDExistsInProjectList(listProjects, this.projectID));
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
    public void setListTasks( String type ) throws Exception {
       if (type.equals("SMALL")) {
           this.listTasks = new Task[1];
       } else if (type.equals("MEDIUM")) {
           this.listTasks = new Task[2];
       } else if (type.equals("LARGE")) {
           this.listTasks = new Task[3];
       } else {
           throw new Exception("Unknown Project type passed to Project.setListTasks().");
       }
    }

    // Methods relating to Tasks.
    public void createTask(int ID, String description, String type, int duration, Project[] listProjects, boolean completed) throws Exception {
        for (int i = 0; i < listTasks.length; i++) {
            if (listTasks[i] == null) {
                listTasks[i] = Task.createTask(this, ID, description, type, duration, listProjects, completed);
                return;
            }
        }

    }

    // Other methods relating to the Project itself and its Tasks.
    public int deleteTask(int ID) throws Exception {
        Task removedTask;

        boolean found = false;
        for (int i = 0; i < listTasks.length; i++) {
            if (listTasks[i] != null && listTasks[i].getTaskID() == ID) {
                listTasks[i] = null;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Could not find Task for deletion from inputted Project ID.");
        }

        return this.projectID;
    }

    public Task retrieveTaskByID(int ID) throws Exception {
        for (Task task: listTasks) {
            if (task.getTaskID() == ID) {
                return task;
            }
        }

        throw new Exception("Could not retrieve Task by specified ID.");
    }

    public int amountTasks() {
        int amount = 0;
        for (Task task: listTasks) {
            if (task != null) { amount++; }
        }
        return amount;
    }

    public static Project createProject(Project[] listProjects, int newID, String newName, String newType) throws Exception {
        Project tempProject = new Project();

        if (listProjects.length > 10) {
            throw new Exception("Maximum amount of concurrent projects already reached! Aborting project creation process...");
        }

        tempProject.setProjectID(newID, listProjects);
        tempProject.setProjectName(newName);
        tempProject.setProjectType(newType);
        tempProject.setListTasks(newType.toUpperCase());

        System.out.println("Created Project #" + newID + " of name " + newName + " and type " + newType + ".");
        return tempProject;
    }

    // Imperative to not fill up listTasks out of order: only sequentially please.
    public boolean checkListTasksFull() {
        if ((projectType.equals("Small") && listTasks[0] != null) ||
                (projectType.equals("Medium") && listTasks[1] != null) ||
                (projectType.equals("Large") && listTasks[2] != null)) {
            return true;
        }
        return false;
    }

    public int findIndex(Project[] listProjects) throws Exception {
        int index = -1;
        int j = 0;
        for (Project project: listProjects) {
            if (project.getProjectID() == this.projectID) {
                return j;
            }
            j++;
        }
        throw new Exception("Failed to find index of Project.");
    }
}
