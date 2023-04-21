    private long findAWord_IdxInFileA(DictFileInfo dictFind, boolean bFind) {
        long lowWord;
        long highWord;
        long intTmp;
        long scanWordIndex;
        lowWord = lowIndexWord;
        highWord = highIndexWord;
        scanWordIndex = (lowWord + highWord) / 2;
        int FileNO = _FindFileNumByNO(dictFind, true, scanWordIndex, dictFind.wordFiles);
        if (FileNO != dictFind.CurWordFileNO) {
            dictFind.CurWordFileNO = FileNO;
            Integer i = new Integer(FileNO);
            String str = null;
            str = new String(dictFind.MainFileName + "w" + i.toString() + ".dat");
            dictFind.pfCurWord = str;
            if (fcWord != null) {
                fcWord.closeDict();
                fcWord = null;
            }
            fcWord = new DictFile();
            if (fcWord.openDict(str) == false) {
                return 0;
            }
        }
        setWordTmpBuffer();
        if (constValue.UNISTREND == dictConstInstance.DICT_WORDBuf[0]) {
            lowWord = 0;
            highWord = 1;
            scanWordIndex = 0;
        }
        if (!bFind) {
            SDGetWordByNO_B(scanWordIndex);
            intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
            while (lowWord < highWord - 1) {
                if (constValue.DICT_WORDSAME == intTmp) {
                    break;
                } else if (constValue.DICT_WORDASMALL == intTmp) {
                    highWord = scanWordIndex;
                    scanWordIndex = (lowWord + highWord) / 2;
                } else {
                    lowWord = scanWordIndex;
                    scanWordIndex = (lowWord + highWord) / 2;
                }
                SDGetWordByNO_B(scanWordIndex);
                intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
            }
        } else {
            intTmp = constValue.DICT_WORDSAME;
            scanWordIndex = lowIndexWord;
        }
        if (constValue.DICT_WORDSAME == intTmp) {
            highWord = scanWordIndex;
            while ((constValue.DICT_WORDSAME == intTmp) && (highWord != 0)) {
                highWord--;
                SDGetWordByNO_B(highWord);
                intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
                if (0 == highWord) {
                    break;
                }
            }
            intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
            if (constValue.DICT_WORDSAME != intTmp) {
                highWord++;
            }
            SDGetWordByNO_B(highWord);
            intTmp = appUtility.UNIStrByteCmp(dictConstInstance.DICT_WORDBuf, wordBytes);
            scanWordIndex = highWord;
            while (true) {
                lowWord = scanWordIndex;
                if (intTmp != 0) {
                    scanWordIndex++;
                } else {
                    break;
                }
                SDGetWordByNO_B(scanWordIndex);
                intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
                if (constValue.DICT_WORDSAME != intTmp || scanWordIndex == (dictFind.wordCount - 1)) {
                    scanWordIndex = highWord;
                    intTmp = constValue.DICT_WORDSAME;
                    break;
                }
                intTmp = appUtility.UNIStrByteCmp(dictConstInstance.DICT_WORDBuf, wordBytes);
            }
        }
        while ((scanWordIndex < (dictFind.wordCount - 1)) && (constValue.DICT_WORDABIG == intTmp)) {
            scanWordIndex++;
            SDGetWordByNO_B(scanWordIndex);
            intTmp = appUtility.compareWord(dictConstInstance.DICT_WORDBuf, wordBytes);
        }
        return scanWordIndex;
    }
