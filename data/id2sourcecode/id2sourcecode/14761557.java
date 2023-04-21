    public static final TreeLogger getAnalyzer(String filename) {
        try {
            TreeLogger log = new TreeLogger();
            RandomAccessFile raf;
            raf = new RandomAccessFile(filename, "rw");
            log.fc = raf.getChannel();
            long len = raf.length();
            log.numEntries = (int) ((len - 128) / 16);
            log.mapBuf = log.fc.map(MapMode.READ_WRITE, 0, len);
            log.computeForwardPointers();
            return log;
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
