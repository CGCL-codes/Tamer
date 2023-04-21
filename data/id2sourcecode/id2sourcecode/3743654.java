    public static void createPdf() throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        TextField field = new TextField(writer, new Rectangle(36, 750, 144, 806), "iText");
        field.setFontSize(9);
        String[] list_options = { "JAVA", "C", "CS", "VB", "PHP" };
        field.setChoiceExports(list_options);
        String[] list_values = { "Java", "C/C++", "C#", "VB", "PHP" };
        field.setChoices(list_values);
        PdfFormField f = field.getListField();
        f.setFieldFlags(PdfFormField.FF_MULTISELECT);
        f.put(PdfName.I, new PdfArray(new int[] { 0, 2 }));
        writer.addAnnotation(f);
        document.close();
    }
