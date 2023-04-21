    @Override
    public void doTask(ERDiagram diagram) throws Exception {
        this.outputDir = getAbsolutePath(this.outputDir);
        this.outputDir = this.outputDir + "/dbdocs/";
        this.log("Output to : " + this.outputDir);
        File dir = new File(outputDir);
        FileUtils.deleteDirectory(dir);
        dir = new File(outputDir + "image");
        dir.mkdirs();
        String outputImageFilePath = this.outputDir + "image/er.png";
        this.log("Output image to : " + outputImageFilePath);
        Image img = null;
        GraphicalViewer viewer = null;
        try {
            viewer = Activator.createGraphicalViewer(diagram);
            int format = ExportToImageAction.getFormatType(outputImageFilePath);
            img = Activator.createImage(viewer);
            ExportToImageManager exportToImageManager = new ExportToImageManager(img, format, outputImageFilePath);
            exportToImageManager.doProcess();
            Map<TableView, Location> tableLocationMap = ExportToHtmlAction.getTableLocationMap(viewer, diagram);
            ExportToHtmlManager reportManager = new ExportToHtmlManager(this.outputDir, diagram, tableLocationMap);
            this.log("Output html beginning...");
            reportManager.doProcess();
        } finally {
            if (viewer != null) {
                viewer.getContents().deactivate();
            }
            if (img != null) {
                img.dispose();
            }
        }
    }
