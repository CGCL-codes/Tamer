    @Override
    public void doTask(ERDiagram diagram) throws Exception {
        if (this.outputFile == null || this.outputFile.trim().equals("")) {
            throw new BuildException("outputFile attribute must be set!");
        }
        this.outputFile = this.getAbsolutePath(this.outputFile);
        this.log("Output to : " + this.outputFile);
        boolean outputImage = false;
        if (this.outputImageFile != null) {
            outputImage = true;
            this.outputImageFile = this.getAbsolutePath(this.outputImageFile);
            this.log("Use image : on");
            this.log("Output image to : " + this.outputImageFile);
        } else {
            this.log("Use image : off");
        }
        InputStream stream = null;
        try {
            byte[] imageBuffer = null;
            int excelPictureType = -1;
            if (outputImage) {
                Image img = null;
                GraphicalViewer viewer = null;
                try {
                    viewer = Activator.createGraphicalViewer(diagram);
                    int format = ExportToImageAction.getFormatType(outputImageFile);
                    if (format == SWT.IMAGE_JPEG) {
                        excelPictureType = HSSFWorkbook.PICTURE_TYPE_JPEG;
                    } else if (format == SWT.IMAGE_PNG) {
                        excelPictureType = HSSFWorkbook.PICTURE_TYPE_PNG;
                    } else {
                        throw new BuildException(ResourceString.getResourceString("dialog.message.export.image.not.supported.for.excel") + " : " + outputImageFile);
                    }
                    img = Activator.createImage(viewer);
                    ExportToImageManager exportToImageManager = new ExportToImageManager(img, format, outputImageFile);
                    exportToImageManager.doProcess();
                    imageBuffer = FileUtils.readFileToByteArray(new File(outputImageFile));
                } finally {
                    if (viewer != null) {
                        viewer.getContents().deactivate();
                    }
                    if (img != null) {
                        img.dispose();
                    }
                }
            }
            stream = this.getTemplate();
            AntConsoleProgressMonitor monitor = new AntConsoleProgressMonitor(this);
            this.log("Output excel beginning...");
            ExportToExcelManager manager = new ExportToExcelManager(outputFile, diagram, stream, this.useLogicalNameAsSheetName, imageBuffer, excelPictureType);
            manager.run(monitor);
            if (manager.getException() != null) {
                throw manager.getException();
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
