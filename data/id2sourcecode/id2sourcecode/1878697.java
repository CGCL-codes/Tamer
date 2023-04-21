    @Override
    protected void doTask(ERDiagram diagram) throws Exception {
        if (this.outputFile == null || this.outputFile.trim().equals("")) {
            throw new BuildException("outputFile attribute must be set!");
        }
        this.outputFile = this.getAbsolutePath(this.outputFile);
        this.log("Output to : " + this.outputFile);
        Image img = null;
        GraphicalViewer viewer = null;
        try {
            viewer = Activator.createGraphicalViewer(diagram);
            int format = ExportToImageAction.getFormatType(outputFile);
            if (format == -1) {
                throw new BuildException(ResourceString.getResourceString("dialog.message.export.image.not.supported") + " : " + outputFile);
            }
            img = Activator.createImage(viewer);
            ExportToImageManager exportToImageManager = new ExportToImageManager(img, format, outputFile);
            exportToImageManager.doProcess();
        } finally {
            if (viewer != null) {
                viewer.getContents().deactivate();
            }
            if (img != null) {
                img.dispose();
            }
        }
    }
