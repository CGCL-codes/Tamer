    public List<HelpDocument> searchInHelpDocuments(URL helpIndex, String[] searchedWords) {
        List<URL> parsedDocuments = new ArrayList<URL>();
        parsedDocuments.add(helpIndex);
        List<HelpDocument> helpDocuments = new ArrayList<HelpDocument>();
        HTMLEditorKit html = new HTMLEditorKit();
        for (int i = 0; i < parsedDocuments.size(); i++) {
            URL helpDocumentUrl = (URL) parsedDocuments.get(i);
            Reader urlReader = null;
            try {
                urlReader = new InputStreamReader(helpDocumentUrl.openStream(), "ISO-8859-1");
                HelpDocument helpDocument = new HelpDocument(helpDocumentUrl, searchedWords);
                helpDocument.putProperty("IgnoreCharsetDirective", Boolean.FALSE);
                try {
                    html.read(urlReader, helpDocument, 0);
                } catch (ChangedCharSetException ex) {
                    String mimeType = ex.getCharSetSpec();
                    String encoding = mimeType.substring(mimeType.indexOf("=") + 1).trim();
                    urlReader.close();
                    urlReader = new InputStreamReader(helpDocumentUrl.openStream(), encoding);
                    helpDocument.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
                    html.read(urlReader, helpDocument, 0);
                }
                if (helpDocument.getRelevance() > 0) {
                    helpDocuments.add(helpDocument);
                }
                for (URL url : helpDocument.getReferencedDocuments()) {
                    String lowerCaseFile = url.getFile().toLowerCase();
                    if (lowerCaseFile.endsWith(".html") && !parsedDocuments.contains(url)) {
                        parsedDocuments.add(url);
                    }
                }
            } catch (IOException ex) {
            } catch (BadLocationException ex) {
            } finally {
                if (urlReader != null) {
                    try {
                        urlReader.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
        Collections.sort(helpDocuments, new Comparator<HelpDocument>() {

            public int compare(HelpDocument document1, HelpDocument document2) {
                return document2.getRelevance() - document1.getRelevance();
            }
        });
        return helpDocuments;
    }
