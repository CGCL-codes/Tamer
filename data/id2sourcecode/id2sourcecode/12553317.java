    private XSWT createXSWT(URL inputURL, String formID, BindingContext ctx) {
        XSWT xswt = null;
        InputStream urlInput = null;
        InputStream newInput = null;
        try {
            urlInput = inputURL.openStream();
            newInput = checkInput(urlInput, formID, ctx);
            xswt = XSWTFactory.create(newInput, config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlInput != null) {
                try {
                    urlInput.close();
                } catch (IOException e) {
                }
            }
            if (newInput != null) {
                try {
                    newInput.close();
                } catch (IOException e) {
                }
            }
        }
        return xswt;
    }
