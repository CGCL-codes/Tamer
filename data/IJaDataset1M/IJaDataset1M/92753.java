package riaf.controller;

import java.util.HashMap;
import java.util.Map;

public class RTask {

    private String id;

    private Map<String, RTaskDef> taskDefs = new HashMap<String, RTaskDef>();

    public RTask(String id, Map<String, RTaskDef> taskDefs) {
        super();
        this.id = id;
        this.taskDefs = taskDefs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, RTaskDef> getTaskDefs() {
        return taskDefs;
    }

    public void setTaskDefs(Map<String, RTaskDef> pages) {
        this.taskDefs = pages;
    }
}
