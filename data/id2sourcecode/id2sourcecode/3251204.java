    public static void write(String fileName, String text) {
        try {
            FileChannel fc = new RandomAccessFile(new File(fileName).getAbsoluteFile(), "rw").getChannel();
            try {
                fc.map(FileChannel.MapMode.READ_WRITE, 0, text.length()).put(text.getBytes());
            } finally {
                fc.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
