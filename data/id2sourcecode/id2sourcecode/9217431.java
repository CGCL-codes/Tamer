    private void saveStream(InputStream responseBodyAsStream, File cacheResourceFile) throws IOException {
        createNewFile(cacheResourceFile);
        BufferedOutputStream destStream = new BufferedOutputStream(new FileOutputStream(cacheResourceFile));
        try {
            int readByte;
            while ((readByte = responseBodyAsStream.read()) != -1) {
                destStream.write(readByte);
            }
        } finally {
            destStream.flush();
            destStream.close();
        }
    }
