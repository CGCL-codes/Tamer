    public byte[] brand(byte[] input, String text) {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            int pages = 1;
            float x1 = 30f;
            float x2 = 280f;
            float x3 = 320f;
            float x4 = 565f;
            float[] y1 = new float[pages];
            float[] y2 = new float[pages];
            float height = (778f - (20f * (pages - 1))) / pages;
            y1[0] = 812f;
            y2[0] = 812f - height;
            for (int i = 1; i < pages; i++) {
                y1[i] = y2[i - 1] - 20f;
                y2[i] = y1[i] - height;
            }
            PdfReader reader = new PdfReader(input);
            int n = reader.getNumberOfPages();
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, output);
            HeaderFooter footer = new HeaderFooter(new Phrase(text), false);
            footer.setBorder(Rectangle.NO_BORDER);
            document.setFooter(footer);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            int rotation;
            int i = 0;
            int p = 0;
            while (i < n) {
                i++;
                Rectangle rect = reader.getPageSizeWithRotation(i);
                float factorx = (x2 - x1) / rect.width();
                float factory = (y1[p] - y2[p]) / rect.height();
                float factor = (factorx < factory ? factorx : factory);
                float dx = (factorx == factor ? 0f : ((x2 - x1) - rect.width() * factor) / 2f);
                float dy = (factory == factor ? 0f : ((y1[p] - y2[p]) - rect.height() * factor) / 2f);
                page = writer.getImportedPage(reader, i);
                rotation = reader.getPageRotation(i);
                if (rotation == 90 || rotation == 270) {
                    cb.addTemplate(page, 0, -factor, factor, 0, x1 + dx, y2[p] + dy + rect.height() * factor);
                } else {
                    factor = 9 / 10;
                    cb.addTemplate(page, 1, 0, 0, 1, dx, 50);
                }
                p++;
                if (p == pages) {
                    p = 0;
                    document.newPage();
                }
            }
            document.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return output.toByteArray();
    }
