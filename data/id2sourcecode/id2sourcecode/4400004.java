    public static void createThumbnailPreviewImage(IDiagramModel diagramModel, Label label) {
        Shell shell = new Shell();
        GraphicalViewer diagramViewer = DiagramUtils.createViewer(diagramModel, shell);
        Rectangle bounds = DiagramUtils.getDiagramExtents(diagramViewer);
        double ratio = Math.min(1, Math.min((double) label.getBounds().width / bounds.width, (double) label.getBounds().height / bounds.height));
        Image image = DiagramUtils.createScaledImage(diagramViewer, ratio);
        label.setImage(image);
        shell.dispose();
    }
