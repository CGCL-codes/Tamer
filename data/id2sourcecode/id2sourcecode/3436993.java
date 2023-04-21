    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            String[] languages = { "English", "French", "Dutch" };
            Rectangle rect;
            PdfFormField language = PdfFormField.createRadioButton(writer, true);
            language.setFieldName("language");
            language.setValueAsName(languages[0]);
            for (int i = 0; i < languages.length; i++) {
                rect = new Rectangle(40, 806 - i * 40, 60, 788 - i * 40);
                addRadioButton(writer, rect, language, languages[i], i == 0, writer.getPageNumber() + i);
            }
            writer.addAnnotation(language);
            for (int i = 0; i < languages.length; i++) {
                cb.beginText();
                cb.setFontAndSize(bf, 18);
                cb.showTextAligned(Element.ALIGN_LEFT, languages[i], 70, 790 - i * 40, 0);
                cb.endText();
                document.newPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }
