    private void init() throws Exception {
        if (!this.imageInfo.check()) {
            throw new Exception("Error getting metadata from file!");
        }
        int heightDPI = this.imageInfo.getPhysicalHeightDpi();
        int widthDPI = this.imageInfo.getPhysicalWidthDpi();
        if (heightDPI <= 0 || widthDPI <= 0) {
            throw new Exception("Error retrieving dpi values from" + " file! heightDPI = " + heightDPI + "   widthDPI = " + widthDPI);
        }
        if (heightDPI == widthDPI) {
            this.dpi = heightDPI;
        } else {
            this.dpi = (heightDPI + widthDPI) / 2;
        }
    }
