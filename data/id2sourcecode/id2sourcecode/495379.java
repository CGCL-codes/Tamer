    public void checkDealer() {
        int[] pix = new int[152154];
        BufferedImage image1 = this.bot.createScreenCapture(new Rectangle(i, j, 474, 321));
        PixelGrabber pg = new PixelGrabber(image1, 0, 0, 474, 321, pix, 0, 474);
        try {
            pg.grabPixels();
        } catch (InterruptedException d) {
            System.err.println("en attente des pixels");
        }
        if ((pix[474 * 99 + 84] >> 8 & 0xFF) < 120 || (pix[474 * 99 + 84] >> 16 & 0xFF) > 80) {
            this.dealer = 1;
        } else if ((pix[474 * 65 + 171] >> 8 & 0xFF) < 120 || (pix[474 * 65 + 171] >> 16 & 0xFF) > 80) {
            this.dealer = 2;
        } else if ((pix[474 * 65 + 315] >> 8 & 0xFF) < 120 || (pix[474 * 65 + 315] >> 16 & 0xFF) > 80) {
            this.dealer = 3;
        } else if ((pix[474 * 99 + 400] >> 8 & 0xFF) < 120 || (pix[474 * 99 + 400] >> 16 & 0xFF) > 80) {
            this.dealer = 4;
        } else if ((pix[474 * 156 + 412] >> 8 & 0xFF) < 120 || (pix[474 * 156 + 412] >> 16 & 0xFF) > 80) {
            this.dealer = 5;
        } else if ((pix[474 * 200 + 338] >> 8 & 0xFF) < 120 || (pix[474 * 200 + 338] >> 16 & 0xFF) > 80) {
            this.dealer = 6;
        } else if ((pix[474 * 201 + 237] >> 8 & 0xFF) < 120 || (pix[474 * 201 + 237] >> 16 & 0xFF) > 80) {
            this.dealer = 7;
        } else if ((pix[474 * 200 + 148] >> 8 & 0xFF) < 120 || (pix[474 * 200 + 148] >> 16 & 0xFF) > 80) {
            this.dealer = 8;
        } else {
            this.dealer = 0;
        }
    }
