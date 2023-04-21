    public Hmtx(FontFile2 currentFontFile, int glyphCount, int metricsCount, int maxAdvance) {
        scaling = (float) maxAdvance;
        LogWriter.writeMethod("{readHmtxTable}", 0);
        if (metricsCount < 0) metricsCount = -metricsCount;
        int startPointer = currentFontFile.selectTable(FontFile2.HMTX);
        int lsbCount = glyphCount - metricsCount;
        hMetrics = new int[glyphCount];
        leftSideBearing = new short[glyphCount];
        int currentMetric = 0;
        if (startPointer == 0) LogWriter.writeLog("No Htmx table found"); else if (lsbCount < 0) {
            LogWriter.writeLog("Invalid Htmx table found");
        } else {
            int i = 0;
            for (i = 0; i < metricsCount; i++) {
                currentMetric = currentFontFile.getNextUint16();
                hMetrics[i] = currentMetric;
                leftSideBearing[i] = currentFontFile.getNextInt16();
            }
            int tableLength = currentFontFile.getTableSize(FontFile2.HMTX);
            int lsbBytes = tableLength - (i * 4);
            lsbCount = (lsbBytes / 2);
            for (int j = i; j < lsbCount; j++) {
                hMetrics[j] = currentMetric;
                leftSideBearing[j] = currentFontFile.getFWord();
            }
        }
    }
