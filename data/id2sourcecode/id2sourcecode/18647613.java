    public CFF(FontFile2 currentFontFile) {
        LogWriter.writeMethod("{readCFFTable}", 0);
        int startPointer = currentFontFile.selectTable(FontFile2.CFF);
        if (startPointer != 0) {
            int length = currentFontFile.getTableSize(FontFile2.CFF);
            byte[] data = currentFontFile.readBytes(startPointer, length);
            try {
                cffData = new Type1C(data, glyphs);
                hasCFFdata = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
