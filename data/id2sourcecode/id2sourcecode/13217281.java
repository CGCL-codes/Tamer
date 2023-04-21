    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        IEditorInput input = getEditorInput();
        if (input instanceof FileEditorInput) {
            FileEditorInput newInput = (FileEditorInput) input;
            try {
                document = Document.createDocumentFromFile(newInput.getFile());
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        viewer.setContents(document);
    }
