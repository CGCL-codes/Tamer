    public static final void copyInputStream(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(IOHelper.class).error("Failed to copy stream", ex);
        }
    }
