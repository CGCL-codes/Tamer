    public static void writeShapefile(Point2D[] points, String fileName) {
        String baseName = fileName.substring(0, fileName.length() - 4);
        try {
            FileOutputStream shpFis = new FileOutputStream(baseName + ".shp");
            FileOutputStream shxFis = new FileOutputStream(baseName + ".shx");
            FileChannel shpChan = shpFis.getChannel();
            FileChannel shxChan = shxFis.getChannel();
            Lock lock = new Lock();
            @SuppressWarnings("unused") ShapefileWriter shpWriter = new ShapefileWriter(shpChan, shxChan, lock);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
