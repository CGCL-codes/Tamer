    public DocumentSummary parseDocument(URL url) throws IOException, DocumentHandlerException {
        DocumentSummary docSummary = new DocumentSummary();
        PDDocument pdfDocument = null;
        try {
            PDFParser parser = new PDFParser(url.openStream());
            parser.parse();
            pdfDocument = parser.getPDDocument();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.writeText(pdfDocument, writer);
            writer.close();
            byte[] contents = out.toByteArray();
            docSummary.contentReader = new InputStreamReader(new ByteArrayInputStream(contents));
            PDDocumentInformation info = pdfDocument.getDocumentInformation();
            if (info.getAuthor() != null) {
                docSummary.authors = new ArrayList();
                docSummary.authors.add(info.getAuthor());
            }
            if (info.getKeywords() != null) {
                docSummary.keywords = new ArrayList();
                docSummary.keywords.add(info.getKeywords());
            }
            if (info.getModificationDate() != null) {
                Date date = info.getModificationDate().getTime();
                if (date.getTime() >= 0) {
                    docSummary.modificationDate = date;
                }
            }
            if (info.getTitle() != null) {
                docSummary.title = info.getTitle();
            }
        } finally {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }
        return docSummary;
    }
