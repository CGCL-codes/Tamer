    public void run() {
        Model model = main.getOwlModel();
        if (model == null) {
            MessageDialog.openError(main.getShell(), "Error!", "Load or query a BioPAX model first!");
            return;
        }
        if (withRoot) {
            updateRoot();
        }
        if (allOpenPathways) {
            for (ScrollingGraphicalViewer viewer : main.getTabToViewerMap().values()) {
                updateViewerContent(viewer);
            }
        } else {
            updateViewerContent(main.getViewer());
        }
    }
