    public void exportImage(String fileName) {
        Device device = getGraphicalViewer().getControl().getDisplay();
        LayerManager lm = (LayerManager) getGraphicalViewer().getEditPartRegistry().get(LayerManager.ID);
        IFigure figure = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
        Rectangle r = figure.getClientArea();
        Image image = null;
        GC gc = null;
        Graphics g = null;
        try {
            image = new Image(device, r.width, r.height);
            gc = new GC(image);
            g = new SWTGraphics(gc);
            g.translate(r.x * -1, r.y * -1);
            figure.paint(g);
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.data = new ImageData[] { image.getImageData() };
            imageLoader.save(fileName, getImageFormat(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (g != null) {
                g.dispose();
            }
            if (gc != null) {
                gc.dispose();
            }
            if (image != null) {
                image.dispose();
            }
        }
    }
