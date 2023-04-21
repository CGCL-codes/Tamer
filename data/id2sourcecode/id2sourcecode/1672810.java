    private void addTexturePaint(Entry e) throws IOException {
        TexturePaint tp = (TexturePaint) e.paint;
        PDFStream pattern = pdf.openStream(e.name, null);
        pattern.entry("Type", pdf.name("Pattern"));
        pattern.entry("PatternType", 1);
        pattern.entry("PaintType", 1);
        BufferedImage image = tp.getImage();
        pattern.entry("TilingType", 1);
        double width = tp.getAnchorRect().getWidth();
        double height = tp.getAnchorRect().getHeight();
        double offsX = tp.getAnchorRect().getX();
        double offsY = tp.getAnchorRect().getY();
        pattern.entry("BBox", new double[] { 0, 0, width, height });
        pattern.entry("XStep", width);
        pattern.entry("YStep", height);
        PDFDictionary resources = pattern.openDictionary("Resources");
        resources.entry("ProcSet", new Object[] { pdf.name("PDF"), pdf.name("ImageC") });
        pattern.close(resources);
        setMatrix(pattern, e, offsX, offsY);
        pattern.matrix(width, 0, 0, -height, 0, height);
        pattern.inlineImage(image, null, e.writeAs);
        pdf.close(pattern);
    }
