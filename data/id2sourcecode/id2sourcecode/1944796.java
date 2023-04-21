    public InputStream getInputStream(URL url) throws PackageManagerException, IOException {
        if (isProxyInUse()) return getInputProxyStream(url.toString()); else return url.openStream();
    }
