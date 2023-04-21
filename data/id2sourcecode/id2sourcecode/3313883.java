    public static byte[] loadBinary(String path, ClassLoader cl) throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        InputStream is = getInputStream(path, cl);
        if (is == null) {
            return null;
        }
        int read;
        byte[] buffer = new byte[1024];
        while ((read = is.read(buffer)) > -1) {
            byteOutput.write(buffer, 0, read);
        }
        is.close();
        return byteOutput.toByteArray();
    }
