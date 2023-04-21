    public Sequence bestSequence(Sequence s, int T) {
        try {
            HashMap<Integer, double[][]> konfidenzValues = new HashMap<Integer, double[][]>();
            delta = new double[s.size()][T];
            psi = new int[s.size()][T];
            for (int i = 0; i < T; i++) {
                delta[0][i] = 1.0;
                psi[0][i] = 0;
            }
            int setIndex = -1;
            int bestLastIndex = -1;
            double bestLastDelta = -1.0;
            double best = 0.0;
            for (int t = 0; t < s.size(); t++) {
                DenseDoubleMatrix2D matrix2D = new DenseDoubleMatrix2D(T, T);
                DenseDoubleMatrix1D matrix1D = new DenseDoubleMatrix1D(T);
                if (likelihood == null) {
                    likelihood = new LBFGSLogLikelihood();
                }
                likelihood.computeMatrices(fSet, s, t, fSet.getLambda(), matrix2D, matrix1D);
                double[][] cValues = new double[T + 1][T + 1];
                for (int j = 0; j < T; j++) {
                    setIndex = -1;
                    best = 0.0;
                    double tempBest;
                    tempBest = matrix1D.getQuick(j);
                    cValues[T][j] = tempBest;
                    for (int i = 0; i < T; i++) {
                        double bestPlus = 1.0;
                        if (t > 0) {
                            bestPlus = delta[t - 1][i] * matrix2D.getQuick(i, j);
                            cValues[i][j] = matrix2D.getQuick(i, j);
                        }
                        if (tempBest * bestPlus > best) {
                            best = tempBest * bestPlus;
                            setIndex = i;
                        }
                    }
                    delta[t][j] = matrix1D.getQuick(j);
                    if (t > 0) {
                        delta[t][j] *= delta[t - 1][setIndex] * matrix2D.getQuick(setIndex, j);
                    }
                    if (t == s.size() - 1) {
                        if (delta[t][j] > bestLastDelta) {
                            bestLastDelta = delta[t][j];
                            bestLastIndex = j;
                        }
                    }
                    psi[t][j] = setIndex;
                }
                konfidenzValues.put(t, cValues.clone());
            }
            int[] prediction = new int[s.size()];
            String[] predLabel = new String[s.size()];
            prediction[s.size() - 1] = bestLastIndex;
            predLabel[s.size() - 1] = fSet.getMapping().getIntLabel(prediction[s.size() - 1]);
            for (int t = s.size() - 2; t >= 0; t--) {
                prediction[t] = psi[t + 1][prediction[t + 1]];
                predLabel[t] = fSet.getMapping().getIntLabel(prediction[t]);
            }
            s.setY(predLabel);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
