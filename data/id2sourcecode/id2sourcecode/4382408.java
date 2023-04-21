    public static void writeShapefile(GeneralPath[] paths, String fileName, int shapeType) {
        String baseName = fileName.substring(0, fileName.length() - 4);
        try {
            FileOutputStream shpFis = new FileOutputStream(baseName + ".shp");
            FileOutputStream shxFis = new FileOutputStream(baseName + ".shx");
            FileChannel shpChan = shpFis.getChannel();
            FileChannel shxChan = shxFis.getChannel();
            Lock lock = new Lock();
            ShapefileWriter shpWriter = new ShapefileWriter(shpChan, shxChan, lock);
            for (GeneralPath path : paths) {
                Geometry geom = Java2DConverter.toMultiGon(path);
                shpWriter.writeGeometry(geom);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
