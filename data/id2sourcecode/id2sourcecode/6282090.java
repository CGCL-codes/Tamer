    public static byte[] read(URL _url) {
        byte[] r = null;
        try {
            InputStream in = _url.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (in.available() > 0) out.write(in.read());
            r = out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(FooLib.getClassName(ex.getClass()) + ":" + ex.getMessage());
        }
        return r;
    }
