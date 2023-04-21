    private InputSource copyRootConfiguration(java.io.InputStream input) throws IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int nRead;
        while ((nRead = input.read(buffer)) > 0) baos.write(buffer, 0, nRead);
        buffer = baos.toByteArray();
        rootConfiguration = new String(buffer);
        return new InputSource(new java.io.ByteArrayInputStream(buffer));
    }
