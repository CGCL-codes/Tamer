    @Override
    public Frame grabFrame() {
        BufferedImage grab = robot.createScreenCapture(screenRectangle);
        int[] rawData = new int[screenRectangle.width * screenRectangle.height];
        grab.getRGB(0, 0, screenRectangle.width, screenRectangle.height, rawData, 0, screenRectangle.width);
        Frame frame = new Frame(screenRectangle.width, screenRectangle.height);
        frame.setData(rawData);
        return frame;
    }
