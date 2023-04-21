    public void plotRawData() {
        GP.removeAllCurveData();
        int chStart = ((Integer) chStart_Spinner.getValue()).intValue();
        int chStep = ((Integer) chStep_Spinner.getValue()).intValue();
        int chStop = ((Integer) chStop_Spinner.getValue()).intValue();
        int posStart = ((Integer) posStart_Spinner.getValue()).intValue();
        int posStep = ((Integer) posStep_Spinner.getValue()).intValue();
        int posStop = ((Integer) posStop_Spinner.getValue()).intValue();
        int posHStart = ((Integer) posHStart_Spinner.getValue()).intValue();
        int posHStep = ((Integer) posHStep_Spinner.getValue()).intValue();
        int posHStop = ((Integer) posHStop_Spinner.getValue()).intValue();
        int chMax = rawData.getChannelsNumber();
        int posMax = rawData.getPositionsNumberSlit();
        int posHMax = rawData.getPositionsNumberHarp();
        if (chStart < 0 || chStart > chMax) {
            return;
        }
        if (posStart < 0 || posStart > posMax) {
            return;
        }
        if (posHStart < 0 || posHStart > posHMax) {
            return;
        }
        double max_val = 0.;
        for (int chInd = chStart; (chInd <= chMax && chInd <= chStop); chInd += chStep) {
            for (int posInd = posStart; (posInd <= posMax && posInd <= posStop); posInd += posStep) {
                for (int posHInd = posHStart; (posHInd <= posHMax && posHInd <= posHStop); posHInd += posHStep) {
                    double val = rawData.getMaxValue(posInd - 1, posHInd - 1, chInd - 1);
                    if (val > max_val) {
                        max_val = val;
                    }
                }
            }
        }
        int iniStoreSize = cdStoreV.size();
        for (int i = iniStoreSize; i < chMax * posMax * posHMax; i++) {
            cdStoreV.add(new CurveData());
        }
        int nSamples = rawData.getSamplesNumber();
        Vector cdV = new Vector();
        int index = 0;
        for (int chInd = chStart; (chInd <= chMax && chInd <= chStop); chInd += chStep) {
            for (int posInd = posStart; (posInd <= posMax && posInd <= posStop); posInd += posStep) {
                for (int posHInd = posHStart; (posHInd <= posHMax && posHInd <= posHStop); posHInd += posHStep) {
                    CurveData cd = (CurveData) cdStoreV.get((chInd - 1) + chMax * (posInd - 1) + chMax * posMax * (posHInd - 1));
                    cd.clear();
                    cd.setColor(IncrementalColors.getColor(index));
                    for (int is = 0; is < nSamples; is++) {
                        double value = rawData.getValue(is, posInd - 1, posHInd - 1, chInd - 1) / max_val;
                        cd.addPoint(is, value);
                    }
                    cdV.add(cd);
                    index++;
                }
            }
        }
        GP.addCurveData(cdV);
    }
