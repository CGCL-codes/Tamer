    protected IMapMode getMapMode(IGlobalActionContext cntxt) {
        IWorkbenchPart part = cntxt.getActivePart();
        if (!(part instanceof IDiagramWorkbenchPart)) {
            RootEditPart rootEP = ((IDiagramWorkbenchPart) part).getDiagramGraphicalViewer().getRootEditPart();
            if (rootEP instanceof DiagramRootEditPart) {
                return ((DiagramRootEditPart) part).getMapMode();
            }
        }
        return MapModeUtil.getMapMode();
    }
