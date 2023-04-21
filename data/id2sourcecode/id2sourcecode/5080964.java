    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document(new Rectangle(360, 360));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        writer.addJavaScript(Utilities.readFileToString(RESOURCE));
        for (int i = 0; i < 10; i++) {
            addPushButton(writer, digits[i], String.valueOf(i), "this.augment(" + i + ")");
        }
        addPushButton(writer, plus, "+", "this.register('+')");
        addPushButton(writer, minus, "-", "this.register('-')");
        addPushButton(writer, mult, "x", "this.register('*')");
        addPushButton(writer, div, ":", "this.register('/')");
        addPushButton(writer, equals, "=", "this.calculateResult()");
        addPushButton(writer, clearEntry, "CE", "this.reset(false)");
        addPushButton(writer, clear, "C", "this.reset(true)");
        addTextField(writer, result, "result");
        addTextField(writer, move, "move");
        document.close();
    }
