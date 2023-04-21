    public static Resource newResource(String resource) throws MalformedURLException, IOException {
        URL url = null;
        try {
            url = new URL(resource);
        } catch (MalformedURLException e) {
            if (!resource.startsWith("ftp:") && !resource.startsWith("file:") && !resource.startsWith("jar:")) {
                try {
                    if (resource.startsWith("./")) resource = resource.substring(2);
                    File file = new File(resource).getCanonicalFile();
                    url = file.toURI().toURL();
                    URLConnection connection = url.openConnection();
                    FileResource fileResource = new FileResource(url, connection, file);
                    return fileResource;
                } catch (Exception e2) {
                    log.debug(LogSupport.EXCEPTION, e2);
                    throw e;
                }
            } else {
                log.warn("Bad Resource: " + resource);
                throw e;
            }
        }
        String nurl = url.toString();
        if (nurl.length() > 0 && nurl.charAt(nurl.length() - 1) != resource.charAt(resource.length() - 1)) {
            if ((nurl.charAt(nurl.length() - 1) != '/' || nurl.charAt(nurl.length() - 2) != resource.charAt(resource.length() - 1)) && (resource.charAt(resource.length() - 1) != '/' || resource.charAt(resource.length() - 2) != nurl.charAt(nurl.length() - 1))) {
                return new BadResource(url, "Trailing special characters stripped by URL in " + resource);
            }
        }
        return newResource(url);
    }
