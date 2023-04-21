    public void previewComponent(Container parent, Component component, int componentIndex, Graphics g, int x, int y, int w, int h) {
        if (component == null) return;
        int compWidth = component.getWidth();
        int compHeight = component.getHeight();
        if ((compWidth > 0) && (compHeight > 0)) {
            BufferedImage tempCanvas = new BufferedImage(compWidth, compHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics tempCanvasGraphics = tempCanvas.getGraphics();
            component.paint(tempCanvasGraphics);
            double coef = Math.min((double) w / (double) compWidth, (double) h / (double) compHeight);
            if (coef < 1.0) {
                int sdWidth = (int) (coef * compWidth);
                int sdHeight = (int) (coef * compHeight);
                int dx = x + (w - sdWidth) / 2;
                int dy = y + (h - sdHeight) / 2;
                g.drawImage(LafWidgetUtilities.createThumbnail(tempCanvas, sdWidth), dx, dy, null);
            } else {
                g.drawImage(tempCanvas, x, y, null);
            }
        }
    }
