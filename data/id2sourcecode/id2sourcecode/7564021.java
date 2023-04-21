    private boolean isXHTML(String pluginId, URL url) {
        if (xhtmlDescriber == null) {
            xhtmlDescriber = new XHTMLContentDescriber();
        }
        InputStream in = null;
        try {
            in = url.openStream();
            return (xhtmlDescriber.describe(in, null) == IContentDescriber.VALID);
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }
