    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfWriter writer = stamper.getWriter();
        PdfAction action = PdfAction.javaScript(Utilities.readFileToString(JS1), writer);
        writer.setOpenAction(action);
        PushbuttonField button1 = new PushbuttonField(stamper.getWriter(), new Rectangle(90, 660, 160, 690), "post");
        button1.setText("POST TO HTML");
        button1.setBackgroundColor(new GrayColor(0.7f));
        button1.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
        PdfFormField submit1 = button1.getField();
        submit1.setAction(PdfAction.javaScript(Utilities.readFileToString(JS2), writer));
        stamper.addAnnotation(submit1, 1);
        stamper.close();
    }
