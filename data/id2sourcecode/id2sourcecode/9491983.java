    private void exec() {
        if (canvas == null) return;
        int width = imp.getWidth();
        int height = imp.getHeight();
        if (hyperstack) {
            int c = imp.getChannel();
            int t = imp.getFrame();
            if (c != currentChannel || t != currentFrame) imageStack = null;
            if (imp.isComposite()) {
                int mode = ((CompositeImage) imp).getMode();
                if (mode != currentMode) imageStack = null;
            }
        }
        ImageStack is = imageStack;
        if (is == null) is = imageStack = getStack();
        double arat = az / ax;
        double brat = az / ay;
        Point p = crossLoc;
        if (p.y >= height) p.y = height - 1;
        if (p.x >= width) p.x = width - 1;
        if (p.x < 0) p.x = 0;
        if (p.y < 0) p.y = 0;
        updateViews(p, is);
        GeneralPath path = new GeneralPath();
        drawCross(imp, p, path);
        imp.setOverlay(path, color, new BasicStroke(1));
        canvas.setCustomRoi(true);
        updateCrosses(p.x, p.y, arat, brat);
        if (syncZoom) updateMagnification(p.x, p.y);
        arrangeWindows(sticky);
    }
