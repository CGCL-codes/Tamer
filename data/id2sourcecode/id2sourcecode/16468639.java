    public void importObjects(InputStream in) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte b = -1;
        while ((b = (byte) in.read()) > -1) buf.write(b);
        InputStream bin = new ByteArrayInputStream(buf.toByteArray());
        XMLDecoder decoder = new XMLDecoder(bin);
        decoder.setExceptionListener(new LogExceptionListener());
        reset();
        objectsByUri = (Map) decoder.readObject();
        reverseMap(objectsByUri, urisByObject);
        decoder.close();
    }
