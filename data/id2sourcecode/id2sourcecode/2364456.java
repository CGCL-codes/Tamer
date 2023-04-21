    static void copy(InputStream fis, OutputStream fos, int bufferSize) throws IOException {
        byte buffer[] = new byte[bufferSize];
        int nbytes;
        while ((nbytes = fis.read(buffer)) != -1) fos.write(buffer, 0, nbytes);
    }
