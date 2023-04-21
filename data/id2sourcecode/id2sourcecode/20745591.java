    public final Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception {
        LogWriter.writeMethod("{readType3Font}" + values, 0);
        fontTypes = StandardFonts.TYPE3;
        Map fontDescriptor = super.createFont(values, fontID, renderPage, descFontValues, objectStore);
        readWidths(values);
        readEmbeddedFont(values, objectStore);
        if (renderPage) setFont(getBaseFontName(), 1);
        return fontDescriptor;
    }
