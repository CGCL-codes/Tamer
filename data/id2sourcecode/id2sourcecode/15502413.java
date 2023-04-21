    public InputStream getTransformXslIS(String sourceSchema, String targetSchema) throws FedoraAPIException {
        try {
            String strUrl = "http://" + rc.getFedoraHost() + ":" + rc.getFedoraPort() + "/services/" + sourceSchema + "_to_" + targetSchema + ".xsl";
            URL url = new URL(strUrl);
            return url.openStream();
        } catch (MalformedURLException mue) {
            throw new FedoraAPIException("Unable to get xsl transformation", mue);
        } catch (IOException ioe) {
            throw new FedoraAPIException("Unable to get xsl transformation", ioe);
        }
    }
