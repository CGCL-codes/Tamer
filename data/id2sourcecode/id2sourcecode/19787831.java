    public GenericStreamDecoder(DynamicVectorRenderer dvr) {
        this.current = dvr;
        try {
            String fontMaps = System.getProperty("org.jpedal.fontmaps");
            if (fontMaps != null) {
                StringTokenizer fontPaths = new StringTokenizer(fontMaps, ",");
                while (fontPaths.hasMoreTokens()) {
                    String fontPath = fontPaths.nextToken();
                    StringTokenizer values = new StringTokenizer(fontPath, "=:");
                    int count = values.countTokens() - 1;
                    String nameInPDF[] = new String[count];
                    String key = values.nextToken();
                    for (int i = 0; i < count; i++) nameInPDF[i] = values.nextToken();
                    setSubstitutedFontAliases(key, nameInPDF);
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("Unable to read FontMaps " + e.getMessage());
        }
        try {
            String fontDirs = System.getProperty("org.jpedal.fontdirs");
            if (fontDirs == null) addFontDirs("/Library/Fonts/,C:/win/fonts/,C:/WINDOWS/fonts/,/usr/X11R6/lib/X11/fonts/truetype/"); else addFontDirs(fontDirs);
        } catch (Exception e) {
            LogWriter.writeLog("Unable to read fontDirs " + e.getMessage());
        }
    }
