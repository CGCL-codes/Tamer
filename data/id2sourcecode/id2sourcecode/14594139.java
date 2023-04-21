    public ArrayList<ReferenceEntity> extractPDF(String urlString) throws IOException, DocumentHandlerException {
        URL url = new URL(urlString);
        InputStream in = new BufferedInputStream(url.openStream());
        FileProcessor fp = new FileProcessor();
        String text = fp.getDocumentText(in);
        ArrayList<ReferenceEntity> rList = fp.getFullCitations(text);
        return rList;
    }
