    public void checkBankroll(int joueur) {
        this.bankroll[joueur] = 0;
        while (this.bankroll[joueur] == 0) {
            int[] pix = new int[152154];
            BufferedImage image1 = this.bot.createScreenCapture(new Rectangle(i, j, 474, 321));
            PixelGrabber pg = new PixelGrabber(image1, 0, 0, 474, 321, pix, 0, 474);
            try {
                pg.grabPixels();
            } catch (InterruptedException d) {
                System.err.println("en attente des pixels");
            }
            switch(joueur) {
                case 0:
                    this.bankroll[0] = OCR(10, 177, 60, pix, -4144960);
                    break;
                case 1:
                    this.bankroll[1] = OCR(9, 65, 59, pix, -4144960);
                    break;
                case 2:
                    this.bankroll[2] = OCR(95, 32, 145, pix, -4144960);
                    break;
                case 3:
                    this.bankroll[3] = OCR(340, 32, 390, pix, -4144960);
                    break;
                case 4:
                    this.bankroll[4] = OCR(420, 65, 470, pix, -4144960);
                    break;
                case 5:
                    this.bankroll[5] = OCR(417, 176, 467, pix, -4144960);
                    break;
                case 6:
                    this.bankroll[6] = OCR(290, 214, 340, pix, -4144960);
                    break;
                case 7:
                    this.bankroll[7] = OCR(210, 260, 260, pix, -4144960);
                    break;
                case 8:
                    this.bankroll[8] = OCR(140, 214, 190, pix, -4144960);
                    break;
                default:
                    Main.logger.info("Impossible de r�aliser le chekBankroll pour le joueur " + joueur);
                    break;
            }
        }
    }
