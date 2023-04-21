    public static InputStream openResource(String strRscName) throws IOException {
        URL urlSrc = ResourceManager.getResourceUrl(strRscName);
        InputStream isPath = urlSrc.openStream();
        return isPath;
    }
