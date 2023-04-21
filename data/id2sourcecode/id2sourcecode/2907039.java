    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) {
        int index = systemId.replace('\\', '/').lastIndexOf("/");
        String shortSystemId = systemId.substring(index + 1);
        if (this.doctype == null) {
            setDoctype(shortSystemId);
        }
        log.info("Trying to load " + systemId + ". In some cases (e.g. network interface up but no connection), this may take a bit.");
        try {
            URL url = new URL(systemId);
            URLConnection urlConn = url.openConnection();
            urlConn.setConnectTimeout(8000);
            urlConn.setReadTimeout(8000);
            urlConn.setAllowUserInteraction(false);
            InputStream is = urlConn.getInputStream();
            return new InputSource(is);
        } catch (IOException e) {
            log.error(e.toString() + ". May not be fatal.");
        }
        if (this.localDtdBase != null) {
            String localFileName = this.localDtdBase + "/" + shortSystemId;
            File dtdFile = new File(localFileName);
            log.debug("dtdfile: " + dtdFile.getAbsolutePath());
            if (dtdFile.exists() && dtdFile.isFile() && dtdFile.canRead()) {
                log.info("Using the local DTD " + localFileName);
                return new InputSource(dtdFile.getAbsolutePath());
            }
        }
        InputStream stream = this.getClass().getResourceAsStream("/dtd/" + shortSystemId);
        if (stream != null) {
            log.info("Using local DTD from jar-file " + shortSystemId);
            return new InputSource(stream);
        }
        log.info("Trying to access local dtd folder at standard location ./dtd...");
        File dtdFile = new File("./dtd/" + shortSystemId);
        if (dtdFile.exists() && dtdFile.isFile() && dtdFile.canRead()) {
            log.info("Using the local DTD " + dtdFile.getAbsolutePath());
            return new InputSource(dtdFile.getAbsolutePath());
        }
        log.warn("Could neither get the DTD from the web nor a local one. " + systemId);
        return null;
    }
