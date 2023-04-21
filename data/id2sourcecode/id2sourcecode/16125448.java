    @Override
    public Frame getFrame() {
        if (capture != null) {
            frame = capture.getFrame();
            return frame;
        } else if (defaultCapture != null) {
            frame.setImage(defaultCapture.createScreenCapture(area));
            frame.setOutputFormat(x, y, width, height, opacity, volume);
            frame.setZOrder(zorder);
            return frame;
        } else {
            return null;
        }
    }
