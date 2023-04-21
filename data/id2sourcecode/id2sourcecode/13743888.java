    public static byte[] readData(InputStream is) throws IOException {
        byte[] buffer = new byte[10240];
        ByteArrayOutputStream bs = new ByteArrayOutputStream(10240);
        int len;
        try {
            while ((len = is.read(buffer, 0, buffer.length)) >= 0) bs.write(buffer, 0, len);
            is.close();
            return bs.toByteArray();
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e2) {
            throw new IOException(e2.getMessage());
        }
    }
