    public static String read(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            FileChannel fc = new RandomAccessFile(fileName, "r").getChannel();
            try {
                sb.append(Charset.forName(System.getProperty("file.encoding")).decode(fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())));
            } finally {
                fc.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
