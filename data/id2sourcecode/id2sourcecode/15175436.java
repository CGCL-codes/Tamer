    @Override
    public void repaint(long tm, final int x, final int y, int w, int h) {
        super.repaint(tm, x, y, w, h);
        final DisplayWriterThread writer = displayClient.getWriter();
        System.err.println(writer);
        final BufferedImage bIm = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final Graphics gDisplay = bIm.getGraphics();
        gDisplay.drawImage(memImage, 0, 0, w, h, x, y, x + w, y + h, null);
        writer.sendImage(bIm, x, y);
        gDisplay.dispose();
        count++;
    }
