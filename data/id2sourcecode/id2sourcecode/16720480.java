    public void setAtomPropertiesFromURL(String propertiesURL) {
        try {
            AtomTypeSet ats1 = new AtomTypeSet();
            java.net.URL url1 = new java.net.URL(propertiesURL);
            ats1.load(url1.openStream());
        } catch (java.io.IOException e1) {
        }
        typesReady = true;
        displayIfAreWeReady();
    }
