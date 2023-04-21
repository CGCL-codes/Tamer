    private void copyInputStream(InputStream aSource, BufferedOutputStream aDest) throws IOException {
        byte[] buf = new byte[1024];
        int readed;
        while ((readed = aSource.read(buf, 0, 1024)) > 0) {
            aDest.write(buf, 0, readed);
        }
    }
