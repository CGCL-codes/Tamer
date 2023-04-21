    public final float getPairSimilarities(final String pText1, final String pText2) {
        double intersection = 0;
        double union;
        String cleanedText1 = pText1;
        String cleanedText2 = pText2;
        char[] pairsArray1 = getPairs(cleanedText1);
        int pairsArray1Size = pairsArray1.length;
        char[] pairsArray2 = getPairs(cleanedText2);
        int pairsArray2Size = pairsArray2.length;
        union = (pairsArray1Size + pairsArray2Size) / 2;
        if (union == 0) {
            union = 1;
        }
        if (isLoggingDebug()) {
            logDebug("Pair Array 1: " + new String(pairsArray1));
            logDebug("Pair Array 2: " + new String(pairsArray2));
        }
        for (int i = 0; i < pairsArray1.length - 1; i += 2) {
            char firstCharForArray1 = pairsArray1[i];
            char nextCharForArray1 = pairsArray1[i + 1];
            if (isLoggingDebug()) {
                logDebug("firstCharForArray1 = " + firstCharForArray1 + "; nextCharForArray1 = " + nextCharForArray1);
            }
            for (int j = 0; j < pairsArray2.length - 1; j += 2) {
                char firstCharForArray2 = pairsArray2[j];
                char nextCharForArray2 = pairsArray2[j + 1];
                if (isLoggingDebug()) {
                    logDebug("\tfirstCharForArray2 = " + firstCharForArray2 + "; nextCharForArray2 = " + nextCharForArray2);
                }
                if (firstCharForArray1 == firstCharForArray2 && nextCharForArray1 == nextCharForArray2 && firstCharForArray2 != 0xFF && nextCharForArray2 != 0xFF) {
                    intersection++;
                    pairsArray2[j] = 0xFF;
                    pairsArray2[j + 1] = 0xFF;
                    if (isLoggingDebug()) {
                        logDebug("Found\n");
                    }
                    break;
                }
            }
        }
        float similarity = (float) intersection;
        if (isLoggingDebug()) {
            logDebug("Union: " + union);
            logDebug("Intersection: " + intersection);
            logDebug("Similarity: " + similarity);
        }
        return similarity;
    }
