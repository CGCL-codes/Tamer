    public static void main(String[] args) {
        System.out.println("Chapter 13: example Document Level JavaScript");
        System.out.println("-> Creates a PDF with a JavaScript method.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   document_level_javascript.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter13/document_level_javascript.pdf"));
            document.open();
            writer.addJavaScript("function saySomething(s) {app.alert('JS says: ' + s)}", false);
            writer.setAdditionalAction(PdfWriter.DOCUMENT_CLOSE, PdfAction.javaScript("saySomething('Thank you for reading this document.');\r", writer));
            document.add(new Paragraph("PDF document with a JavaScript function."));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
