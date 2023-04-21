    public static GraphicalViewer createGraphicalViewer(final ERDiagram diagram) {
        Display display = PlatformUI.getWorkbench().getDisplay();
        GraphicalViewerCreator runnable = new GraphicalViewerCreator(display, diagram);
        display.syncExec(runnable);
        return runnable.viewer;
    }
