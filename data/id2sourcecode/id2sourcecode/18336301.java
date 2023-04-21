    public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception {
        LogWriter.writeMethod("{readType1Font}" + values, 0);
        fontTypes = StandardFonts.TYPE1;
        Map fontDescriptor = super.createFont(values, fontID, renderPage, descFontValues, objectStore);
        if (fontDescriptor != null) readEmbeddedFont(values, fontDescriptor);
        readWidths(values);
        if (embeddedFontName != null && is1C()) {
            if (cleanupFonts || PdfStreamDecoder.runningStoryPad) {
                embeddedFontName = cleanupFontName(embeddedFontName);
                this.setBaseFontName(embeddedFontName);
                this.setFontName(embeddedFontName);
            }
        }
        if (renderPage) setFont(getBaseFontName(), 1);
        return fontDescriptor;
    }
