    public TemplateConfiguration parseUrl(URL url) throws IOException, SAXException {
        InputStream in = null;
        try {
            in = url.openStream();
            return this.parse(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
