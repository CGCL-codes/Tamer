    private void openPathwayInTab(PathwayHolder p) {
        assert p != null;
        BioPAXGraph graph = main.getRootGraph().excise(p);
        boolean layedout = graph.fetchLayout();
        CTabItem tab = main.createNewTab(graph);
        if (refsToHighlight != null && !refsToHighlight.isEmpty()) {
            ScrollingGraphicalViewer viewer = main.getTabToViewerMap().get(tab);
            CompoundModel root = (CompoundModel) viewer.getContents().getModel();
            if (root instanceof BioPAXGraph) {
                new HighlightWithRefAction(main, (BioPAXGraph) root, refsToHighlight).run();
            }
        }
        if (!layedout) {
            new CoSELayoutAction(main).run();
        }
    }
