    public void run() {
        ScrollingGraphicalViewer viewer = main.getViewer();
        BioPAXGraph pathwayGraph = main.getPathwayGraph();
        if (pathwayGraph == null) {
            MessageDialog.openError(main.getShell(), "Error!", "No BioPAX pathway.");
            return;
        }
        if (viewer != null) {
            BioPAXGraph original = (BioPAXGraph) viewer.getContents().getModel();
            if (!original.isMechanistic()) {
                MessageDialog.openError(main.getShell(), "Not Supported!", "Duplication is supported only in mechanistic views.");
                return;
            }
            original.recordLayout();
            PathwayHolder p = original.getPathway();
            BioPAXGraph graph = main.getRootGraph().excise(p);
            graph.setPathway(null);
            String name = p.getName();
            if (name.indexOf(" ") > 0) {
                String last = name.substring(name.lastIndexOf(" ") + 1);
                if (last.indexOf("(") == 0 && last.indexOf(")") == last.length() - 1) {
                    boolean isdigit = true;
                    for (int i = 1; isdigit && i < last.length() - 1; i++) {
                        isdigit = Character.isDigit(last.charAt(i));
                    }
                    if (isdigit) name = name.substring(0, name.lastIndexOf(" "));
                }
            }
            graph.setName(name);
            main.createNewTab(graph);
            graph.fetchLayout(original.getPathwayRDFID());
        }
    }
