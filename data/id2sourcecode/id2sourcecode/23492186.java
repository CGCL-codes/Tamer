    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            PdfReader reader = new PdfReader("results/in_action/chapter14/simple_letter.pdf");
            paper = writer.getImportedPage(reader, 1);
            not_printed = new PdfLayer("template", writer);
            not_printed.setOnPanel(false);
            not_printed.setPrint("Print", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
