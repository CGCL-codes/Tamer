    public void convertStackToImages(ImagePlus imp) {
        if (nSlices < 2) {
            IJ.error("\"Convert Stack to Images\" requires a stack");
            return;
        }
        if (!imp.lock()) return;
        ImageStack stack = imp.getStack();
        int size = stack.getSize();
        if (size > 30 && !IJ.isMacro()) {
            boolean ok = IJ.showMessageWithCancel("Convert to Images?", "Are you sure you want to convert this\nstack to " + size + " separate windows?");
            if (!ok) {
                imp.unlock();
                return;
            }
        }
        Calibration cal = imp.getCalibration();
        CompositeImage cimg = imp.isComposite() ? (CompositeImage) imp : null;
        if (imp.getNChannels() != imp.getStackSize()) cimg = null;
        for (int i = 1; i <= size; i++) {
            String label = stack.getShortSliceLabel(i);
            String title = label != null && !label.equals("") ? label : getTitle(imp, i);
            ImageProcessor ip = stack.getProcessor(i);
            if (cimg != null) {
                LUT lut = cimg.getChannelLut(i);
                if (lut != null) {
                    ip.setColorModel(lut);
                    ip.setMinAndMax(lut.min, lut.max);
                }
            }
            ImagePlus imp2 = new ImagePlus(title, ip);
            imp2.setCalibration(cal);
            String info = stack.getSliceLabel(i);
            if (info != null && !info.equals(label)) imp2.setProperty("Info", info);
            imp2.show();
        }
        imp.changes = false;
        ImageWindow win = imp.getWindow();
        if (win != null) win.close(); else if (Interpreter.isBatchMode()) Interpreter.removeBatchModeImage(imp);
        imp.unlock();
    }
