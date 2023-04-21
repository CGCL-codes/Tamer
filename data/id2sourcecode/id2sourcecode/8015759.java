    public static void createPdf(ZipOutputStream zip, int counter) throws IOException, DocumentException {
        ZipEntry entry = new ZipEntry("document" + counter + ".pdf");
        zip.putNextEntry(entry);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, zip);
        writer.setCloseStream(false);
        document.open();
        document.add(new Paragraph("Document " + counter));
        document.close();
        zip.closeEntry();
    }
