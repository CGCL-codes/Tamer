    public static void main(String[] args) {
        System.out.println("Chapter 2: example HelloWorldImportedPages");
        System.out.println("-> Creates a PDF file, then uses pages from");
        System.out.println("   this file as Images imported into a new PDF.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   HelloWorldToImport.pdf");
        System.out.println("   HelloWorldImportedPages.pdf");
        createPdf("results/in_action/chapter02/HelloWorldToImport.pdf");
        Document document = new Document(PageSize.A4);
        try {
            PdfReader reader = new PdfReader("results/in_action/chapter02/HelloWorldToImport.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter02/HelloWorldImportedPages.pdf"));
            document.open();
            System.out.println("Tampered? " + reader.isTampered());
            document.add(new Paragraph("This is page 1:"));
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            Image image = Image.getInstance(page);
            image.scalePercent(15f);
            image.setBorder(Rectangle.BOX);
            image.setBorderWidth(3f);
            image.setBorderColor(new GrayColor(0.5f));
            document.add(image);
            document.add(new Paragraph("This is page 2:"));
            page = writer.getImportedPage(reader, 2);
            image = Image.getInstance(page);
            image.scalePercent(15f);
            image.setBorder(Rectangle.BOX);
            image.setBorderWidth(3f);
            image.setBorderColor(new GrayColor(0.5f));
            document.add(image);
            document.add(new Paragraph("This is page 3:"));
            page = writer.getImportedPage(reader, 3);
            image = Image.getInstance(page);
            image.scalePercent(15f);
            image.setBorder(Rectangle.BOX);
            image.setBorderWidth(3f);
            image.setBorderColor(new GrayColor(0.5f));
            document.add(image);
            document.add(new Paragraph("This is page 4:"));
            page = writer.getImportedPage(reader, 4);
            image = Image.getInstance(page);
            image.scalePercent(15f);
            image.setBorder(Rectangle.BOX);
            image.setBorderWidth(3f);
            image.setBorderColor(new GrayColor(0.5f));
            document.add(image);
            System.out.println("Tampered? " + reader.isTampered());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }
