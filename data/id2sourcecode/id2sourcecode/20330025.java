    protected Element loadDocument(String location) throws ConfigureException {
        URL url = null;
        try {
            url = new URL("file", "", 80, location);
            logger_.debug("URL location is " + url.toString());
            return loadDocument(url.openStream());
        } catch (java.net.MalformedURLException mfx) {
            logger_.error("Configurator error: " + mfx);
            mfx.printStackTrace();
        } catch (java.io.IOException e) {
            logger_.error("Configurator error: " + e);
            e.printStackTrace();
        }
        return null;
    }
