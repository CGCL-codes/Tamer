    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        writer.addJavaScript(Utilities.readFileToString(RESOURCE));
        PdfContentByte canvas = writer.getDirectContent();
        Font font = new Font(FontFamily.HELVETICA, 18);
        Rectangle rect;
        PdfFormField field;
        PdfFormField radiogroup = PdfFormField.createRadioButton(writer, true);
        radiogroup.setFieldName("language");
        RadioCheckField radio;
        for (int i = 0; i < LANGUAGES.length; i++) {
            rect = new Rectangle(40, 806 - i * 40, 60, 788 - i * 40);
            radio = new RadioCheckField(writer, rect, null, LANGUAGES[i]);
            radio.setBorderColor(GrayColor.GRAYBLACK);
            radio.setBackgroundColor(GrayColor.GRAYWHITE);
            radio.setCheckType(RadioCheckField.TYPE_CIRCLE);
            field = radio.getRadioField();
            radiogroup.addKid(field);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(LANGUAGES[i], font), 70, 790 - i * 40, 0);
        }
        writer.addAnnotation(radiogroup);
        PdfAppearance[] onOff = new PdfAppearance[2];
        onOff[0] = canvas.createAppearance(20, 20);
        onOff[0].rectangle(1, 1, 18, 18);
        onOff[0].stroke();
        onOff[1] = canvas.createAppearance(20, 20);
        onOff[1].setRGBColorFill(255, 128, 128);
        onOff[1].rectangle(1, 1, 18, 18);
        onOff[1].fillStroke();
        onOff[1].moveTo(1, 1);
        onOff[1].lineTo(19, 19);
        onOff[1].moveTo(1, 19);
        onOff[1].lineTo(19, 1);
        onOff[1].stroke();
        RadioCheckField checkbox;
        for (int i = 0; i < LANGUAGES.length; i++) {
            rect = new Rectangle(180, 806 - i * 40, 200, 788 - i * 40);
            checkbox = new RadioCheckField(writer, rect, LANGUAGES[i], "on");
            field = checkbox.getCheckField();
            field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off", onOff[0]);
            field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "On", onOff[1]);
            writer.addAnnotation(field);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(LANGUAGES[i], font), 210, 790 - i * 40, 0);
        }
        rect = new Rectangle(300, 806, 370, 788);
        PushbuttonField button = new PushbuttonField(writer, rect, "Buttons");
        button.setBackgroundColor(new GrayColor(0.75f));
        button.setBorderColor(GrayColor.GRAYBLACK);
        button.setBorderWidth(1);
        button.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
        button.setTextColor(GrayColor.GRAYBLACK);
        button.setFontSize(12);
        button.setText("Push me");
        button.setLayout(PushbuttonField.LAYOUT_ICON_LEFT_LABEL_RIGHT);
        button.setScaleIcon(PushbuttonField.SCALE_ICON_ALWAYS);
        button.setProportionalIcon(true);
        button.setIconHorizontalAdjustment(0);
        button.setImage(Image.getInstance(IMAGE));
        field = button.getField();
        field.setAction(PdfAction.javaScript("this.showButtonState()", writer));
        writer.addAnnotation(field);
        document.close();
    }
