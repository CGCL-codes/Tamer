    public static Image createThumbnailImage(IDiagramModel diagramModel) {
        Shell shell = new Shell();
        GraphicalViewer diagramViewer = DiagramUtils.createViewer(diagramModel, shell);
        Rectangle bounds = DiagramUtils.getDiagramExtents(diagramViewer);
        double ratio = Math.min(1, Math.min((double) TemplateManager.THUMBNAIL_WIDTH / bounds.width, (double) TemplateManager.THUMBNAIL_HEIGHT / bounds.height));
        Image image = DiagramUtils.createScaledImage(diagramViewer, ratio);
        shell.dispose();
        GC gc = new GC(image);
        Color c = new Color(null, 64, 64, 64);
        gc.setForeground(c);
        gc.drawRectangle(0, 0, image.getBounds().width - 1, image.getBounds().height - 1);
        gc.dispose();
        c.dispose();
        return image;
    }
