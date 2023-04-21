    public static void exportAsImage(AgentEditor agentEditor, String saveFilePath, int format) {
        if ((agentEditor != null) && (agentEditor.getGraphicalViewer() != null)) {
            exportAsImage(agentEditor, agentEditor.getGraphicalViewer(), saveFilePath, format);
        }
    }
