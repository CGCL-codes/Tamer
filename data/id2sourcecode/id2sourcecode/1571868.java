    public void check(SourceIdentifier original, boolean indent) throws UnsupportedEncodingException, XMLStreamException {
        if (logger.isDebugEnabled()) logger.debug("Processing:\n{}", original);
        byte[] bytes = write(original, indent);
        if (bytes == null) {
            return;
        }
        String originalStr = new String(bytes, "UTF-8");
        if (logger.isDebugEnabled()) logger.debug("Marshalled to:\n{}", originalStr);
        SourceIdentifier read = read(bytes);
        if (logger.isDebugEnabled()) logger.debug("Read.");
        assertEquals(original, read);
        if (logger.isDebugEnabled()) logger.debug("Equal.");
        bytes = write(read, indent);
        String readStr = new String(bytes, "UTF-8");
        assertEquals(originalStr, readStr);
        if (logger.isDebugEnabled()) logger.debug("Strings equal.");
    }
