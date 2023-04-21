    public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception {
        LogWriter.writeMethod("{readFontType0 " + fontID + "}", 0);
        Map fontDescriptor = null;
        fontTypes = StandardFonts.CIDTYPE2;
        this.fontID = fontID;
        fontDescriptor = createCIDFont(values, descFontValues);
        if (fontDescriptor != null) {
            String fontFileRef = (String) fontDescriptor.get("FontFile2");
            if (fontFileRef != null) {
                if (renderPage) readEmbeddedFont(currentPdfFile.readStream(fontFileRef, true), hasEncoding, false);
            }
        }
        if ((renderPage) && (!isFontEmbedded) && (this.substituteFontFile != null)) {
            this.substituteFontUsed(substituteFontFile, substituteFontName);
            isFontSubstituted = true;
            this.isFontEmbedded = true;
        }
        if (renderPage) setFont(getBaseFontName(), 1);
        if (!isFontEmbedded) selectDefaultFont();
        return fontDescriptor;
    }
