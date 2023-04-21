    public void run() {
        this.saved = false;
        if (main.getOwlModel() == null) {
            return;
        }
        if (filename == null) {
            filename = main.getOwlFileName();
        }
        if (filename == null) {
            SaveAsBioPAXFileAction action = new SaveAsBioPAXFileAction(main);
            action.run();
            this.saved = action.isSaved();
            return;
        }
        try {
            main.lockWithMessage("Saving ...");
            for (ScrollingGraphicalViewer viewer : main.getTabToViewerMap().values()) {
                Object o = viewer.getContents().getModel();
                if (o instanceof BioPAXGraph) {
                    BioPAXGraph graph = (BioPAXGraph) o;
                    if (graph.isMechanistic()) {
                        graph.recordLayout();
                    }
                }
            }
            BioPAXIOHandler exporter = new SimpleIOHandler(main.getOwlModel().getLevel());
            FileOutputStream stream = new FileOutputStream(filename);
            exporter.convertToOWL(main.getOwlModel(), stream);
            stream.close();
            main.setOwlFileName(filename);
            main.markSaved();
            this.saved = true;
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.openError(main.getShell(), ChisioMain.TOOL_NAME, "File cannot be saved!\n" + e.getMessage());
        } finally {
            main.unlock();
            filename = null;
        }
    }
