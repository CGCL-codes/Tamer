    private XSplashWindow(Frame parent, Image image, boolean undecorated, boolean resize, boolean onScreen) {
        super(parent);
        this.image = image;
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 0);
        try {
            mt.waitForID(0);
        } catch (InterruptedException ie) {
            logger.error(ie);
        }
        if (undecorated) {
            parent.setUndecorated(true);
        }
        int imgWidth = image.getWidth(this);
        int imgHeight = image.getHeight(this);
        setSize(imgWidth, imgHeight);
        if (resize) {
            parent.setSize(imgWidth, imgHeight);
        }
        int x;
        int y;
        if (onScreen) {
            Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
            x = (screenDim.width - imgWidth) / 2;
            y = (screenDim.height - imgHeight) / 2;
        } else {
            Rectangle frameBounds = parent.getBounds();
            Point location = parent.getLocationOnScreen();
            x = location.x + (frameBounds.width - imgWidth) / 2;
            y = location.y + (frameBounds.height - imgHeight) / 2;
        }
        setLocation(x, y);
        Robot robot;
        try {
            robot = new Robot();
            bkImage = robot.createScreenCapture(new Rectangle(x, y, imgWidth, imgHeight));
        } catch (AWTException e) {
            logger.debug("Unable to create screen capture for transparent window", e);
        } catch (NegativeArraySizeException e) {
            logger.debug("Unable to create screen capture for transparent window on two-monitor configurations");
        } catch (Exception e) {
            logger.debug("Unable to create screen capture for transparent window for some reason", e);
        }
    }
