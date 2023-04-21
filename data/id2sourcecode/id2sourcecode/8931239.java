    public static String readText(File file, String encoding) throws IOException {
        FileChannel channel = new FileInputStream(file).getChannel();
        Charset charset = Charset.forName(encoding);
        return readText(channel, charset);
    }
