    private void readSHP() throws IOException {
        URL url = new URL(base, baseName + ".shp");
        shpStream = new DataInputStream(url.openStream());
        int magic = shpStream.readInt();
        if (magic != 9994) {
            throw new IOException("Unknown file");
        }
        shpStream.skipBytes(20);
        shpStream.readInt();
        if (readInt(shpStream) != 1000) {
            throw new IOException("Bad version");
        }
        readInt(shpStream);
        double xMin = readDouble(shpStream);
        double yMin = readDouble(shpStream);
        double xMax = readDouble(shpStream);
        double yMax = readDouble(shpStream);
        if (xMin < -180) {
            xMin = -180;
        }
        if (yMin < -80) {
            yMin = -80;
        }
        if (xMax > 180) {
            xMax = 180;
        }
        if (yMax > 80) {
            yMax = 80;
        }
        shpStream.skipBytes(32);
        while (readRecordHeader()) {
            try {
                readRecordContents();
            } catch (Exception e) {
            }
        }
        shpStream.close();
    }
