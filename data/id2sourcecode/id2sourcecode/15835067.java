    public String getContent(Entry entry) throws IOException {
        String url = getBaseApi() + "?action=query" + "&format=xml" + "&prop=revisions" + "&titles=" + escape(entry) + "&rvprop=content";
        try {
            GetRevisionHandler h = new GetRevisionHandler(null);
            InputStream in = openStream(url);
            this.saxParser.parse(in, h);
            in.close();
            if (h.getContent() == null) {
                System.err.println("(no content for " + url + ")");
            }
            return h.getContent();
        } catch (SAXException e1) {
            throw new IOException(e1);
        }
    }
