    public static Image createScaledImage(GraphicalViewer diagramViewer, double scale) {
        if (scale <= 0) {
            scale = 1;
        }
        if (scale > 4) {
            scale = 4;
        }
        Rectangle rectangle = getDiagramExtents(diagramViewer);
        Image image = new Image(Display.getDefault(), (int) (rectangle.width * scale), (int) (rectangle.height * scale));
        GC gc = new GC(image);
        SWTGraphics swtGraphics = new SWTGraphics(gc);
        Graphics graphics = swtGraphics;
        IFigure figure = ((FreeformGraphicalRootEditPart) diagramViewer.getRootEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS);
        if (scale != 1) {
            graphics = new ScaledGraphics(swtGraphics);
            graphics.scale(scale);
        }
        graphics.translate(rectangle.x * -1, rectangle.y * -1);
        figure.paint(graphics);
        gc.dispose();
        graphics.dispose();
        if (swtGraphics != graphics) {
            swtGraphics.dispose();
        }
        return image;
    }
