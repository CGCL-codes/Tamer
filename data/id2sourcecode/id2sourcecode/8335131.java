    private String getResultOfURL(String uri) {
        String ret = "Nothing happened!!";
        try {
            URL url = new URL(uri);
            InputStream is = url.openStream();
            BufferedReader inStream = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = inStream.readLine()) != null) {
                ret = line;
            }
            inStream.close();
            is.close();
        } catch (MalformedURLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            ret = e.getMessage();
        } catch (IOException e) {
            if (logger.isInfoEnabled()) {
                logger.info("IOException: " + e.getMessage());
            }
            ret = e.getMessage();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Returning " + ret);
        }
        return ret;
    }
