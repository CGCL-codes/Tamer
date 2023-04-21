    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapterX/read_out_loud.pdf"));
            writer.setTagged();
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfStructureTreeRoot root = writer.getStructureTreeRoot();
            PdfStructureElement div = new PdfStructureElement(root, new PdfName("Div"));
            PdfDictionary dict;
            cb.beginMarkedContentSequence(div);
            cb.beginText();
            cb.moveText(36, 788);
            cb.setFontAndSize(bf, 12);
            cb.setLeading(18);
            cb.showText("These are some famous movies by Stanley Kubrick: ");
            dict = new PdfDictionary();
            dict.put(PdfName.E, new PdfString("Doctor"));
            cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
            cb.newlineShowText("Dr.");
            cb.endMarkedContentSequence();
            cb.showText(" Strangelove or: How I Learned to Stop Worrying and Love the Bomb.");
            dict = new PdfDictionary();
            dict.put(PdfName.E, new PdfString("Eyes Wide Shut."));
            cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
            cb.newlineShowText("EWS");
            cb.endMarkedContentSequence();
            cb.endText();
            dict = new PdfDictionary();
            dict.put(PdfName.LANGUAGE, new PdfString("en-us"));
            dict.put(new PdfName("Alt"), new PdfString("2001: A Space Odyssey."));
            cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
            Image img = Image.getInstance("resources/in_action/chapterX/kubrick07.jpg");
            img.setAbsolutePosition(36, 734 - img.getScaledHeight());
            cb.addImage(img);
            cb.endMarkedContentSequence();
            cb.endMarkedContentSequence();
        } catch (Exception de) {
            de.printStackTrace();
        }
        document.close();
    }
