    public static byte[] readStream(InputStream stream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int numBytes = 0;
        byte[] buf = new byte[4096];
        try {
            while ((numBytes = stream.read(buf)) > 0) baos.write(buf, 0, numBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
        return baos.toByteArray();
    }
