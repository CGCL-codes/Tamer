    private void startCapture() {
        if (captureThread == null) {
            Interval i = null;
            try {
                i = intervals.getInterval();
            } catch (Exception e) {
                return;
            }
            try {
                fg = processor.getGrabber((ImageFormat) formats.getSelectedItem());
            } catch (V4L4JException e) {
                JOptionPane.showMessageDialog(f, "Error obtaining the frame grabber");
                return;
            }
            try {
                if (i != null) fg.setFrameInterval(i.num, i.denom);
            } catch (Exception e) {
            }
            try {
                fg.startCapture();
            } catch (V4L4JException e) {
                JOptionPane.showMessageDialog(f, "Failed to start capture:\n" + e.getMessage());
                e.printStackTrace();
                processor.releaseGrabber();
                return;
            }
            width = fg.getWidth();
            height = fg.getHeight();
            video.setMaximumSize(new Dimension(width, height));
            video.setSize(new Dimension(width, height));
            controlScrollPane.setPreferredSize(new Dimension(300, height));
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
            raster = Raster.createInterleavedRaster(new DataBufferByte(new byte[width * height * 3], width * height * 3), width, height, 3 * width, 3, new int[] { 0, 1, 2 }, null);
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
            img = new BufferedImage(cm, raster, false, null);
            stop = false;
            captureThread = new Thread(this, "Capture Thread");
            captureThread.start();
            System.out.println("Input format: " + fg.getImageFormat().getName());
        }
    }
