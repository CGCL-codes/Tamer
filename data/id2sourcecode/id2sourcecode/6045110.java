    protected void setInput(IEditorInput input) {
        superSetInput(input);
        IFile file = ((IFileEditorInput) input).getFile();
        try {
            InputStream is = file.getContents(false);
            ObjectInputStream ois = new ObjectInputStream(is);
            setLogicDiagram((LogicDiagram) ois.readObject());
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!editorSaving) {
            if (getGraphicalViewer() != null) {
                getGraphicalViewer().setContents(getLogicDiagram());
                loadProperties();
            }
            if (outlinePage != null) {
                outlinePage.setContents(getLogicDiagram());
            }
        }
    }
