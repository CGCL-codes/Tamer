    public static void unzipToStream(byte[] buff, OutputStream os) throws IOException {
        ZipInputStream zips = new ZipInputStream(new ByteArrayInputStream(buff));
        zips.getNextEntry();
        byte[] readBuffer = new byte[4096];
        int nread;
        while ((nread = zips.read(readBuffer)) > 0) {
            os.write(readBuffer, 0, nread);
        }
        zips.closeEntry();
        zips.close();
    }
