    protected URLConnection openConnection(final URL url) throws IOException {
        final FileObject entry = context.resolveFile(url.toExternalForm(), fileSystemOptions);
        return new DefaultURLConnection(url, entry.getContent());
    }
