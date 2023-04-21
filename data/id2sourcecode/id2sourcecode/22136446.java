    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buf = new byte[1048576];
        int length = -1;
        while ((length = inputStream.read(buf)) != -1) outputStream.write(buf, 0, length);
        inputStream.close();
        outputStream.close();
    }
