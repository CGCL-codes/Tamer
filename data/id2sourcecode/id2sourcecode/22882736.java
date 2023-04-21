    protected static void loadStandardFontWidth(String fontName) {
        Integer fileNumber = (Integer) standardFileList.get(fontName);
        if ((fileNumber != null) && (standardFontLoaded.get(fileNumber) == null)) {
            standardFontLoaded.put(fileNumber, "x");
            try {
                loadStandardFont(fileNumber.intValue());
            } catch (Exception e) {
                LogWriter.writeLog("[PDF] " + e + " problem reading lookup table for pdf font " + fontName + " " + fontName);
            }
        }
    }
