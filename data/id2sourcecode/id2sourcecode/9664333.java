    public AChannel(AChannelSelection chs) {
        super();
        markChange();
        audible = true;
        selection = new AChannelSelection(this);
        setPlotType(ALayer.SAMPLE_CURVE_TYPE);
        mask = new AChannelMask(this);
        marker = new AChannelMarker(this);
        samples = new MMArray(chs.getLength(), 0);
        samples.copy(chs.getChannel().samples, chs.getOffset(), 0, samples.getLength());
        graphicObjects = new GGraphicObjects();
        graphicObjects.setChannel(this);
    }
