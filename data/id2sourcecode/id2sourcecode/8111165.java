    public void readFontData(byte[] fontData) {
        LogWriter.writeMethod("{readFontData}", 0);
        fontTypes = glyphs.readEmbeddedFont(TTstreamisCID, fontData);
    }
