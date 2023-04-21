    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfWriter writer = stamper.getWriter();
        writer.addJavaScript(Utilities.readFileToString(RESOURCE));
        AcroFields form = stamper.getAcroFields();
        AcroFields.Item fd = form.getFieldItem("married");
        PdfDictionary dictYes = (PdfDictionary) PdfReader.getPdfObject(fd.getWidgetRef(0));
        PdfDictionary yesAction = dictYes.getAsDict(PdfName.AA);
        if (yesAction == null) yesAction = new PdfDictionary();
        yesAction.put(new PdfName("Fo"), PdfAction.javaScript("setReadOnly(false);", stamper.getWriter()));
        dictYes.put(PdfName.AA, yesAction);
        PdfDictionary dictNo = (PdfDictionary) PdfReader.getPdfObject(fd.getWidgetRef(1));
        PdfDictionary noAction = dictNo.getAsDict(PdfName.AA);
        if (noAction == null) noAction = new PdfDictionary();
        noAction.put(new PdfName("Fo"), PdfAction.javaScript("setReadOnly(true);", stamper.getWriter()));
        dictNo.put(PdfName.AA, noAction);
        PushbuttonField button = new PushbuttonField(writer, new Rectangle(40, 690, 200, 710), "submit");
        button.setText("validate and submit");
        button.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
        PdfFormField validateAndSubmit = button.getField();
        validateAndSubmit.setAction(PdfAction.javaScript("validate();", stamper.getWriter()));
        stamper.addAnnotation(validateAndSubmit, 1);
        stamper.close();
    }
