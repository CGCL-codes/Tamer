    public void run() {
        if (!hasDiagramElements()) {
            openMessageNoElements();
            return;
        }
        GraphicalViewer graphicalViewer = getDesignerEditor().getGraphicalViewer();
        final FileFormat imageFormat = getImageFormat();
        if (imageFormat == null) {
            return;
        }
        final String fileName = FileUtils.getInputFileName(getShell(), imageFormat.getFileExtensions(), SWT.SAVE, true);
        if (fileName == null) {
            return;
        }
        LayerManager layerManager = (LayerManager) graphicalViewer.getEditPartRegistry().get(LayerManager.ID);
        final IFigure diagramFigure = layerManager.getLayer("Printable Layers");
        Dimension dimension = diagramFigure.getSize();
        Display display = graphicalViewer.getControl().getDisplay();
        Image image = null;
        try {
            image = new Image(display, dimension.width, dimension.height);
        } catch (SWTException _ex) {
            MessageDialog.openError(null, "Error", "Error exporting image");
            return;
        }
        final Image exportImage = image;
        ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(getShell());
        try {
            monitorDialog.run(true, false, new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("Exporting...", -1);
                    try {
                        exportToImage(exportImage, diagramFigure, imageFormat, fileName);
                    } catch (Exception ex) {
                        throw new InvocationTargetException(ex);
                    }
                }
            });
        } catch (Throwable ex) {
            handleProcessError(ex);
        }
    }
