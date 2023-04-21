    ImagePlus duplicateImage(ImagePlus imp) {
        ImageProcessor ip = imp.getProcessor();
        ImageProcessor ip2 = ip.crop();
        ImagePlus imp2 = imp.createImagePlus();
        imp2.setProcessor("DUP_" + imp.getTitle(), ip2);
        String info = (String) imp.getProperty("Info");
        if (info != null) imp2.setProperty("Info", info);
        if (imp.getStackSize() > 1) {
            ImageStack stack = imp.getStack();
            String label = stack.getSliceLabel(imp.getCurrentSlice());
            if (label != null && label.indexOf('\n') > 0) imp2.setProperty("Info", label);
            if (imp.isComposite()) {
                LUT lut = ((CompositeImage) imp).getChannelLut();
                imp2.getProcessor().setColorModel(lut);
            }
        }
        Overlay overlay = imp.getOverlay();
        if (overlay != null && !imp.getHideOverlay()) {
            overlay = overlay.duplicate();
            Rectangle r = ip.getRoi();
            if (r.x > 0 || r.y > 0) overlay.translate(-r.x, -r.y);
            imp2.setOverlay(overlay);
        }
        return imp2;
    }
