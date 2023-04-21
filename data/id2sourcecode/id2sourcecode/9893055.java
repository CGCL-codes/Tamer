    public static void main(String argv[]) {
        ShowFrame frameRed = new ShowFrame();
        ShowFrame frameGreen = new ShowFrame();
        ShowFrame frameBlue = new ShowFrame();
        BufferedImage inputImage = null;
        BufferedImage outputImage = null;
        String inputImagePath = "images/leopard.jpg";
        int width = 64;
        int height = 64;
        int intorno = 8;
        outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        frameRed.setVisible(true);
        frameGreen.setVisible(true);
        frameBlue.setVisible(true);
        int r = 0;
        Color auxColor;
        for (int i = 0; i < width; i++) for (int j = 0; j < height; j++) {
            r = (int) (Math.random() * 256);
            auxColor = new Color(r, r, r);
            outputImage.setRGB(i, j, auxColor.getRGB());
        }
        try {
            inputImage = ImageIO.read(new File(inputImagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage imgR = Utility.getChannel(Utility.RED_CH, inputImage);
        BufferedImage imgG = Utility.getChannel(Utility.GREEN_CH, inputImage);
        BufferedImage imgB = Utility.getChannel(Utility.BLUE_CH, inputImage);
        frameRed.setImage(imgR);
        frameGreen.setImage(imgG);
        frameBlue.setImage(imgB);
        BufferedImage rImg = runSynthetizer(Utility.RED_CH, imgR, outputImage);
        BufferedImage gImg = runSynthetizer(Utility.GREEN_CH, imgG, outputImage);
        BufferedImage bImg = runSynthetizer(Utility.BLUE_CH, imgB, outputImage);
        try {
            Utility.writeImageToFile(Utility.mergeChannel(rImg, gImg, bImg), "images/", "output", "jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
