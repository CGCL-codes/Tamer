    public static PDSIMG readIMGFile(String filename, int clipMin, int clipMax) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        FileChannel fc = fis.getChannel();
        int sz = (int) fc.size();
        MappedByteBuffer fileData = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
        PDSIMG img = new PDSIMG();
        try {
            img.read(fileData, clipMin, clipMax);
        } finally {
            fc.close();
        }
        return img;
    }
