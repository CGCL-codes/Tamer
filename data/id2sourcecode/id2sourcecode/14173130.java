    public static ByteArrayInputStream getResourceAsStream(String path) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (is == null) throw new FileNotFoundException("Resource not found: " + path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        try {
            int b = -1;
            while ((b = is.read()) != -1) baos.write(b);
        } finally {
            is.close();
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }
