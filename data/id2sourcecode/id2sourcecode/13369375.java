    private RPFImage[] deprojectSouthernDatelineFrames(int frameNumber, BufferedImage frame, PixelTransformer pt) {
        RPFImage[] images = new RPFImage[2];
        int minX = pixelColumn(0, frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int maxX = pixelColumn(this.frameStructure.getPixelRowsPerFrame(), frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int minY = pixelRow(0, frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int maxY = pixelRow(this.frameStructure.getPixelRowsPerFrame(), frameNumber, this.frameStructure.getPixelRowsPerFrame(), this.frameStructure.getPolarFrames());
        int midX = (minX + maxX) / 2;
        int midY = (minY + maxY) / 2;
        MinMaxLatLon bndsWest = new MinMaxLatLon();
        bndsWest.minLon = -180.;
        if (isCenterFrame(frameNumber)) {
            bndsWest.maxLon = 0.;
            bndsWest.maxLat = pt.pixel2Latitude(midX, midY, this.frameStructure.getPolarPixelConstant());
            bndsWest.minLat = pt.pixel2Latitude(minX, minY, this.frameStructure.getPolarPixelConstant());
        } else {
            bndsWest.minLat = pt.pixel2Latitude(minX, maxY, this.frameStructure.getPolarPixelConstant());
            bndsWest.maxLat = pt.pixel2Latitude(midX, minY, this.frameStructure.getPolarPixelConstant());
            bndsWest.maxLon = pt.pixel2Longitude(minX, minY);
        }
        Sector sector = Sector.fromDegrees(bndsWest.minLat, bndsWest.maxLat, bndsWest.minLon, bndsWest.maxLon);
        BufferedImage destImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        resampleFrameFile(sector, frame, destImage, frameNumber, pt);
        images[0] = new RPFImage(sector, destImage);
        MinMaxLatLon bndsEast = new MinMaxLatLon();
        bndsEast.minLat = bndsWest.minLat;
        bndsEast.maxLat = bndsWest.maxLat;
        if (isCenterFrame(frameNumber)) {
            bndsEast.minLon = 0.;
            bndsEast.maxLon = 180.;
        } else {
            bndsEast.minLon = pt.pixel2Longitude(maxX, minY);
            bndsEast.maxLon = 180.;
        }
        sector = Sector.fromDegrees(bndsEast.minLat, bndsEast.maxLat, bndsEast.minLon, bndsEast.maxLon);
        destImage = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        resampleFrameFile(sector, frame, destImage, frameNumber, pt);
        images[1] = new RPFImage(sector, destImage);
        return images;
    }
