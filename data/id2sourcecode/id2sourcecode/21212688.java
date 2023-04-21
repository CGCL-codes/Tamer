    public static void main(String[] args) {
        System.out.println("Chapter 2: example HelloWorldCopyForm");
        System.out.println("-> This is an example of how you SHOULDN'T copy forms.");
        System.out.println("   Take a look at HelloWorldCopyFields to know how it should be done.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   HelloWorldForm1.pdf");
        System.out.println("   HelloWorldForm2.pdf");
        System.out.println("   HelloWorldForm3.pdf");
        System.out.println("   HelloWorldCopyForm.pdf");
        createPdf("results/in_action/chapter02/HelloWorldForm1.pdf", "field1", "value1.1");
        createPdf("results/in_action/chapter02/HelloWorldForm2.pdf", "field1", "value1.2");
        createPdf("results/in_action/chapter02/HelloWorldForm3.pdf", "field2", "value2");
        try {
            PdfReader reader = new PdfReader("results/in_action/chapter02/HelloWorldForm1.pdf");
            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy writer = new PdfCopy(document, new FileOutputStream("results/in_action/chapter02/HelloWorldCopyForm.pdf"));
            document.open();
            writer.addPage(writer.getImportedPage(reader, 1));
            reader = new PdfReader("results/in_action/chapter02/HelloWorldForm2.pdf");
            writer.addPage(writer.getImportedPage(reader, 1));
            reader = new PdfReader("results/in_action/chapter02/HelloWorldForm3.pdf");
            writer.addPage(writer.getImportedPage(reader, 1));
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
