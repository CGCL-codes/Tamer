    protected static Object[] calcGainRatios(InstanceList ilist, int[] instIndices, int minNumInsts) {
        int numInsts = instIndices.length;
        Alphabet dataDict = ilist.getDataAlphabet();
        LabelAlphabet targetDict = (LabelAlphabet) ilist.getTargetAlphabet();
        double[] targetCounts = new double[targetDict.size()];
        for (int ii = 0; ii < numInsts; ii++) {
            Instance inst = ilist.get(instIndices[ii]);
            Labeling labeling = inst.getLabeling();
            double labelWeightSum = 0;
            for (int ll = 0; ll < labeling.numLocations(); ll++) {
                int li = labeling.indexAtLocation(ll);
                double labelWeight = labeling.valueAtLocation(ll);
                labelWeightSum += labelWeight;
                targetCounts[li] += labelWeight;
            }
            assert (Maths.almostEquals(labelWeightSum, 1));
        }
        double[] targetDistribution = new double[targetDict.size()];
        double baseEntropy = 0;
        for (int ci = 0; ci < targetDict.size(); ci++) {
            double p = targetCounts[ci] / numInsts;
            targetDistribution[ci] = p;
            if (p > 0) baseEntropy -= p * Math.log(p) / log2;
        }
        LabelVector baseLabelDistribution = new LabelVector(targetDict, targetDistribution);
        double infoGainSum = 0;
        int totalNumSplitPoints = 0;
        double[] passTestTargetCounts = new double[targetDict.size()];
        Hashtable[] featureToInfo = new Hashtable[dataDict.size()];
        for (int fi = 0; fi < dataDict.size(); fi++) {
            if ((fi + 1) % 1000 == 0) logger.info("at feature " + (fi + 1) + " / " + dataDict.size());
            featureToInfo[fi] = new Hashtable();
            Arrays.fill(passTestTargetCounts, 0);
            instIndices = sortInstances(ilist, instIndices, fi);
            for (int ii = 0; ii < numInsts - 1; ii++) {
                Instance inst = ilist.get(instIndices[ii]);
                Instance instPlusOne = ilist.get(instIndices[ii + 1]);
                FeatureVector fv1 = (FeatureVector) inst.getData();
                FeatureVector fv2 = (FeatureVector) instPlusOne.getData();
                double lower = fv1.value(fi);
                double higher = fv2.value(fi);
                Labeling labeling = inst.getLabeling();
                for (int ll = 0; ll < labeling.numLocations(); ll++) {
                    int li = labeling.indexAtLocation(ll);
                    double labelWeight = labeling.valueAtLocation(ll);
                    passTestTargetCounts[li] += labelWeight;
                }
                if (Maths.almostEquals(lower, higher) || inst.getLabeling().toString().equals(instPlusOne.getLabeling().toString())) continue;
                totalNumSplitPoints++;
                double splitPoint = (lower + higher) / 2;
                double numPassInsts = ii + 1;
                double numFailInsts = numInsts - numPassInsts;
                if (numPassInsts < minNumInsts || numFailInsts < minNumInsts) continue;
                double passProportion = numPassInsts / numInsts;
                if (Maths.almostEquals(passProportion, 0) || Maths.almostEquals(passProportion, 1)) continue;
                double passEntropy = 0;
                double failEntropy = 0;
                double p;
                for (int ci = 0; ci < targetDict.size(); ci++) {
                    if (numPassInsts > 0) {
                        p = passTestTargetCounts[ci] / numPassInsts;
                        if (p > 0) passEntropy -= p * Math.log(p) / log2;
                    }
                    if (numFailInsts > 0) {
                        double failTestTargetCount = targetCounts[ci] - passTestTargetCounts[ci];
                        p = failTestTargetCount / numFailInsts;
                        if (p > 0) failEntropy -= p * Math.log(p) / log2;
                    }
                }
                double gainDT = baseEntropy - passProportion * passEntropy - (1 - passProportion) * failEntropy;
                infoGainSum += gainDT;
                double splitDT = -passProportion * Math.log(passProportion) / log2 - (1 - passProportion) * Math.log(1 - passProportion) / log2;
                double gainRatio = gainDT / splitDT;
                featureToInfo[fi].put(new Double(splitPoint), new Point2D.Double(gainDT, gainRatio));
            }
        }
        double[] gainRatios = new double[dataDict.size()];
        double[] splitPoints = new double[dataDict.size()];
        int numSplitsForBestFeature = 0;
        if (totalNumSplitPoints == 0 || Maths.almostEquals(infoGainSum, 0)) return new Object[] { gainRatios, splitPoints, new Double(baseEntropy), baseLabelDistribution, new Integer(numSplitsForBestFeature) };
        double avgInfoGain = infoGainSum / totalNumSplitPoints;
        double maxGainRatio = 0;
        double gainForMaxGainRatio = 0;
        int xxx = 0;
        for (int fi = 0; fi < dataDict.size(); fi++) {
            double featureMaxGainRatio = 0;
            double featureGainForMaxGainRatio = 0;
            double bestSplitPoint = Double.NaN;
            for (Iterator iter = featureToInfo[fi].keySet().iterator(); iter.hasNext(); ) {
                Object key = iter.next();
                Point2D.Double pt = (Point2D.Double) featureToInfo[fi].get(key);
                double splitPoint = ((Double) key).doubleValue();
                double infoGain = pt.getX();
                double gainRatio = pt.getY();
                if (infoGain >= avgInfoGain) {
                    if (gainRatio > featureMaxGainRatio || (gainRatio == featureMaxGainRatio && infoGain > featureGainForMaxGainRatio)) {
                        featureMaxGainRatio = gainRatio;
                        featureGainForMaxGainRatio = infoGain;
                        bestSplitPoint = splitPoint;
                    }
                } else xxx++;
            }
            assert (bestSplitPoint != Double.NaN);
            gainRatios[fi] = featureMaxGainRatio;
            splitPoints[fi] = bestSplitPoint;
            if (featureMaxGainRatio > maxGainRatio || (featureMaxGainRatio == maxGainRatio && featureGainForMaxGainRatio > gainForMaxGainRatio)) {
                maxGainRatio = featureMaxGainRatio;
                gainForMaxGainRatio = featureGainForMaxGainRatio;
                numSplitsForBestFeature = featureToInfo[fi].size();
            }
        }
        logger.info("label distrib:\n" + baseLabelDistribution);
        logger.info("base entropy=" + baseEntropy + ", info gain sum=" + infoGainSum + ", total num split points=" + totalNumSplitPoints + ", avg info gain=" + avgInfoGain + ", num splits with < avg gain=" + xxx);
        return new Object[] { gainRatios, splitPoints, new Double(baseEntropy), baseLabelDistribution, new Integer(numSplitsForBestFeature) };
    }
