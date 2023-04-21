    public CreatePDF(Patient patient, File path) {
        this.patient = patient;
        try {
            Document document = new Document(PageSize.A4, 20, 20, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
