    public static BufferedImage loadGeoTiffImage(String fileName, double[][] range, CoordinateReferenceSystem[] crs) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file == null) {
            return null;
        }
        FileChannel fc = file.getChannel();
        org.geotools.gce.geotiff.GeoTiffReader reader;
        try {
            GeoTiffReader.logger.info("Start reading " + fileName);
            ImageIO.setUseCache(false);
            reader = new org.geotools.gce.geotiff.GeoTiffReader(fc);
            GeoTiffReader.logger.info("Done reading");
        } catch (DataSourceException ex) {
            ex.printStackTrace();
            return null;
        }
        GridCoverage2D coverage;
        try {
            coverage = reader.read(null);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        RenderedImage renderedImage = coverage.getRenderedImage();
        Envelope env = coverage.getEnvelope();
        range[0][0] = env.getMinimum(0);
        range[0][1] = env.getMaximum(0);
        range[1][0] = env.getMinimum(1);
        range[1][1] = env.getMaximum(1);
        crs[0] = coverage.getCoordinateReferenceSystem2D();
        return PlanarImage.wrapRenderedImage(renderedImage).getAsBufferedImage();
    }
