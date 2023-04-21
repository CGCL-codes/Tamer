    @Override
    public void setup(final FlagManager flagManager) throws BoblightException {
        this.flagManager = (FlagManagerV4l) flagManager;
        try {
            this.vd = new VideoDevice(this.flagManager.device);
        } catch (final V4L4JException e) {
            throw new BoblightException(e);
        }
        try {
            this.fg = this.vd.getRawFrameGrabber(this.flagManager.width, this.flagManager.height, this.flagManager.getChannel(), 1);
        } catch (final V4L4JException e) {
            throw new BoblightException(e);
        }
        this.getClient().getLightsHolder().setScanRange(this.flagManager.width, this.flagManager.height);
        this.fg.setCaptureCallback(this);
        try {
            this.fg.startCapture();
        } catch (final V4L4JException e1) {
            throw new BoblightException(e1);
        }
    }
