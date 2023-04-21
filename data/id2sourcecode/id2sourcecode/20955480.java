    private InputSource urlLookup(ResourceLocation matchingEntry) {
        String uri = matchingEntry.getLocation();
        URL baseURL = null;
        if (matchingEntry.getBase() != null) {
            baseURL = matchingEntry.getBase();
        } else {
            try {
                baseURL = FILE_UTILS.getFileURL(getProject().getBaseDir());
            } catch (MalformedURLException ex) {
                throw new BuildException("Project basedir cannot be converted to a URL");
            }
        }
        InputSource source = null;
        URL url = null;
        try {
            url = new URL(baseURL, uri);
        } catch (MalformedURLException ex) {
        }
        if (url != null) {
            try {
                InputStream is = url.openStream();
                if (is != null) {
                    source = new InputSource(is);
                    String sysid = url.toExternalForm();
                    source.setSystemId(sysid);
                    log("catalog entry matched as a URL: '" + sysid + "'", Project.MSG_DEBUG);
                }
            } catch (IOException ex) {
            }
        }
        return source;
    }
