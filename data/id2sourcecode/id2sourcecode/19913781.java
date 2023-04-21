    private static void copyAndClose(InputStream inputStream, BufferedOutputStream output) {
        byte[] buf = new byte[1024 * 8];
        try {
            int read;
            while ((read = inputStream.read(buf)) >= 0) {
                output.write(buf, 0, read);
            }
        } catch (IOException e) {
            throw new ContentException("error copying stream");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
