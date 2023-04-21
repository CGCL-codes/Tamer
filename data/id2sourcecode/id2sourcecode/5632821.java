    protected static void transferStreams(InputStream is, OutputStream os) throws IOException {
        try {
            byte[] buf = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = is.read(buf)) != -1) os.write(buf, 0, bytesRead);
        } finally {
            is.close();
            os.close();
        }
    }
