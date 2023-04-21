    protected <T> T parseBody(InputStream body, final Handler<T> handler) throws ParserConfigurationException, SAXException, IOException {
        if (body == null) return null;
        if (logger.isLoggable(Level.FINE)) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buff = new byte[1024];
            int size = 0;
            while ((size = body.read(buff, 0, buff.length)) > -1) baos.write(buff, 0, size);
            final byte[] data = baos.toByteArray();
            logger.fine(new String(data, "UTF-8"));
            body = new ByteArrayInputStream(data);
        }
        final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(body, handler);
        return handler.getObject();
    }
