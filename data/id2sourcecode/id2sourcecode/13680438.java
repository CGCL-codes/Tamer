    private static void writeRawBytes(String file, String filename) throws IOException {
        final FileOutputStream fos;
        if (filename != null && filename.trim().length() > 0) {
            fos = new FileOutputStream(filename);
            log.debug("ELSE GET FILENAME FROM JETTY Filename is;:" + filename, "writeRawBytes");
        } else {
            fos = new FileOutputStream(getHomeFilename());
            log.debug("ELSE GET FILENAME FROM JETTY Filename to write is::" + getHomeFilename(), "writeRawBytes");
        }
        FileChannel fc = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(file.getBytes());
        fc.write(buffer);
        fc.close();
    }
