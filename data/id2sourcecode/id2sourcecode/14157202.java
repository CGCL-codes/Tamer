    public Hhea(FontFile2 currentFontFile) {
        LogWriter.writeMethod("{readHheaTable}", 0);
        int startPointer = currentFontFile.selectTable(FontFile2.HHEA);
        if (startPointer != 0) {
            version = currentFontFile.getNextUint32();
            ascender = currentFontFile.getFWord();
            descender = currentFontFile.getFWord();
            lineGap = currentFontFile.getFWord();
            advanceWidthMax = currentFontFile.readUFWord();
            minLeftSideBearing = currentFontFile.getFWord();
            minRightSideBearing = currentFontFile.getFWord();
            xMaxExtent = currentFontFile.getFWord();
            caretSlopeRise = currentFontFile.getNextInt16();
            caretSlopeRun = currentFontFile.getNextInt16();
            caretOffset = currentFontFile.getFWord();
            for (int i = 0; i < 4; i++) currentFontFile.getNextUint16();
            metricDataFormat = currentFontFile.getNextInt16();
            numberOfHMetrics = currentFontFile.getNextUint16();
        }
    }
