    public static final byte[] readBytesFromFile(String filename) {
        try {
            File file = new File(filename);
            long fullsize = file.length();
            if (fullsize > Integer.MAX_VALUE) {
                throw new IOException("File too large");
            }
            FileChannel channel = new FileInputStream(file).getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            byte[] result = new byte[(int) fullsize];
            buffer.get(result);
            return result;
        } catch (Exception e) {
        }
        return null;
    }
