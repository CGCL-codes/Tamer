    private static URL getURL(URL contextURL, String spec, int recursiveDepth) throws MalformedURLException {
        URL url = null;
        try {
            url = new URL(contextURL, spec);
            try {
                url.openStream();
            } catch (IOException ioe1) {
                throw new MalformedURLException("This file was not found: " + url);
            }
        } catch (MalformedURLException e1) {
            url = new URL("file", "", spec);
            try {
                url.openStream();
            } catch (IOException ioe2) {
                if (contextURL != null) {
                    String contextFileName = contextURL.getFile();
                    String parentName = new File(contextFileName).getParent();
                    if (parentName != null && recursiveDepth < 3) {
                        return getURL(new URL("file", "", parentName + '/'), spec, recursiveDepth + 1);
                    }
                }
                throw new MalformedURLException("This file was not found: " + url);
            }
        }
        return url;
    }
