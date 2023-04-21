    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        if (in != null && out != null) {
            byte buffer[] = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
        }
    }
