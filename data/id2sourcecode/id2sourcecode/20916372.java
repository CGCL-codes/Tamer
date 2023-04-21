    public static boolean exportAsImage(AgentEditor agentEditor) {
        if ((agentEditor != null) && (agentEditor.getGraphicalViewer() != null)) {
            return exportAsImage(agentEditor, agentEditor.getGraphicalViewer());
        } else {
            return false;
        }
    }
