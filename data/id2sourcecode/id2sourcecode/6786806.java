    public static IVirtualDirectory createFor(URL url, String resourceName) {
        try {
            String urlPath = url.getFile();
            urlPath = URLDecoder.decode(urlPath, "UTF-8");
            if (resourceName != null) urlPath = urlPath.substring(0, urlPath.length() - resourceName.length());
            if (urlPath.startsWith("vas:") || urlPath.startsWith("virtual:")) {
                try {
                    URLConnection connection = new URL(urlPath).openConnection();
                    if (connection instanceof VirtualArtifactURLConnection) {
                        IVirtualArtifact artifact = ((VirtualArtifactURLConnection) connection).getArtifact();
                        if (artifact instanceof IVirtualDirectory) return (IVirtualDirectory) artifact;
                    }
                } catch (IOException e) {
                    throw ThrowableManagerRegistry.caught(e);
                }
            }
            if (urlPath.startsWith("file:")) urlPath = urlPath.substring(5);
            if (urlPath.indexOf('!') > 0) urlPath = urlPath.substring(0, urlPath.indexOf('!'));
            File file = new File(urlPath);
            return file.isDirectory() ? new SystemDirectory(file) : new ZippedDirectory(new SystemFile(file));
        } catch (UnsupportedEncodingException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
