    public void loadXmlInputFromURI(String docUri) throws ExecException {
        if (docUri == null) {
            inputXMLtree = null;
            return;
        }
        try {
            URL url = URI.create(docUri).toURL();
            URLConnection urlconn = url.openConnection();
            urlconn.setAllowUserInteraction(false);
            urlconn.setDefaultUseCaches(true);
            urlconn.setDoInput(true);
            urlconn.setDoOutput(false);
            urlconn.connect();
            InputStream is = (InputStream) urlconn.getContent();
            loadXmlInputFromStream(is);
            is.close();
        } catch (Exception e) {
            throw new ExecException("Failed to load XML data by URI: " + e);
        }
    }
