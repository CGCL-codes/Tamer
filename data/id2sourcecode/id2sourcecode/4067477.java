    @SuppressWarnings("unchecked")
    public static void createOriginal() {
        Document document = new Document();
        try {
            document.addAuthor("Bruno Lowagie");
            document.addKeywords("Hello World, XMP, Metadata");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ORIGINAL));
            writer.createXmpMetadata();
            document.open();
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
