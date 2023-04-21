    public boolean isCitexploreOnline() {
        if (isCitexploreActive) {
            return true;
        }
        if (log.isDebugEnabled()) log.debug("Checking citexplore status");
        try {
            URL url = new URL("http://www.ebi.ac.uk/webservices/citexplore/v1.0/service?wsdl");
            final URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1000);
            urlConnection.connect();
        } catch (Exception e) {
            log.debug("\tCitexplore is not reachable");
            isCitexploreActive = false;
            return false;
        }
        isCitexploreActive = true;
        return true;
    }
