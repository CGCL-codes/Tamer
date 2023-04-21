    void finalizePeak() {
        while (!mzPeakMap.isEmpty()) {
            int scanNumber = mzPeakMap.firstKey();
            if (mzPeakMap.get(scanNumber).getIntensity() > 0) break;
            mzPeakMap.remove(scanNumber);
        }
        while (!mzPeakMap.isEmpty()) {
            int scanNumber = mzPeakMap.lastKey();
            if (mzPeakMap.get(scanNumber).getIntensity() > 0) break;
            mzPeakMap.remove(scanNumber);
        }
        if (mzPeakMap.isEmpty()) {
            throw (new IllegalStateException("Peak can not be finalized without any data points"));
        }
        int allScanNumbers[] = CollectionUtils.toIntArray(mzPeakMap.keySet());
        for (int i = 0; i < allScanNumbers.length; i++) {
            DataPoint dataPoint = mzPeakMap.get(allScanNumbers[i]);
            double rt = dataFile.getScan(allScanNumbers[i]).getRetentionTime();
            if (dataPoint.getIntensity() > height) {
                height = dataPoint.getIntensity();
                representativeScan = allScanNumbers[i];
                this.rt = rt;
            }
        }
        area = 0;
        for (int i = 1; i < allScanNumbers.length; i++) {
            double previousRT = dataFile.getScan(allScanNumbers[i - 1]).getRetentionTime() * 60d;
            double currentRT = dataFile.getScan(allScanNumbers[i]).getRetentionTime() * 60d;
            double rtDifference = currentRT - previousRT;
            double previousIntensity = mzPeakMap.get(allScanNumbers[i - 1]).getIntensity();
            double thisIntensity = mzPeakMap.get(allScanNumbers[i]).getIntensity();
            double averageIntensity = (previousIntensity + thisIntensity) / 2;
            area += (rtDifference * averageIntensity);
        }
        double mzArray[] = new double[allScanNumbers.length];
        for (int i = 0; i < allScanNumbers.length; i++) {
            mzArray[i] = mzPeakMap.get(allScanNumbers[i]).getMZ();
        }
        this.mz = MathUtils.calcQuantile(mzArray, 0.5f);
        fragmentScan = ScanUtils.findBestFragmentScan(dataFile, rtRange, mzRange);
        if (fragmentScan > 0) {
            Scan fragmentScanObject = dataFile.getScan(fragmentScan);
            int precursorCharge = fragmentScanObject.getPrecursorCharge();
            if ((precursorCharge > 0) && (this.charge == 0)) this.charge = precursorCharge;
        }
    }
