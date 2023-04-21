    public static Image createScaledImage(IDiagramModel model, double scale) {
        Shell shell = new Shell();
        shell.setLayout(new FillLayout());
        GraphicalViewer viewer = createViewer(model, shell);
        Image image = createScaledImage(viewer, scale);
        shell.dispose();
        return image;
    }
