    public static void writeShapefile(Geometry[] paths, String fileNameRoot) {
        if (paths == null) {
            logger.severe("can't write null paths!");
            return;
        }
        try {
            GeometryFactory geomFact = new GeometryFactory();
            GeometryCollection geoms = null;
            geoms = new GeometryCollection(paths, geomFact);
            File shp = new File(fileNameRoot + ".shp");
            File shx = new File(fileNameRoot + ".shx");
            FileOutputStream shpStream = new FileOutputStream(shp);
            FileOutputStream shxStream = new FileOutputStream(shx);
            FileChannel shpChan = shpStream.getChannel();
            FileChannel shxChan = shxStream.getChannel();
            ShapefileWriter writer = new ShapefileWriter(shpChan, shxChan, new Lock());
            ShapeType bestType = JTSUtilities.getShapeType(paths[0], 2);
            writer.write(geoms, bestType);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
