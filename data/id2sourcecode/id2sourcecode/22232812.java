    public static float getZ(int worldId, float x, float y, float clientZ) {
        float z = 0F;
        float x0 = x / 2F;
        float y0 = y / 2F;
        int x1 = (int) (x0);
        int y1 = (int) (y0);
        int x2 = (int) (x0);
        int y2 = (int) (y0) + 1;
        int x3 = (int) (x0) + 1;
        int y3 = (int) (y0);
        int x4 = (int) (x0) + 1;
        int y4 = (int) (y0) + 1;
        float z1 = 0F;
        float z2 = 0F;
        float z3 = 0F;
        float z4 = 0F;
        float z12 = 0F;
        float z23 = 0F;
        float z34 = 0F;
        float z41 = 0F;
        float z1234 = 0F;
        float z2341 = 0F;
        int fraction = 0;
        int number = 0;
        long filePosition = 0;
        int side = 0;
        RandomAccessFile geoFile;
        try {
            geoFile = new RandomAccessFile(new File("./data/geo/" + String.valueOf(worldId) + ".map"), "r");
            side = (int) Math.sqrt(geoFile.length() / 2);
            filePosition = 2 * (long) (y1 + x1 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z1 = number * 8F + fraction * 0.03125F;
            filePosition = 2 * (long) (y2 + x2 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z2 = number * 8F + fraction * 0.03125F;
            filePosition = 2 * (long) (y3 + x3 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z3 = number * 8F + fraction * 0.03125F;
            filePosition = 2 * (long) (y4 + x4 * side);
            geoFile.seek(filePosition);
            number = geoFile.read();
            fraction = geoFile.read();
            z4 = number * 8F + fraction * 0.03125F;
            geoFile.close();
            z12 = (y0 - y1) * (z2 - z1) + z1;
            z23 = (x0 - x1) * (z3 - z2) + z2;
            z34 = (y0 - y1) * (z4 - z3) + z3;
            z41 = (x0 - x1) * (z1 - z4) + z4;
            z1234 = (x0 - x1) * (z34 - z12) + z12;
            z2341 = (y0 - y1) * (z23 - z41) + z41;
            z = (z1234 + z2341) / 2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            z = clientZ;
        } catch (IOException e) {
            e.printStackTrace();
            z = clientZ;
        }
        return z;
    }
