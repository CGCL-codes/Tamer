    public boolean isItMyTurn() {
        int[] pix = new int[1];
        boolean myTurn = false;
        BufferedImage image1 = this.bot.createScreenCapture(new Rectangle(i + 254, j + 308, 1, 1));
        PixelGrabber pg = new PixelGrabber(image1, 0, 0, 1, 1, pix, 0, 1);
        try {
            pg.grabPixels();
        } catch (InterruptedException d) {
            System.err.println("en attente des pixels");
        }
        if (pix[0] == -7060977) {
            myTurn = true;
        }
        return myTurn;
    }
