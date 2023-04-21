    public static byte[] transform(Templates templates, SAXParser parser, Resolver resolver, RequestTracker.Errors listener, Map parameters) throws TransformerConfigurationException, IOException, CMSException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFER_SIZE);
        Utf8StreamWriter writer = Utf8StreamWriter.getThreadLocal().setOutputStream(os);
        Result result = new StreamResult(writer);
        transform(templates, parser, resolver, listener, parameters, result);
        writer.close();
        return os.toByteArray();
    }
