    public JWindowSplash() {
        Image image = new ImageIcon(Common.image_path + Common.image_splash).getImage();
        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight(this);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        rect = new Rectangle((screenWidth / 2 - (imageWidth / 2)), ((screenHeight / 2) - imageHeight), imageWidth, imageHeight);
        setBounds(rect);
        getContentPane().setLayout(new BorderLayout(0, 0));
        try {
            bufImage = new Robot().createScreenCapture(rect);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        Graphics2D g2D = bufImage.createGraphics();
        g2D.drawImage(image, 0, 0, this);
        setVisible(true);
    }
