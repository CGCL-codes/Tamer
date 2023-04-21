    public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception {
        LogWriter.writeMethod("{readCIDFONT0 " + fontID + "}", 0);
        fontTypes = StandardFonts.CIDTYPE0;
        this.fontID = fontID;
        Map fontDescriptor = createCIDFont(values, descFontValues);
        if (fontDescriptor != null) readEmbeddedFont(values, fontDescriptor);
        if ((renderPage) && (!isFontEmbedded) && (this.substituteFontFile != null)) {
            isFontSubstituted = true;
            subFont = new CIDFontType2(currentPdfFile, TTstreamisCID);
            subFont.substituteFontUsed(substituteFontFile, substituteFontName);
            this.isFontEmbedded = true;
        }
        if (!isFontEmbedded) selectDefaultFont();
        if (renderPage) setFont(getBaseFontName(), 1);
        return fontDescriptor;
    }
