    public Boolean copy_stream(final Object[] r, final List bindings) throws IOException {
        InputStream is = (InputStream) r[0];
        OutputStream os = (OutputStream) r[1];
        byte[] buffer = new byte[sChunk];
        int length;
        while ((length = is.read(buffer, 0, sChunk)) != -1) os.write(buffer, 0, length);
        is.close();
        os.close();
        Object[] retobj = new Object[2];
        retobj[0] = r[0];
        retobj[1] = r[1];
        bindings.add(retobj);
        return Boolean.TRUE;
    }
