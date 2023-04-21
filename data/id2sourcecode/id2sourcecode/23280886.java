    public SmoothMeasureFrame(String targetLogoFilename, Meter meter, RGBBase.Channel targetChannel) {
        this.targetLogoFilename = targetLogoFilename;
        this.meter = meter;
        this.targetChannel = targetChannel;
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
            myInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
