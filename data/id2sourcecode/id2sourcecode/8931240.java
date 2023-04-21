    public static void writeText(String text, File file, String encoding) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        writeText(text, channel, Charset.forName(encoding));
    }
