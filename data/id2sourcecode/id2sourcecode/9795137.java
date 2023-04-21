    public Instances transformData(MultiLabelInstances trainingData) throws Exception {
        classifierInstances = prepareClassifierInstances(trainingData);
        classifierInstances.insertAttributeAt(new Attribute("Threshold"), classifierInstances.numAttributes());
        classifierInstances.setClassIndex(classifierInstances.numAttributes() - 1);
        for (int k = 0; k < kFoldsCV; k++) {
            MultiLabelLearner tempLearner;
            MultiLabelInstances mlTest;
            if (kFoldsCV == 1) {
                tempLearner = baseLearner;
                mlTest = trainingData;
            } else {
                Instances train = trainingData.getDataSet().trainCV(kFoldsCV, k);
                Instances test = trainingData.getDataSet().testCV(kFoldsCV, k);
                MultiLabelInstances mlTrain = new MultiLabelInstances(train, trainingData.getLabelsMetaData());
                mlTest = new MultiLabelInstances(test, trainingData.getLabelsMetaData());
                tempLearner = foldLearner.makeCopy();
                tempLearner.build(mlTrain);
            }
            for (int instanceIndex = 0; instanceIndex < mlTest.getDataSet().numInstances(); instanceIndex++) {
                Instance instance = mlTest.getDataSet().instance(instanceIndex);
                double[] newValues = new double[classifierInstances.numAttributes()];
                valuesX(tempLearner, instance, newValues, metaDatasetChoice);
                boolean[] trueLabels = new boolean[numLabels];
                for (int i = 0; i < numLabels; i++) {
                    int labelIndice = labelIndices[i];
                    String classValue = mlTest.getDataSet().attribute(labelIndice).value((int) mlTest.getDataSet().instance(instanceIndex).value(labelIndice));
                    trueLabels[i] = classValue.equals("1");
                }
                MultiLabelOutput mlo = baseLearner.makePrediction(mlTest.getDataSet().instance(instanceIndex));
                double[] arrayOfScores = mlo.getConfidences();
                ArrayList<Double> list = new ArrayList();
                for (int i = 0; i < numLabels; i++) {
                    list.add(arrayOfScores[i]);
                }
                Collections.sort(list);
                double tempThresshold = 0, threshold = 0;
                double prev = list.get(0);
                int t = numLabels, tempT = 0;
                for (Double x : list) {
                    tempThresshold = (x + prev) / 2;
                    for (int i = 0; i < numLabels; i++) {
                        if ((trueLabels[i] == true) && (arrayOfScores[i] <= tempThresshold)) {
                            tempT++;
                        } else if ((trueLabels[i] == false) && (arrayOfScores[i] >= tempThresshold)) {
                            tempT++;
                        }
                    }
                    if (tempT < t) {
                        t = tempT;
                        threshold = tempThresshold;
                    }
                    tempT = 0;
                    prev = x;
                }
                newValues[newValues.length - 1] = threshold;
                Instance newInstance = DataUtils.createInstance(mlTest.getDataSet().instance(instanceIndex), mlTest.getDataSet().instance(instanceIndex).weight(), newValues);
                classifierInstances.add(newInstance);
            }
        }
        return classifierInstances;
    }
