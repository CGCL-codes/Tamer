    public String getRevisionId(Entry entry, String revId) throws IOException {
        String url = getBaseApi() + "?action=query" + "&format=xml" + "&prop=revisions" + "&titles=" + escape(entry) + "&rvlimit=1" + "&rvprop=content|ids" + "&rvstartid=" + XMLUtilities.escape(revId);
        try {
            GetRevisionHandler h = new GetRevisionHandler(revId);
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
