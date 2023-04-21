    public void writeTrailer() throws IOException {
        if (!isMultiPage()) closePage();
        PDFPageTree pages = os.openPageTree("RootPage", null);
        for (int i = 1; i <= currentPage; i++) {
            pages.addPage("Page" + i);
        }
        Dimension pageSize = PageConstants.getSize(getProperty(PAGE_SIZE), getProperty(ORIENTATION));
        pages.setMediaBox(0, 0, pageSize.getWidth(), pageSize.getHeight());
        pages.setResources("Resources");
        os.close(pages);
        os.object("PageProcSet", new Object[] { os.name("PDF"), os.name("Text"), os.name("ImageC") });
        int nFonts = fontTable.addFontDictionary();
        int nXObjects = delayImageQueue.addXObjects();
        int nPatterns = delayPaintQueue.addPatterns();
        if (extGStates.size() > 0) {
            PDFDictionary extGState = os.openDictionary("ExtGState");
            for (Iterator i = extGStates.keySet().iterator(); i.hasNext(); ) {
                Float alpha = (Float) i.next();
                String alphaName = (String) extGStates.get(alpha);
                PDFDictionary alphaDictionary = extGState.openDictionary(alphaName);
                alphaDictionary.entry("ca", alpha.floatValue());
                alphaDictionary.entry("CA", alpha.floatValue());
                alphaDictionary.entry("BM", os.name("Normal"));
                alphaDictionary.entry("AIS", false);
                extGState.close(alphaDictionary);
            }
            os.close(extGState);
        }
        PDFDictionary resources = os.openDictionary("Resources");
        resources.entry("ProcSet", os.ref("PageProcSet"));
        if (nFonts > 0) resources.entry("Font", os.ref("FontList"));
        if (nXObjects > 0) resources.entry("XObject", os.ref("XObjects"));
        if (nPatterns > 0) resources.entry("Pattern", os.ref("Pattern"));
        if (extGStates.size() > 0) resources.entry("ExtGState", os.ref("ExtGState"));
        os.close(resources);
        PDFOutlineList outlines = os.openOutlineList("Outlines", "Outline1", "Outline" + currentPage);
        os.close(outlines);
        for (int i = 1; i <= currentPage; i++) {
            String prev = i > 1 ? "Outline" + (i - 1) : null;
            String next = i < currentPage ? "Outline" + (i + 1) : null;
            PDFOutline outline = os.openOutline("Outline" + i, (String) titles.get(i - 1), "Outlines", prev, next);
            outline.setDest(new Object[] { os.ref("Page" + i), os.name("Fit") });
            os.close(outline);
        }
        processDelayed();
    }
