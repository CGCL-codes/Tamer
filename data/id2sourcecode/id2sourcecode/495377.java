    public void checkPlayers() {
        this.nbplay = 0;
        int[] pix = new int[152154];
        BufferedImage image1 = this.bot.createScreenCapture(new Rectangle(i, j, 474, 321));
        PixelGrabber pg = new PixelGrabber(image1, 0, 0, 474, 321, pix, 0, 474);
        try {
            pg.grabPixels();
        } catch (InterruptedException d) {
            System.err.println("en attente des pixels");
        }
        if ((pix[474 * 85 + 121] >> 8 & 0xFF) < 120 || (pix[474 * 85 + 121] >> 16 & 0xFF) > 120) {
            this.play[1] = true;
            this.nbplay++;
        } else {
            this.play[1] = false;
        }
        if ((pix[474 * 69 + 158] >> 8 & 0xFF) < 120 || (pix[474 * 69 + 158] >> 16 & 0xFF) > 80) {
            this.play[2] = true;
            this.nbplay++;
        } else {
            this.play[2] = false;
        }
        if ((pix[474 * 69 + 336] >> 8 & 0xFF) < 120 || (pix[474 * 69 + 336] >> 16 & 0xFF) > 80) {
            this.play[3] = true;
            this.nbplay++;
        } else {
            this.play[3] = false;
        }
        if ((pix[474 * 86 + 374] >> 8 & 0xFF) < 120 || (pix[474 * 86 + 374] >> 16 & 0xFF) > 80) {
            this.play[4] = true;
            this.nbplay++;
        } else {
            this.play[4] = false;
        }
        if ((pix[474 * 140 + 418] >> 8 & 0xFF) < 120 || (pix[474 * 140 + 418] >> 16 & 0xFF) > 80) {
            this.play[5] = true;
            this.nbplay++;
        } else {
            this.play[5] = false;
        }
        if ((pix[474 * 184 + 368] >> 8 & 0xFF) < 120 || (pix[474 * 184 + 368] >> 16 & 0xFF) > 80) {
            this.play[6] = true;
            this.nbplay++;
        } else {
            this.play[6] = false;
        }
        if ((pix[474 * 205 + 266] >> 8 & 0xFF) < 120 || (pix[474 * 205 + 266] >> 16 & 0xFF) > 80) {
            this.play[7] = true;
            this.nbplay++;
        } else {
            this.play[7] = false;
        }
        if ((pix[474 * 183 + 118] >> 8 & 0xFF) < 120 || (pix[474 * 183 + 118] >> 16 & 0xFF) > 80) {
            this.play[8] = true;
            this.nbplay++;
        } else {
            this.play[8] = false;
        }
    }
