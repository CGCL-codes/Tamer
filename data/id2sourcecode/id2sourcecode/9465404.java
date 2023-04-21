    protected ProtectedStream openStream(Element e, ParsedURL purl) throws IOException {
        List mimeTypes = new ArrayList(ImageTagRegistry.getRegistry().getRegisteredMimeTypes());
        mimeTypes.add(MimeTypeConstants.MIME_TYPES_SVG);
        InputStream reference = purl.openStream(mimeTypes.iterator());
        return new ProtectedStream(reference);
    }
