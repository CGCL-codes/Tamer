    private void startCapture() {
        Interval i = intervals.getInterval();
        try {
            fg = getFrameGrabber((ImageFormat) formats.getSelectedItem());
        } catch (V4L4JException e) {
            JOptionPane.showMessageDialog(f, "Error obtaining the frame grabber");
            e.printStackTrace();
            return;
        }
        fg.setCaptureCallback(this);
        try {
            if (i != null) fg.setFrameInterval(i.num, i.denom);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(f, "Error setting the frame rate");
        }
        width = fg.getWidth();
        height = fg.getHeight();
        video.setMaximumSize(new Dimension(width, height));
        video.setSize(new Dimension(width, height));
        controlScrollPane.setPreferredSize(new Dimension(300, height));
        try {
            fg.startCapture();
        } catch (V4L4JException e) {
            JOptionPane.showMessageDialog(f, "Failed to start capture:\n" + e.getMessage());
            e.printStackTrace();
            vd.releaseFrameGrabber();
            return;
        }
        formats.setEnabled(false);
        intervals.setEnabled(false);
        startCap.setEnabled(false);
        stopCap.setEnabled(true);
        try {
            tuner = fg.getTuner();
            tinfo = vd.getDeviceInfo().getInputs().get(fg.getChannel()).getTunerInfo();
            freqSpinner.setModel(new SpinnerNumberModel(new Double(tuner.getFrequency()), new Double(tinfo.getRangeLow()), new Double(tinfo.getRangeHigh()), new Double(1)));
            freq.setVisible(true);
            freqSpinner.setVisible(true);
        } catch (V4L4JException nte) {
        }
        f.pack();
        System.out.println("Input format: " + fg.getImageFormat().getName());
    }
