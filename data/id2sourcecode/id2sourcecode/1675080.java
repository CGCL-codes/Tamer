    public void setMaximumMagnification(float mm, boolean forceRaster) {
        super.setMaximumMagnification(mm, forceRaster);
        c = (MM - 1) / 2;
        e = (1 + MM) / 2;
    }
