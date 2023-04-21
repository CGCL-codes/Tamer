    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            Paragraph p = new Paragraph(StarSeparators.TEXT);
            VerticalPositionMark separator = new LineSeparator(1, 100, Color.RED, Element.ALIGN_RIGHT, -2);
            p.add(separator);
            ColumnText column = new ColumnText(writer.getDirectContent());
            for (int i = 0; i < 5; i++) {
                column.addElement(p);
            }
            column.setSimpleColumn(36, 36, 295, 806);
            column.go();
            column.setSimpleColumn(300, 36, 559, 806);
            column.go();
            document.newPage();
            for (int i = 0; i < 10; i++) {
                document.add(p);
            }
            document.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
