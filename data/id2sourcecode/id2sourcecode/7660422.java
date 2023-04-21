    public static void main(String[] args) {
        System.out.println("Chapter 2: example HelloWorldStampCopy");
        System.out.println("-> Creates a PDF in multiple passes;");
        System.out.println("   first do the stamping, then the copying.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   HelloLetter.pdf");
        System.out.println("   HelloWorldStampCopy.pdf");
        createPdf("results/in_action/chapter02/HelloLetter.pdf", "field", "value");
        PdfReader reader;
        PdfStamper stamper;
        AcroFields form;
        try {
            RandomAccessFileOrArray letter = new RandomAccessFileOrArray("results/in_action/chapter02/HelloLetter.pdf");
            reader = new PdfReader(letter, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, baos);
            form = stamper.getAcroFields();
            form.setField("field", "World,");
            stamper.setFormFlattening(true);
            stamper.close();
            reader = new PdfReader(baos.toByteArray());
            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy writer = new PdfCopy(document, new FileOutputStream("results/in_action/chapter02/HelloWorldStampCopy.pdf"));
            document.open();
            writer.addPage(writer.getImportedPage(reader, 1));
            reader = new PdfReader(letter, null);
            baos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, baos);
            form = stamper.getAcroFields();
            form.setField("field", "People,");
            stamper.setFormFlattening(true);
            stamper.close();
            reader = new PdfReader(baos.toByteArray());
            writer.addPage(writer.getImportedPage(reader, 1));
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
