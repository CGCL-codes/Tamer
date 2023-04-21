    public void createBlur() throws Exception {
        blurBuffer = new java.awt.Robot().createScreenCapture(getBounds());
        Graphics2D g2 = blurBuffer.createGraphics();
        paint(g2);
        g2.dispose();
        backBuffer = blurBuffer;
        new GaussianBlurFilter(5).filter(backBuffer, blurBuffer);
    }
