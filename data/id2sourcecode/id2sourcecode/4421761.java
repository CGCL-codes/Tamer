    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            Paragraph p = new Paragraph(StarSeparators.TEXT);
            p.add(0, new PositionedMarks(true));
            p.add(new PositionedMarks(false));
            ColumnText column = new ColumnText(writer.getDirectContent());
            for (int i = 0; i < 5; i++) {
                column.addElement(p);
            }
            column.setSimpleColumn(36, 36, 275, 806);
            column.go();
            column.setSimpleColumn(320, 36, 559, 806);
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
