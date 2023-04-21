    public static void memoryMapFile(OutputStream os, String fileName, int bufSize) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.allocate(32 * 1024);
        try {
            int s = -1;
            while ((s = fc.read(bb)) > 0) {
                os.write(bb.array(), 0, s);
                bb.clear();
            }
        } finally {
            CloseUtils.safeClose(fc);
            CloseUtils.safeClose(fis);
            CloseUtils.safeClose(os);
        }
        return;
    }
