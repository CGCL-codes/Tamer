    protected final void readGenericFontMetadata(Map values) {
        LogWriter.writeMethod("{readGenericFontMetadata " + fontID + "}", 0);
        String fontMatrix = (String) values.get("FontMatrix");
        if (fontMatrix != null) {
            StringTokenizer tokens = new StringTokenizer(fontMatrix, "[] ");
            for (int i = 0; i < 6; i++) {
                FontMatrix[i] = (Float.parseFloat(tokens.nextToken()));
            }
        }
        String fontBounding = (String) values.get("FontBBox");
        if (fontBounding != null) {
            StringTokenizer tokens = new StringTokenizer(fontBounding, "[] ");
            for (int i = 0; i < 4; i++) FontBBox[i] = Float.parseFloat(tokens.nextToken());
        }
        String baseFontName = currentPdfFile.getValue((String) values.get("BaseFont"));
        if (baseFontName == null) baseFontName = currentPdfFile.getValue((String) values.get("FontName"));
        if (baseFontName == null) baseFontName = fontID; else baseFontName = baseFontName.substring(1);
        if (cleanupFonts || PdfStreamDecoder.runningStoryPad) baseFontName = cleanupFontName(baseFontName);
        glyphs.fontName = baseFontName;
        int index = baseFontName.indexOf("+");
        if (index == 6) glyphs.fontName = baseFontName.substring(index + 1);
        glyphs.setBaseFontName(baseFontName);
    }
