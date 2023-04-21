    public void run() {
        if (main.getViewer() == null) {
            return;
        }
        BioPAXGraph graph = main.getPathwayGraph();
        if (graph == null) {
            MessageDialog.openError(main.getShell(), "Not applicable!", "This feature works only for process views.");
            return;
        }
        List<Node> selectedNodes = new ArrayList<Node>();
        ScrollingGraphicalViewer viewer = main.getViewer();
        if (viewer == null) return;
        Iterator selectedObjects = ((IStructuredSelection) viewer.getSelection()).iterator();
        while (selectedObjects.hasNext()) {
            Object model = ((EditPart) selectedObjects.next()).getModel();
            if (model instanceof Node) {
                selectedNodes.add((Node) model);
            }
        }
        int limit = 10;
        Map<Node, Map<Integer, List<Path>>> allMap = AlgoRunner.searchPathsBetween(graph, selectedNodes, limit);
        List<String> pathIDs = new ArrayList<String>();
        idMap = new HashMap<String, Path>();
        for (Node target : allMap.keySet()) {
            Map<Integer, List<Path>> pathsMap = allMap.get(target);
            if (pathsMap != null) {
                for (int i = 1; i <= limit; i++) {
                    List<Path> pathList = pathsMap.get(i);
                    if (pathList != null) {
                        for (Path path : pathList) {
                            String id = path.toString();
                            if (pathIDs.contains(id)) {
                                int j = 2;
                                String nextID;
                                do {
                                    nextID = id + " (" + (j++) + ")";
                                } while (pathIDs.contains(nextID));
                                id = nextID;
                            }
                            pathIDs.add(id);
                            idMap.put(id, path);
                        }
                    }
                }
            }
        }
        if (!pathIDs.isEmpty()) {
            ItemSelectionDialog dialog = new ItemSelectionDialog(main.getShell(), 250, "Path Selection Dialog", "Select path to visualize", pathIDs, new ArrayList<String>(), false, false, new Runner());
            dialog.setUpdateUponSelection(true);
            String lastItem = dialog.open();
        } else {
            MessageDialog.openInformation(main.getShell(), "No results!", "No directed paths found between selected nodes.");
        }
    }
