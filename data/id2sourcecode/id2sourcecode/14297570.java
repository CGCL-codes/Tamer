    public void zoomRange(double wx1, double wy1, double wx2, double wy2) {
        cancelBuffer();
        MapCoordinates cc = canvas_.getCC();
        double gx1 = cc.xToScreen(wx1);
        double gy1 = cc.yToScreen(wy2);
        double gx2 = cc.xToScreen(wx2);
        double gy2 = cc.yToScreen(wy1);
        double screenAspect = (double) width_ / height_;
        double newAspect = (gx2 - gx1) / (gy2 - gy1);
        double x1, y1, x2, y2;
        if (newAspect < screenAspect) {
            y1 = wy1;
            y2 = wy2;
            double mx = (wx1 + wx2) / 2;
            double wxRange = screenAspect * (wy2 - wy1);
            x1 = mx - wxRange / 2;
            x2 = mx + wxRange / 2;
        } else {
            x1 = wx1;
            x2 = wx2;
            double my = (wy1 + wy2) / 2;
            double wyRange = (1 / screenAspect) * (wx2 - wx1);
            y1 = my - wyRange / 2;
            y2 = my + wyRange / 2;
        }
        canvas_.getCC().updateRange(x1, y1, x2, y2);
        updated(true, true, true);
    }
