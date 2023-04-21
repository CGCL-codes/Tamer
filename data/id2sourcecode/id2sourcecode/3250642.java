    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            PdfReader reader = new PdfReader(FormFillOut1.FORM);
            background = writer.getImportedPage(reader, 1);
            AcroFields fields = reader.getAcroFields();
            FieldPosition pos;
            pos = fields.getFieldPositions("name").get(0);
            fillArray(name, pos.position);
            pos = fields.getFieldPositions("bio").get(0);
            fillArray(bio, pos.position);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
