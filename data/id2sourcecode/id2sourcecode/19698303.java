    @Override
    public void open() {
        try {
            FileChannel shpChannel = shpStream.getChannel();
            FileChannel shxChannel = shxStream.getChannel();
            this.shpWriter = new org.geotools.data.shapefile.shp.ShapefileWriter(shpChannel, shxChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("Writable Shapefile " + this.fileName + " Opened.");
    }
