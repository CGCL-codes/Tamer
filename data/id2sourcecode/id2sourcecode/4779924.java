    public static void main(String[] args) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 100);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            writer.setPageEvent(new AddTableAsHeaderFooter());
            document.open();
            String text = "Lots of text. ";
            for (int i = 0; i < 5; i++) text += text;
            for (int i = 0; i < 20; i++) document.add(new Paragraph(text));
            document.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
