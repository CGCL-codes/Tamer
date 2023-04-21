    public int performDTW(SignatureDataIR signatureDataStored, SignatureDataIR signatureDataTest) {
        double[][] distanceMatrix;
        final LinkedList<Double> xStoredData = signatureDataStored.getX();
        final LinkedList<Double> yStoredData = signatureDataStored.getY();
        int numStoredData = xStoredData.size();
        final LinkedList<Double> xTestData = signatureDataTest.getX();
        final LinkedList<Double> yTestData = signatureDataTest.getY();
        int numTestData = xTestData.size();
        DTWMatrix = null;
        distanceMatrix = new double[numStoredData][numTestData];
        DTWMatrix = new double[numStoredData][numTestData];
        for (int i = 0; i < numStoredData; i++) {
            for (int j = 0; j < numTestData; j++) {
                double u = Math.sqrt((xStoredData.get(i) * xStoredData.get(i)) + (yStoredData.get(i) * yStoredData.get(i)));
                double v = Math.sqrt((xTestData.get(j) * xTestData.get(j)) + (yTestData.get(j) * yTestData.get(j)));
                distanceMatrix[i][j] = (u - v) * (u - v);
            }
        }
        DTWMatrix[0][0] = distanceMatrix[0][0];
        for (int i = 1; i < numStoredData; i++) {
            DTWMatrix[i][0] = distanceMatrix[i][0] + DTWMatrix[i - 1][0];
        }
        for (int i = 1; i < numTestData; i++) {
            DTWMatrix[0][i] = distanceMatrix[0][i] + DTWMatrix[0][i - 1];
        }
        for (int i = 1; i < numStoredData; i++) {
            for (int j = 1; j < numTestData; j++) {
                DTWMatrix[i][j] = distanceMatrix[i][j] + Math.min(DTWMatrix[i - 1][j], Math.min(DTWMatrix[i - 1][j - 1], DTWMatrix[i][j - 1]));
            }
        }
        warpLength = computePath(numStoredData, numTestData);
        numStoredData = timeWarpSeries(xStoredData, yStoredData, 0);
        timeWarpSeries(xTestData, yTestData, 1);
        return numStoredData;
    }
