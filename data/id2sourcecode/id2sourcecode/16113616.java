    public ImagePlus doResize() {
        ImagePlus aimp = null;
        int nwidth = this.width;
        int nheight = this.height;
        nwidth = (int) Math.round(nwidth * ax);
        nheight = (int) Math.round(nheight * ay);
        int size = depth * nframes;
        if (isStack) {
            ImageStack iso = new ImageStack(nwidth, nheight, size);
            IJ.log("stack width " + nwidth + " stack height " + nheight + " stack depth " + depth + " frames " + nframes);
            int cnt = 1;
            for (int f = 1; f <= nframes; f++) {
                for (int s = 1; s <= depth; s++) {
                    inimp.setPositionWithoutUpdate(1, s, f);
                    final ImageProcessor ip2 = inimp.getChannelProcessor().duplicate();
                    ip2.setInterpolate(true);
                    ImageProcessor ip3 = ip2.resize(nwidth, nheight);
                    iso.setPixels(ip3.getPixels(), cnt);
                    cnt++;
                }
            }
            iso.setColorModel(inimp.getChannelProcessor().getColorModel());
            aimp = new ImagePlus("resized stack", iso);
            aimp.setDimensions(1, depth, nframes);
            aimp.setOpenAsHyperStack(inimp.isHyperStack());
            inimp.setPositionWithoutUpdate(1, 1, 1);
        } else {
            ImageProcessor ip2 = inimp.getProcessor().duplicate();
            ip2.setInterpolate(true);
            ip2.scale(nwidth, nheight);
            ImageProcessor ip3 = ip2.resize(nwidth, nheight);
            ip3.setColorModel(ip2.getColorModel());
            aimp = new ImagePlus("resized", ip3);
        }
        aimp.resetDisplayRange();
        aimp.setCalibration(cal);
        return aimp;
    }
