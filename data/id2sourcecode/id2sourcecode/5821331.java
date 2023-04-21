    public GraphicalViewer findViewer(IFigure figure) {
        final IFigure root = getRoot(figure);
        Set<GraphicalViewer> viewers = new HashSet<GraphicalViewer>();
        for (IWorkbenchWindow window : PlatformUI.getWorkbench().getWorkbenchWindows()) {
            for (IWorkbenchPage page : window.getPages()) {
                for (IEditorReference editorReference : page.getEditorReferences()) {
                    IEditorPart editor = editorReference.getEditor(false);
                    GraphicalViewer viewer = getViewer(editor);
                    if (viewer != null && viewers.add(viewer)) {
                        if (getRoot(viewer) == root) return viewer;
                        viewer = GEFWorkbenchUtilities.getPaletteViewer(viewer);
                        if (viewer != null && viewers.add(viewer) && getRoot(viewer) == root) return viewer;
                    }
                }
                for (IViewReference viewReference : page.getViewReferences()) {
                    IViewPart view = viewReference.getView(false);
                    GraphicalViewer viewer = getViewer(view);
                    if (viewer != null && viewers.add(viewer) && getRoot(viewer) == root) return viewer;
                }
            }
        }
        return null;
    }
