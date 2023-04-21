    protected BufferedImage pillaFondo(JPopupMenu pop, Rectangle rect, int transparencia) {
        BufferedImage img = null;
        try {
            img = robot.createScreenCapture(rect);
        } catch (Throwable ex) {
            img = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            g.setColor(NimRODUtils.getColorAlfa(pop.getBackground(), transparencia));
            g.fillRect(0, 0, rect.width, rect.height);
            g.dispose();
        }
        return img;
    }
