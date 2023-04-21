    public static GraphicalViewerImpl createViewer(IDiagramModel model, Composite parent) {
        EditPartFactory editPartFactory = null;
        if (model instanceof IArchimateDiagramModel) {
            editPartFactory = new ArchimateDiagramEditPartFactory();
        } else if (model instanceof ISketchModel) {
            editPartFactory = new SketchEditPartFactory();
        } else {
            IDiagramEditorFactory factory = DiagramEditorFactoryExtensionHandler.INSTANCE.getFactory(model);
            if (factory != null) {
                editPartFactory = factory.createEditPartFactory();
            }
        }
        if (editPartFactory == null) {
            throw new RuntimeException("Unsupported model type");
        }
        GraphicalViewerImpl viewer = new GraphicalViewerImpl();
        viewer.createControl(parent);
        viewer.setEditPartFactory(editPartFactory);
        RootEditPart rootPart = new FreeformGraphicalRootEditPart();
        viewer.setRootEditPart(rootPart);
        viewer.setContents(model);
        viewer.flush();
        return viewer;
    }
