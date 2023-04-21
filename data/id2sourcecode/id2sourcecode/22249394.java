    public static byte[] getResourceAsBytes(String path, ClassLoader loader) throws IOException {
        if (loader == null) loader = Thread.currentThread().getContextClassLoader();
        InputStream is = loader.getResourceAsStream(path);
        if (is == null) throw new FileNotFoundException("Resource not found: " + path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        try {
            int b = -1;
            while ((b = is.read()) != -1) baos.write(b);
        } finally {
            is.close();
        }
        return baos.toByteArray();
    }
