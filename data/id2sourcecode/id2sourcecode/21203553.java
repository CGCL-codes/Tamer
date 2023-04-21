    private static byte[] loadBytecodePrivileged() {
        URL url = SecureRenderer.class.getResource("SecureRendererImpl.clazz");
        try {
            InputStream in = url.openStream();
            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                for (; ; ) {
                    int r = in.read();
                    if (r == -1) {
                        return bout.toByteArray();
                    }
                    bout.write(r);
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
