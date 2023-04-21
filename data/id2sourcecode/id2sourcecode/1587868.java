    public RegistryLoaderJython(URL url) throws IOException {
        scriptDescription = url.toExternalForm();
        InputStream is = url.openStream();
        script = stream2String(is);
        is.close();
    }
