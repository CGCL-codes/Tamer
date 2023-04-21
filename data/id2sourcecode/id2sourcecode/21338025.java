    public void setImageURL(URL imageURL) throws IOException, IllegalStateException {
        this.imageURL = imageURL;
        BufferedImage image;
        image = ImageIO.read(imageURL);
        int width = image.getWidth();
        int height = image.getHeight();
        int extra = 31;
        setSize(new Dimension(width, height + extra));
        setLocationRelativeTo(null);
        Rectangle windowRect = getBounds();
        splashBufferedImage = new BufferedImage(width, height + extra, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) splashBufferedImage.getGraphics();
        try {
            Robot robot = new Robot(getGraphicsConfiguration().getDevice());
            BufferedImage capture = robot.createScreenCapture(new Rectangle(windowRect.x, windowRect.y, windowRect.width + extra, windowRect.height + extra));
            g2.drawImage(capture, null, 0, 0);
        } catch (AWTException e) {
        }
        g2.drawImage(image, 0, 0, this);
    }
