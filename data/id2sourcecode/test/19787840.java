    private PdfFont readFont(String fontName) {
        LogWriter.writeMethod("{readFonts}", 0);
        String subFont = null;
        PdfFont currentFontData = null;
        if (debug) System.out.println("Font name=" + fontName);
        if ((fontSubstitutionTable != null)) subFont = (String) fontSubstitutionLocation.get(fontName);
        if (debug) System.out.println("subfont=" + subFont);
        try {
            currentFontData = new TrueType(subFont);
            currentFontData.createFont(fontName);
        } catch (Exception e) {
            LogWriter.writeLog("[PDF] Problem " + e + " reading Font  type " + subFont);
            addPageFailureMessage("Problem " + e + " reading Font  type " + subFont);
        }
        return currentFontData;
    }
