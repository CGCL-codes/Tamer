    public static byte[] getByteArrayFromDocument(Document doc, boolean asFragment) throws CMSException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer writer = Utf8StreamWriter.getThreadLocal().setOutputStream(out);
        writeDocument(doc, writer, asFragment);
        return out.toByteArray();
    }
