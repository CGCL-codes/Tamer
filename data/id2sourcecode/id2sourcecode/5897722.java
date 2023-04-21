    public Object getContent() throws IOException {
        try {
            this.urlConnectionToResource = url.openConnection();
            this.urlConnectionToResource.connect();
            return this.urlConnectionToResource.getContent();
        } finally {
            this.release();
        }
    }
