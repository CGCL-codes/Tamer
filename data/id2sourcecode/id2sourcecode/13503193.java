    private void deletePathway(CTabItem tab) {
        ScrollingGraphicalViewer viewer = main.getTabToViewerMap().get(tab);
        main.closeTab(tab, false);
        CompoundModel root = (CompoundModel) viewer.getContents().getModel();
        if (root instanceof BioPAXGraph) {
            BioPAXGraph graph = (BioPAXGraph) root;
            if (graph.isMechanistic()) {
                graph.forgetLayout();
                Model model = graph.getBiopaxModel();
                PathwayHolder p = graph.getPathway();
                p.removeFromModel(model);
                main.getAllPathwayNames().remove(p.getName());
            }
        }
    }
