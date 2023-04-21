    public static String getStreamAsString(InputStream is) throws IOException {
        if (is == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        try {
            int b = -1;
            while ((b = is.read()) != -1) baos.write(b);
        } finally {
            is.close();
        }
        return new String(baos.toByteArray());
    }
