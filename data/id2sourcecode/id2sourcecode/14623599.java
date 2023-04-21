    public static String inputStreamToString(InputStream x, int length) throws IOException {
        InputStreamReader in = new InputStreamReader(x);
        StringWriter writer = new StringWriter();
        int blocksize = 8 * 1024;
        char[] buffer = new char[blocksize];
        for (int left = length; left > 0; ) {
            int read = in.read(buffer, 0, left > blocksize ? blocksize : left);
            if (read == -1) {
                break;
            }
            writer.write(buffer, 0, read);
            left -= read;
        }
        writer.close();
        return writer.toString();
    }
