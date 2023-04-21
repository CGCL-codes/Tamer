    public static void main(String[] args) {
        Document.compress = false;
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.beginText();
            cb.setFontAndSize(bf, 12);
            cb.moveText(88.66f, 788);
            cb.showText("ld");
            cb.moveText(-22f, 0);
            cb.showText("Wor");
            cb.moveText(-15.33f, 0);
            cb.showText("llo");
            cb.endText();
            PdfTemplate tmp = cb.createTemplate(250, 25);
            tmp.beginText();
            tmp.setFontAndSize(bf, 12);
            tmp.moveText(0, 7);
            tmp.showText("He");
            tmp.endText();
            cb.addTemplate(tmp, 36, 781);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
