    public void run() {
        isAlive = true;
        currentAction = "";
        Image image = TSwingUtils.getImage(imageFile).getImage();
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);
        if (imageWidth > 0 && imageHeight > 0) {
            int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
            rect = new Rectangle((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2, imageWidth, imageHeight);
            try {
                bufImage = new Robot().createScreenCapture(rect);
            } catch (AWTException e) {
                CMessageEngine.newFatalError("Can not render splashscreen", "Capture failed !");
            }
            Graphics2D g2D = bufImage.createGraphics();
            g2D.drawImage(image, 0, 0, this);
            setBounds(rect);
            setVisible(true);
        } else {
            CMessageEngine.newFatalError("Can not render splashscreen", "File " + imageFile + " was not found or is not an image file.");
        }
        isAlive = false;
    }
