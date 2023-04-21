    public void useSlidingWindow() {
        Vector<AAIndexParameterResult> lTemp = new Vector<AAIndexParameterResult>();
        int lSlidingWindowStart = 0;
        if (iSlidingWindowSize % 2 == 0) {
            lSlidingWindowStart = iSlidingWindowSize / 2;
        } else {
            lSlidingWindowStart = (iSlidingWindowSize + 1) / 2;
        }
        for (int i = 0; i < iAaParameterResults.size(); i++) {
            double lTempCalMean = 0.0;
            double lTempRefMean = 0.0;
            double lTempSD = 0.0;
            double lTwoTempCalMean = 0.0;
            double lTwoTempRefMean = 0.0;
            double lTwoTempSD = 0.0;
            int lNumberOfSummedResults = 0;
            for (int j = 1; j <= iSlidingWindowSize; j++) {
                int lResultPosition = i - (lSlidingWindowStart - j);
                if (lResultPosition >= 0 && lResultPosition <= iAaParameterResults.size() - 1) {
                    AAIndexParameterResult lResult = iAaParameterResults.get(lResultPosition);
                    lTempCalMean = lTempCalMean + lResult.getCalulatedMean();
                    lTempRefMean = lTempRefMean + lResult.getNegativeSetMean();
                    lTempSD = lTempSD + lResult.getStandardDeviation();
                    if (lUseTwoSets) {
                        lTwoTempCalMean = lTwoTempCalMean + lResult.getCalulatedMeanSetTwo();
                        lTwoTempRefMean = lTwoTempRefMean + lResult.getNegativeSetMean();
                        lTwoTempSD = lTwoTempSD + lResult.getStandardDeviation();
                    }
                    lNumberOfSummedResults = lNumberOfSummedResults + 1;
                }
            }
            AAIndexParameterResult lResult = new AAIndexParameterResult(lTempCalMean / (double) lNumberOfSummedResults, lTempRefMean / (double) lNumberOfSummedResults, lTempSD / (double) lNumberOfSummedResults, i);
            if (lUseTwoSets) {
                lResult.setCalulatedMeanSetTwo(lTwoTempCalMean / (double) lNumberOfSummedResults);
            }
            lTemp.add(lResult);
        }
        iAaParameterResults = lTemp;
    }
