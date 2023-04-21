    public Assets request(URL url) {
        try {
            log.info("Executing " + url);
            return xmlUtil.unmarshall(urlOpener.openStream(url));
        } catch (FileNotFoundException e) {
            log.info("File not found: " + url);
        } catch (IOException e) {
            log.error("Failed to read from url: " + url + ". " + e.getMessage(), e);
        }
        return null;
    }
