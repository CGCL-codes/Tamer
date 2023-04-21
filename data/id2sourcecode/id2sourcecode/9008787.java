    private void includeDocument(PdfWriter writer, Document document, PdfFileNode n, CommonOptions opt) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(new FileInputStream(n.getFileName()));
        for (int j = 1; j <= reader.getNumberOfPages(); j++) {
            PdfImportedPage page = writer.getImportedPage(reader, j);
            Image image = Image.getInstance(page);
            image.setRotationDegrees(n.getRotation().getAngle());
            image.scalePercent(95f);
            document.add(image);
        }
        reader.close();
        document.newPage();
    }
