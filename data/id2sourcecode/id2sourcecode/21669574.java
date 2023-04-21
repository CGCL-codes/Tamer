    public int printScreen() {
        Image img = control.createScreenCapture(screenSize);
        Image scaledImg = img.getScaledInstance(cliWidth, cliHeight, Image.SCALE_AREA_AVERAGING);
        return dataFormat.formatAndSend(scaledImg);
    }
