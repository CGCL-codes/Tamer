    void arrangeWindows(boolean sticky) {
        ImageWindow xyWin = imp.getWindow();
        if (xyWin == null) return;
        Point loc = xyWin.getLocation();
        if ((xyX != loc.x) || (xyY != loc.y)) {
            xyX = loc.x;
            xyY = loc.y;
            ImageWindow yzWin = null;
            long start = System.currentTimeMillis();
            while (yzWin == null && (System.currentTimeMillis() - start) <= 2500L) {
                yzWin = yz_image.getWindow();
                if (yzWin == null) IJ.wait(50);
            }
            if (yzWin != null) yzWin.setLocation(xyX + xyWin.getWidth(), xyY);
            ImageWindow xzWin = null;
            start = System.currentTimeMillis();
            while (xzWin == null && (System.currentTimeMillis() - start) <= 2500L) {
                xzWin = xz_image.getWindow();
                if (xzWin == null) IJ.wait(50);
            }
            if (xzWin != null) xzWin.setLocation(xyX, xyY + xyWin.getHeight());
            if (firstTime) {
                imp.getWindow().toFront();
                if (hyperstack) imp.setPosition(imp.getChannel(), imp.getNSlices() / 2, imp.getFrame()); else imp.setSlice(imp.getNSlices() / 2);
                firstTime = false;
            }
        }
    }
