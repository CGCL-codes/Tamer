    public InputStream open(String filenameOrURI) {
        if (!hasScheme(filenameOrURI, "http:")) {
            if (FileManager.logAllLookups && log.isTraceEnabled()) log.trace("Not found: " + filenameOrURI);
            return null;
        }
        try {
            URL url = new URL(filenameOrURI);
            URLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", acceptHeader);
            conn.setRequestProperty("Accept-Charset", "utf-8,*");
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            if (in == null) {
                if (FileManager.logAllLookups && log.isTraceEnabled()) log.trace("Not found: " + filenameOrURI);
                return null;
            }
            if (FileManager.logAllLookups && log.isTraceEnabled()) log.trace("Found: " + filenameOrURI);
            return in;
        } catch (java.io.FileNotFoundException ex) {
            if (FileManager.logAllLookups && log.isTraceEnabled()) log.trace("LocatorURL: not found: " + filenameOrURI);
            return null;
        } catch (MalformedURLException ex) {
            log.warn("Malformed URL: " + filenameOrURI);
            return null;
        } catch (IOException ex) {
            if (ex instanceof ConnectException) {
                if (FileManager.logAllLookups && log.isTraceEnabled()) log.trace("LocatorURL: not found: " + filenameOrURI);
            } else log.warn("IO Exception opening URL: " + filenameOrURI + "  " + ex.getMessage());
            return null;
        }
    }
