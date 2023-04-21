    public DocumentSummary parseDocument(URL url) throws IOException, DocumentHandlerException {
        DocumentSummary result = new DocumentSummary();
        String mimeType = null;
        Document doc = new Document("top", null, null);
        try {
            Cache cache = Multivalent.getInstance().getCache();
            String genre = cache.getGenre(mimeType, url.toString(), null);
            if ("RawImage".equals(genre) || genre == "ASCII") return null;
            MediaAdaptor mediaAdapter = (MediaAdaptor) Behavior.getInstance(genre, genre, null, null, null);
            if (!url.getProtocol().equals("file")) {
                throw new DocumentHandlerException("Multivalent handler can take only local files");
            }
            File file = new File(url.getPath());
            mediaAdapter.docURI = file.toURI();
            InputStream is = new CachedInputStream(url.openStream(), file, null);
            mediaAdapter.setInputStream(is);
            mediaAdapter.setHints(MediaAdaptor.HINT_NO_IMAGE | MediaAdaptor.HINT_NO_SHAPE | MediaAdaptor.HINT_EXACT | MediaAdaptor.HINT_NO_TRANSCLUSIONS);
            mediaAdapter.parse(doc);
            if (doc.getAttr("author") != null) {
                List authors = new LinkedList();
                authors.add(doc.getAttr("author"));
                result.authors = authors;
            }
            if (doc.getAttr("title") != null) {
                result.title = doc.getAttr("title");
            }
            if (doc.getAttr("summary") != null) {
                result.summary = doc.getAttr("summary");
            }
            if (doc.getAttr("keywords") != null) {
                result.summary = doc.getAttr("keywords");
            }
            result.contentReader = getDocumentContent(doc, mediaAdapter);
            mediaAdapter.closeInputStream();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocumentHandlerException("Caught Exception", e);
        }
    }
