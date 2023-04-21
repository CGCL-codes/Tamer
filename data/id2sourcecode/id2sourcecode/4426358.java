    public static void createStationary(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(new Phrase("FOOBAR FILM FESTIVAL", FilmFonts.BOLD));
        document.add(table);
        Font font = new Font(FontFamily.HELVETICA, 52, Font.BOLD, new GrayColor(0.75f));
        ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase("FOOBAR FILM FESTIVAL", font), 297.5f, 421, 45);
        document.close();
    }
