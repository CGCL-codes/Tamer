    public org.w3c.dom.Document getResourceAsDocument() throws DomHandlerException {
        if (url == null) {
            throw new IllegalStateException("url property not set.");
        }
        if (this.lastCheck == 0) {
            this.lastCheck = System.currentTimeMillis();
            load();
        } else if (this.millisBetweenChecks == 0 || this.lastCheck + this.millisBetweenChecks > System.currentTimeMillis()) {
            this.lastCheck = System.currentTimeMillis();
            try {
                URLConnection connection = url.openConnection();
                if (connection.getLastModified() > this.lastModified) {
                    load();
                }
            } catch (IOException e) {
                throw new DomHandlerException(e);
            }
        }
        return this.document;
    }
