    public static void transform(Templates templates, InputSource source, Resolver resolver, RequestTracker.Errors listener, Map parameters, OutputStream out) throws TransformerConfigurationException, IOException, CMSException {
        Utf8StreamWriter writer = Utf8StreamWriter.getThreadLocal().setOutputStream(out);
        transform(templates, source, resolver, listener, parameters, writer);
        out.close();
    }
