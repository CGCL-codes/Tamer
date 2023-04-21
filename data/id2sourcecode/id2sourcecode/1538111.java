    public void readWidths(Map values) throws Exception {
        LogWriter.writeMethod("{readWidths}" + values, 0);
        String firstChar = currentPdfFile.getValue((String) values.get("FirstChar"));
        int firstCharNumber = 1;
        if (firstChar != null) firstCharNumber = ToInteger.getInteger(firstChar);
        String lastChar = currentPdfFile.getValue((String) values.get("LastChar"));
        float shortestWidth = 0;
        String width_value = currentPdfFile.getValue((String) values.get("Widths"));
        if (width_value != null) {
            widthTable = new float[maxCharCount];
            for (int ii = 0; ii < maxCharCount; ii++) widthTable[ii] = -1;
            String rawWidths = width_value.substring(1, width_value.length() - 1).trim();
            StringTokenizer widthValues = new StringTokenizer(rawWidths);
            int lastCharNumber = ToInteger.getInteger(lastChar);
            float widthValue;
            float ratio = (float) (1f / FontMatrix[0]);
            if (ratio < 0) ratio = -ratio;
            for (int i = firstCharNumber; i < lastCharNumber + 1; i++) {
                if (!widthValues.hasMoreTokens()) {
                    widthValue = 0;
                } else {
                    if (fontTypes == StandardFonts.TYPE3) widthValue = Float.parseFloat(widthValues.nextToken()) / (ratio); else widthValue = Float.parseFloat(widthValues.nextToken()) * xscale;
                    widthTable[i] = widthValue;
                    if ((widthValue > 0)) {
                        shortestWidth = shortestWidth + widthValue;
                    }
                }
            }
        }
    }
