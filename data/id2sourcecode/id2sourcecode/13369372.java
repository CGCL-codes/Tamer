    public Sector computeFrameCoverage(int frameNumber) {
        int maxFrameNumber = getMaximumFrameNumber();
        if (frameNumber < 0 || frameNumber > maxFrameNumber) {
            String message = Logging.getMessage("generic.ArgumentOutOfRange", frameNumber);
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        int minX = pixelColumn(0, frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int maxY = pixelRow(0, frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int maxX = pixelColumn(this.frameStructure.getPixelRowsPerFrame(), frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int minY = pixelRow(this.frameStructure.getPixelRowsPerFrame(), frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int midX = (minX + maxX) / 2;
        int midY = (minY + maxY) / 2;
        PixelTransformer pt = (this.zoneCode == '9') ? northernPixels : southernPixels;
        MinMaxLatLon bounds = new MinMaxLatLon();
        double lat = pt.pixel2Latitude(minX, minY, this.frameStructure.getPolarPixelConstant());
        double lon = pt.pixel2Longitude(minX, minY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(maxX, minY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(maxX, minY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(minX, maxY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(minX, maxY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(maxX, maxY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(maxX, maxY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(midX, maxY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(midX, maxY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(maxX, midY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(maxX, midY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(midX, minY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(midX, minY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(minX, midY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(minX, midY);
        bounds.setMinMax(lat, lon);
        lat = pt.pixel2Latitude(midX, midY, this.frameStructure.getPolarPixelConstant());
        lon = pt.pixel2Longitude(midX, midY);
        bounds.setMinMax(lat, lon);
        return Sector.fromDegrees(bounds.minLat, bounds.maxLat, bounds.minLon, bounds.maxLon);
    }
