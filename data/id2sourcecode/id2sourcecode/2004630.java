    public static void main(String[] args) {
        System.out.println("Chapter 15: example Choice Fields");
        System.out.println("-> Creates a PDF file with widget annotations of type field;");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   choicefields.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter15/choicefields.pdf"));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            String options[] = { "English", "French", "Dutch", "German" };
            PdfFormField combo = PdfFormField.createCombo(writer, true, options, 0);
            combo.setWidget(new Rectangle(40, 780, 120, 800), PdfAnnotation.HIGHLIGHT_INVERT);
            combo.setFieldName("languageCombo");
            combo.setValueAsString("English");
            writer.addAnnotation(combo);
            PdfFormField field = PdfFormField.createList(writer, options, 0);
            PdfAppearance app = cb.createAppearance(80, 60);
            app.rectangle(1, 1, 78, 58);
            app.stroke();
            field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, app);
            field.setWidget(new Rectangle(140, 740, 220, 800), PdfAnnotation.HIGHLIGHT_OUTLINE);
            field.setFieldName("languageList");
            field.setValueAsString("English");
            writer.addAnnotation(field);
            TextField tf1 = new TextField(writer, new Rectangle(240, 740, 290, 800), "comboLanguage");
            tf1.setBackgroundColor(Color.YELLOW);
            tf1.setBorderColor(Color.BLUE);
            tf1.setBorderWidth(2);
            tf1.setFontSize(10);
            tf1.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
            tf1.setVisibility(TextField.VISIBLE_BUT_DOES_NOT_PRINT);
            tf1.setChoices(new String[] { "English", "French" });
            tf1.setChoiceExports(new String[] { "EN", "FR" });
            tf1.setRotation(90);
            writer.addAnnotation(tf1.getComboField());
            TextField tf2 = new TextField(writer, new Rectangle(300, 740, 400, 800), "listLetters");
            tf2.setBackgroundColor(Color.YELLOW);
            tf2.setBorderColor(Color.RED);
            tf2.setBorderWidth(2);
            tf2.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
            tf2.setFontSize(10);
            tf2.setChoices(new String[] { "a", "b", "c", "d", "e", "f", "g", "h" });
            tf2.setChoiceSelection(4);
            writer.addAnnotation(tf2.getListField());
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.close();
    }
