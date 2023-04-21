    void plotHistogram(ImagePlus imp) {
        ImageStatistics stats;
        if (balance && (channels == 4 || channels == 2 || channels == 1) && imp.getType() == ImagePlus.COLOR_RGB) {
            int w = imp.getWidth();
            int h = imp.getHeight();
            byte[] r = new byte[w * h];
            byte[] g = new byte[w * h];
            byte[] b = new byte[w * h];
            ((ColorProcessor) imp.getProcessor()).getRGB(r, g, b);
            byte[] pixels = null;
            if (channels == 4) pixels = r; else if (channels == 2) pixels = g; else if (channels == 1) pixels = b;
            ImageProcessor ip = new ByteProcessor(w, h, pixels, null);
            stats = ImageStatistics.getStatistics(ip, 0, imp.getCalibration());
        } else {
            int range = imp.getType() == ImagePlus.GRAY16 ? ImagePlus.getDefault16bitRange() : 0;
            if (range != 0 && imp.getProcessor().getMax() == Math.pow(2, range) - 1 && !(imp.getCalibration().isSigned16Bit())) {
                ImagePlus imp2 = new ImagePlus("Temp", imp.getProcessor());
                stats = new StackStatistics(imp2, 256, 0, Math.pow(2, range));
            } else stats = imp.getStatistics();
        }
        Color color = Color.gray;
        if (imp.isComposite() && !(balance && channels == 7)) color = ((CompositeImage) imp).getChannelColor();
        plot.setHistogram(stats, color);
    }
