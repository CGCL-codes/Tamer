    public Fenetre(String player) {
        System.out.println("Calibrage de la fen�tre de jeu");
        int i = 0;
        int j = 0;
        this.taille = 0;
        this.x = new int[10];
        this.y = new int[10];
        int[] pix = new int[800000];
        int valeurPixel = -2900605;
        try {
            Robot rob = new Robot();
            BufferedImage image = rob.createScreenCapture(new Rectangle(1, 1, 1000, 800));
            PixelGrabber pg = new PixelGrabber(image, 0, 0, 1000, 800, pix, 0, 1000);
            try {
                pg.grabPixels();
            } catch (InterruptedException d) {
                System.err.println("en attente des pixels");
            }
            while (i < 1000 && j < 800) {
                if (pix[i + j * 1000] == valeurPixel) {
                    this.x[taille] = i;
                    this.y[taille] = j;
                    taille++;
                    System.out.println("table trouv�e " + i + "-" + j);
                }
                i++;
                if (i == 1000) {
                    j++;
                    i = 0;
                }
            }
        } catch (AWTException e) {
            System.out.println("Erreur de lecture de l'image");
        }
    }
