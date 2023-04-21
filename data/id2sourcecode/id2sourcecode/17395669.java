    protected byte[] readValueStream(InputStream in, int streamSize, int bufSize) throws IOException {
        byte[] buf = new byte[bufSize];
        int read;
        ByteArrayOutputStream out = (streamSize > 0) ? new ByteArrayOutputStream(streamSize) : new ByteArrayOutputStream();
        try {
            while ((read = in.read(buf, 0, bufSize)) >= 0) {
                out.write(buf, 0, read);
            }
            return out.toByteArray();
        } finally {
            in.close();
        }
    }
