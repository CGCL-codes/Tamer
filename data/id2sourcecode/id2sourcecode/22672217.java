    @Override
    public void invoke(InputStream is, OutputStream os, String objectId, SerializerFactory serializerFactory) throws Exception {
        CharArrayWriter writer = new CharArrayWriter();
        _threadWriter.set(writer);
        PrintWriter dbg = new PrintWriter(writer);
        HessianDebugInputStream debug = new HessianDebugInputStream(is, dbg);
        debug.startTop2();
        super.invoke(debug, os, objectId, serializerFactory);
    }
