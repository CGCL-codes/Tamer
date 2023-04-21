    void showLut(FileInfo fi, boolean showImage) {
        ImagePlus imp = WindowManager.getCurrentImage();
        if (imp != null) {
            if (imp.getType() == ImagePlus.COLOR_RGB) IJ.error("Color tables cannot be assiged to RGB Images."); else {
                ImageProcessor ip = imp.getChannelProcessor();
                IndexColorModel cm = new IndexColorModel(8, 256, fi.reds, fi.greens, fi.blues);
                if (imp.isComposite()) ((CompositeImage) imp).setChannelColorModel(cm); else ip.setColorModel(cm);
                if (imp.getStackSize() > 1) imp.getStack().setColorModel(cm);
                imp.updateAndRepaintWindow();
            }
        } else createImage(fi, showImage);
    }
