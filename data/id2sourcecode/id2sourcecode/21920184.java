    private static void copyStream(InputStream inStream, OutputStream outStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, read);
        }
    }
