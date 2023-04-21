    public void manipulatePdf(String src, String dest, int pow) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        Rectangle pageSize = reader.getPageSize(1);
        Rectangle newSize = (pow % 2) == 0 ? new Rectangle(pageSize.getWidth(), pageSize.getHeight()) : new Rectangle(pageSize.getHeight(), pageSize.getWidth());
        Rectangle unitSize = new Rectangle(pageSize.getWidth(), pageSize.getHeight());
        for (int i = 0; i < pow; i++) {
            unitSize = new Rectangle(unitSize.getHeight() / 2, unitSize.getWidth());
        }
        int n = (int) Math.pow(2, pow);
        int r = (int) Math.pow(2, pow / 2);
        int c = n / r;
        Document document = new Document(newSize, 0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(String.format(dest, n)));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfImportedPage page;
        Rectangle currentSize;
        float offsetX, offsetY, factor;
        int total = reader.getNumberOfPages();
        for (int i = 0; i < total; ) {
            if (i % n == 0) {
                document.newPage();
            }
            currentSize = reader.getPageSize(++i);
            factor = Math.min(unitSize.getWidth() / currentSize.getWidth(), unitSize.getHeight() / currentSize.getHeight());
            offsetX = unitSize.getWidth() * ((i % n) % c) + (unitSize.getWidth() - (currentSize.getWidth() * factor)) / 2f;
            offsetY = newSize.getHeight() - (unitSize.getHeight() * (((i % n) / c) + 1)) + (unitSize.getHeight() - (currentSize.getHeight() * factor)) / 2f;
            page = writer.getImportedPage(reader, i);
            cb.addTemplate(page, factor, 0, 0, factor, offsetX, offsetY);
        }
        document.close();
    }
