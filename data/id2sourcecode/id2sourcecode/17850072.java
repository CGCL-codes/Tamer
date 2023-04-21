    public static void main(String[] args) {
        System.out.println("Chapter 13: example Event Triggered Actions");
        System.out.println("-> Creates a PDF file with actions triggered by events (open, close, save, print).");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   event_triggered_actions.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter13/event_triggered_actions.pdf"));
            document.open();
            document.add(new Paragraph("Page 1"));
            document.newPage();
            document.add(new Paragraph("This PDF file jumps directly to page 2 when opened."));
            PdfContentByte cb = writer.getDirectContent();
            cb.localDestination("page2", new PdfDestination(PdfDestination.XYZ, -1, 10000, 0));
            writer.setOpenAction("page2");
            document.add(new Paragraph("It will warn you before printing the document."));
            PdfAction copyrightNotice = PdfAction.javaScript("app.alert('Warning: this document is protected by copyright.');\r", writer);
            writer.setAdditionalAction(PdfWriter.WILL_PRINT, copyrightNotice);
            document.add(new Paragraph("It will thank you for reading the document when closing it."));
            writer.setAdditionalAction(PdfWriter.DOCUMENT_CLOSE, PdfAction.javaScript("app.alert('Thank you for reading this document.');\r", writer));
            document.newPage();
            document.add(new Paragraph("Go to page three and back and you will also get alert messages."));
            writer.setPageAction(PdfWriter.PAGE_OPEN, PdfAction.javaScript("app.alert('You have reached page 3');\r", writer));
            writer.setPageAction(PdfWriter.PAGE_CLOSE, PdfAction.javaScript("app.alert('You have left page 3');\r", writer));
            document.add(new Paragraph("Page 3"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
