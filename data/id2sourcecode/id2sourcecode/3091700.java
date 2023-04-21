    public void captureImageBackground(GraphicsDevice grafica) {
        try {
            Robot rbt = new Robot();
            Rectangle bounds = grafica.getDefaultConfiguration().getBounds();
            imageBackground = rbt.createScreenCapture(bounds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
