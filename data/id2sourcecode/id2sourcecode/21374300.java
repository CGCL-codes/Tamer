    public void writeTo(Object object, String mimeType, OutputStream out) throws IOException {
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            out.write(bytes);
            out.flush();
        } else if (object instanceof InputStream) {
            InputStream in = (InputStream) object;
            byte[] bytes = new byte[4096];
            for (int len = in.read(bytes); len != -1; len = in.read(bytes)) out.write(bytes, 0, len);
            out.flush();
        } else throw new IOException("Unsupported data type: " + object.getClass().getName());
    }
