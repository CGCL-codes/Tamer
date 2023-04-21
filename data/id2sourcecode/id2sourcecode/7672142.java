    private long getWordPosByIndex() {
        int lowWord = 0;
        int highWord = vIndexInfo.size();
        int scanWordIndex = (highWord + lowWord) / 2;
        int intTmp = 0;
        while (lowWord < highWord - 1) {
            indexInfo idx = null;
            idx = (indexInfo) vIndexInfo.elementAt(scanWordIndex);
            int wordLen = idx.endPos - idx.startPos;
            appUtility.memsetBytes(wordBytes);
            appUtility.byteStrCpy(wordBytes, 0, indexBuf, idx.startPos, wordLen);
            intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
            if (constValue.DICT_WORDSAME == intTmp) {
                return idx.wordNum;
            } else if (constValue.DICT_WORDASMALL == intTmp) {
                highWord = scanWordIndex;
                scanWordIndex = (lowWord + highWord) / 2;
            } else {
                lowWord = scanWordIndex;
                scanWordIndex = (lowWord + highWord) / 2;
            }
            idx = null;
        }
        indexInfo idx1 = null;
        idx1 = (indexInfo) vIndexInfo.elementAt(scanWordIndex);
        int wordLen = idx1.endPos - idx1.startPos;
        appUtility.memsetBytes(wordBytes);
        appUtility.byteStrCpy(wordBytes, 0, indexBuf, idx1.startPos, wordLen);
        intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
        if (constValue.DICT_WORDSAME == intTmp) {
            lowIndexWord = 0;
            indexInfo idx2 = (indexInfo) vIndexInfo.elementAt(scanWordIndex + 1);
            highIndexWord = idx2.wordNum;
            idx2 = null;
            return idx1.wordNum;
        } else if (constValue.DICT_WORDASMALL == intTmp) {
            if (scanWordIndex > 0) {
                highIndexWord = idx1.wordNum;
                indexInfo idx2 = null;
                idx2 = (indexInfo) vIndexInfo.elementAt(scanWordIndex - 1);
                lowIndexWord = idx2.wordNum;
                idx2 = null;
            } else {
                return -2;
            }
            return -1;
        } else {
            if (scanWordIndex < vIndexInfo.size() - 1) {
                lowIndexWord = idx1.wordNum;
                indexInfo idx2 = null;
                idx2 = (indexInfo) vIndexInfo.elementAt(scanWordIndex + 1);
                highIndexWord = idx2.wordNum;
                idx2 = null;
            } else {
                return -3;
            }
            return -1;
        }
    }
