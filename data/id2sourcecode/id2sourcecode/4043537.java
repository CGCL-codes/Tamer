    public void createDocument() {
        File fl = new File(PrintAbstract.print_root + this.getName());
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fl.getAbsoluteFile()));
            writer.setPageEvent(this);
            document.open();
            document.add(getTitle());
            fillDocumentBody(document);
        } catch (Exception de) {
            log.error("Error on document creation [" + de.getMessage() + "]: " + de, de);
            de.printStackTrace();
        }
        document.close();
    }
