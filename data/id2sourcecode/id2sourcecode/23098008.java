    private String writeRawBytes(byte[] file, String dir, String filename) throws IOException {
        Date now = new Date();
        long time = now.getTime();
        final FileOutputStream fos = new FileOutputStream(dir + time + "_" + filename);
        FileChannel fc = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(file);
        fc.write(buffer);
        fc.close();
        return time + "_" + filename;
    }
