    public static final void copyFromURL(String source, String destination) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        URL url = new URL(source);
        BufferedInputStream input;
        BufferedOutputStream output;
        input = new BufferedInputStream(new DataInputStream(url.openStream()));
        output = new BufferedOutputStream(new FileOutputStream(destination));
        copyStream(input, output, buffer);
        input.close();
        output.close();
    }
