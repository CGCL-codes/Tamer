    public void show(String statusMessage) {
        if (win != null) return;
        if ((IJ.isMacro() && ij == null) || Interpreter.isBatchMode()) {
            if (isComposite()) ((CompositeImage) this).reset();
            ImagePlus img = WindowManager.getCurrentImage();
            if (img != null) img.saveRoi();
            WindowManager.setTempCurrentImage(this);
            Interpreter.addBatchModeImage(this);
            return;
        }
        if (Prefs.useInvertingLut && getBitDepth() == 8 && ip != null && !ip.isInvertedLut() && !ip.isColorLut()) invertLookupTable();
        img = getImage();
        if ((img != null) && (width >= 0) && (height >= 0)) {
            activated = false;
            int stackSize = getStackSize();
            if (stackSize > 1) win = new StackWindow(this); else win = new ImageWindow(this);
            if (roi != null) roi.setImage(this);
            if (overlay != null && getCanvas() != null) getCanvas().setOverlay(overlay);
            draw();
            IJ.showStatus(statusMessage);
            if (IJ.isMacro()) {
                long start = System.currentTimeMillis();
                while (!activated) {
                    IJ.wait(5);
                    if ((System.currentTimeMillis() - start) > 2000) {
                        WindowManager.setTempCurrentImage(this);
                        break;
                    }
                }
            }
            if (imageType == GRAY16 && default16bitDisplayRange != 0) {
                resetDisplayRange();
                updateAndDraw();
            }
            if (stackSize > 1) {
                int c = getChannel();
                int z = getSlice();
                int t = getFrame();
                if (c > 1 || z > 1 || t > 1) setPosition(c, z, t);
            }
            notifyListeners(OPENED);
        }
    }
