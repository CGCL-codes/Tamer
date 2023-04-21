    public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception {
        LogWriter.writeMethod("{readNonCIDFont}" + values + "{render=" + renderPage, 0);
        if ((PdfJavaGlyphs.fontList == null) & (renderPage)) {
            PdfJavaGlyphs.fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            int count = PdfJavaGlyphs.fontList.length;
            for (int i = 0; i < count; i++) PdfJavaGlyphs.fontList[i] = PdfJavaGlyphs.fontList[i].toLowerCase();
        }
        this.fontID = fontID;
        this.renderPage = renderPage;
        readGenericFontMetadata(values);
        String fontType = (String) values.get("Subtype");
        if (fontType == null) fontType = "";
        Object toUnicode = values.get("ToUnicode");
        if (toUnicode != null) {
            if (toUnicode instanceof String) this.readUnicode(currentPdfFile.readStream((String) toUnicode, true), fontID); else this.readUnicode((byte[]) ((Map) toUnicode).get("DecodedStream"), fontID);
        }
        String firstChar = (String) values.get("FirstChar");
        int firstCharNumber = 1;
        if (firstChar != null) firstCharNumber = ToInteger.getInteger(firstChar);
        String lastChar = (String) values.get("LastChar");
        float shortestWidth = 0;
        int count = 0;
        String width_value = currentPdfFile.getValue((String) values.get("Widths"));
        if (width_value != null) {
            widthTable = new float[maxCharCount];
            for (int ii = 0; ii < maxCharCount; ii++) widthTable[ii] = -1;
            String rawWidths = width_value.substring(1, width_value.length() - 1).trim();
            StringTokenizer widthValues = new StringTokenizer(rawWidths);
            int lastCharNumber = ToInteger.getInteger(lastChar) + 1;
            float widthValue;
            float ratio = (float) (1f / FontMatrix[0]);
            ratio = -ratio;
            for (int i = firstCharNumber; i < lastCharNumber; i++) {
                if (widthValues.hasMoreTokens() == false) widthValue = 0; else if (fontTypes == StandardFonts.TYPE3) widthValue = Float.parseFloat(widthValues.nextToken()) / (ratio); else widthValue = Float.parseFloat(widthValues.nextToken()) * xscale;
                widthTable[i] = widthValue;
                if ((widthValue > 0)) {
                    shortestWidth = shortestWidth + widthValue;
                    count++;
                }
            }
        }
        Object encValue = values.get("Encoding");
        if (encValue != null) {
            if (encValue instanceof String) handleFontEncoding((String) encValue, null, fontType); else handleFontEncoding("", (Map) encValue, fontType);
        } else {
            handleNoEncoding(0);
        }
        if (count > 0) possibleSpaceWidth = shortestWidth / (2 * count);
        Map fontDescriptor = null;
        Object rawFont = values.get("FontDescriptor");
        if (rawFont instanceof String) {
            String fontDescriptorRef = (String) rawFont;
            if (fontDescriptorRef != null && fontDescriptorRef.length() > 1) fontDescriptor = currentPdfFile.readObject(fontDescriptorRef, false, null);
        } else fontDescriptor = (Map) rawFont;
        if (fontDescriptor != null) {
            int flags = 0;
            String value = (String) fontDescriptor.get("Flags");
            if (value != null) flags = Integer.parseInt(value);
            fontFlag = flags;
            glyphs.remapFont = false;
            int flag = fontFlag;
            if ((flag & 4) == 4) glyphs.remapFont = true;
            value = currentPdfFile.getValue((String) fontDescriptor.get("MissingWidth"));
            if (value != null) missingWidth = Float.parseFloat(value);
        }
        return fontDescriptor;
    }
