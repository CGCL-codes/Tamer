    public static WorkflowDescriptor load(final URL url, boolean validate) throws SAXException, IOException, InvalidWorkflowDescriptorException {
        return load(url.openStream(), url, validate);
    }
