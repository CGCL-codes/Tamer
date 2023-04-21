    public void exportPDF(IDrawingProvider plugInPort, File file) throws FileNotFoundException, DocumentException {
        Dimension d = plugInPort.getSize();
        float factor = 1f * PDF_RESOLUTION / SCREEN_RESOLUTION;
        float totalWidth = (float) (factor * (2 * margin + d.getWidth()));
        float totalHeight = (float) (factor * (2 * margin + d.getHeight()));
        Document document = new Document(new com.lowagie.text.Rectangle(totalWidth, totalHeight));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate template = contentByte.createTemplate(totalWidth, totalHeight);
        DefaultFontMapper mapper = new DefaultFontMapper();
        if (Utils.isWindows()) {
            mapper.insertDirectory(System.getenv("windir") + "\\Fonts");
        } else if (Utils.isMac()) {
            mapper.insertDirectory("$HOME/Library/Fonts");
        } else if (Utils.isUnix()) {
            mapper.insertDirectory("/usr/share/fonts/truetype/");
        }
        Graphics2D g2d = template.createGraphics((float) (factor * d.getWidth()), (float) (factor * d.getHeight()), mapper);
        g2d.scale(factor, factor);
        plugInPort.draw(g2d);
        g2d.dispose();
        contentByte.addTemplate(template, (float) margin, (float) margin);
        document.close();
    }
