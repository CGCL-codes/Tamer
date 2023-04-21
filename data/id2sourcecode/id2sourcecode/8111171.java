    protected final void readEmbeddedFont(byte[] font_data, boolean hasEncoding, boolean isSubstituted) {
        LogWriter.writeMethod("{readEmbeddedFont}", 0);
        try {
            LogWriter.writeLog("Embedded TrueType font used");
            readFontData(font_data);
            isFontEmbedded = true;
            glyphs.setEncodingToUse(hasEncoding, this.getFontEncoding(false), isSubstituted, TTstreamisCID);
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " processing TrueType font");
            e.printStackTrace();
        }
    }
