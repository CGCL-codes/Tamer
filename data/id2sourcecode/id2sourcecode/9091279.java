    public static void createOneCard() throws DocumentException, IOException {
        Rectangle rect = new Rectangle(Utilities.millimetersToPoints(86.5f), Utilities.millimetersToPoints(55));
        Document document = new Document(rect);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(CARD));
        writer.setViewerPreferences(PdfWriter.PrintScalingNone);
        document.open();
        PdfReader reader = new PdfReader(LOGO);
        Image img = Image.getInstance(writer.getImportedPage(reader, 1));
        img.scaleToFit(rect.getWidth() / 1.5f, rect.getHeight() / 1.5f);
        img.setAbsolutePosition((rect.getWidth() - img.getScaledWidth()) / 2, (rect.getHeight() - img.getScaledHeight()) / 2);
        document.add(img);
        document.newPage();
        BaseFont bf = BaseFont.createFont(FONT, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, 12);
        font.setColor(new CMYKColor(1, 0.5f, 0, 0.467f));
        ColumnText column = new ColumnText(writer.getDirectContent());
        Paragraph p;
        p = new Paragraph("Bruno Lowagie\n1T3XT\nbruno@1t3xt.com", font);
        p.setAlignment(Element.ALIGN_CENTER);
        column.addElement(p);
        column.setSimpleColumn(0, 0, rect.getWidth(), rect.getHeight() * 0.75f);
        column.go();
        document.close();
    }
