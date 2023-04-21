    @Override
    protected void initializeGraphicalViewer() {
        IEditorInput editorI = getEditorInput();
        XDGuiEditorInput in;
        if (editorI instanceof XDGuiEditorInput) {
        } else if (editorI instanceof IFileEditorInput) {
            IFile file = ((IFileEditorInput) editorI).getFile();
            try {
                setInput(new XDGuiEditorInput(file.getProject().getName(), XDPlugin.getManager().getLogicalURI(file.getProject().getName(), file)));
            } catch (ManagerException e) {
                e.printStackTrace();
                System.err.println(e.getLocalizedMessage());
            }
        } else {
            System.err.println("Unsupported editor input");
        }
        in = ((XDGuiEditorInput) getEditorInput());
        OWLModelDelegate delegate = in.getOWLModelDelegate();
        ontology = ModelFactory.createWorkingOntology(delegate);
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(ontology);
        splitter.hookDropTargetListener(getGraphicalViewer());
        viewer.addDropTargetListener(createTransferDropTargetListener());
        getEditDomain().setPaletteRoot(getPaletteRoot());
    }
