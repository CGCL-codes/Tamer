    public int printScreen(int x, int y, int zoom) {
        Image img = control.createScreenCapture(new Rectangle(x, y, cliWidth * zoom, cliHeight * zoom));
        Image scaledImg = img.getScaledInstance(cliWidth, cliHeight, BufferedImage.SCALE_AREA_AVERAGING);
        return dataFormat.formatAndSend(scaledImg);
    }
