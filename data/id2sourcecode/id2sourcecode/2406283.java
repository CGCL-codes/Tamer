    public static void print(Layout layout, OutputStream out) {
        Document document = new Document();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, out);
        } catch (Exception e) {
            err().report(ErrorLevel.Warning, Voc.Error_CouldNotSaveDocument);
        }
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        AWTPageLayoutRenderer renderer = AWTPageLayoutRenderer.getInstance();
        It<Page> pages = it(layout.pages);
        for (Page page : pages) {
            Size2f pageSize = page.format.size;
            float width = Units.mmToPx(pageSize.width, 1);
            float height = Units.mmToPx(pageSize.height, 1);
            document.newPage();
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
            Log.message("Printing page " + pages.getIndex() + "...");
            renderer.paint(layout, pages.getIndex(), new AWTGraphicsContext(g2d, 1f, RenderingFormat.Vector));
            g2d.dispose();
            cb.addTemplate(tp, 0, 0);
        }
        document.close();
    }
