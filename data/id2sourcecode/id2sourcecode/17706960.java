    public void startCapturing() throws V4L4JException {
        if (!this.captureStarted) {
            System.out.println("Device " + this.deviceNumber + ":\t\tCapture resolution " + this.configH.getWidth() + "x" + this.configH.getHeight());
            fg = vd.getJPEGFrameGrabber(this.configH.getWidth(), this.configH.getHeight(), this.configH.getChannel(), this.configH.getStd(), this.configH.getQty());
            fg.setCaptureCallback(this);
            fg.startCapture();
            this.captureStarted = true;
        }
    }
