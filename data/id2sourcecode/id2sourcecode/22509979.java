    public static byte[] loadResource(String classpath) throws IOException {
        InputStream in = getResource(classpath);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }
