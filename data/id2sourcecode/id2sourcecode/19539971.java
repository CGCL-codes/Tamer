    public void buildClassifier(InstanceSet instanceSet) throws Exception {
        this.instanceSet = instanceSet;
        Instance instance;
        numAttributes = instanceSet.numAttributes();
        numericOutput = instanceSet.getClassAttribute().isNumeric();
        float quadraticError = 0;
        float oldQuadraticError;
        long numWeightedInstances = instanceSet.numWeightedInstances();
        long numInstances = instanceSet.numInstances();
        attributeVector = instanceSet.getAttributes();
        classIndex = instanceSet.getClassIndex();
        numClasses = attributeVector[classIndex].numValues();
        highestValue = new float[numAttributes];
        lowestValue = new float[numAttributes];
        if (numericOutput) {
            Stats stats;
            stats = instanceSet.getAttributeStats(classIndex).getNumericStats();
            highestValue[classIndex] = stats.getMax();
            lowestValue[classIndex] = stats.getMin();
        }
        expectedOutput = new float[numClasses][0];
        if (numericOutput) {
            float[] output;
            output = attributeVector[classIndex].getDistinticNumericValues();
            for (int i = 0; i < numClasses; i++) {
                expectedOutput[i] = new float[1];
                expectedOutput[i][0] = activationFunction.normalizeToFunctionInterval(output[i], highestValue[classIndex], lowestValue[classIndex]);
            }
        } else {
            for (int i = 0; i < numClasses; i++) {
                expectedOutput[i] = new float[numClasses];
                expectedOutput[i][i] = 1;
            }
        }
        if (numericalInputNormalization == NO_NORMALIZATION) {
            normalizationFunction = new NoNormalization();
        } else if (numericalInputNormalization == LINEAR_NORMALIZATION) {
            normalizationFunction = new LinearNormalization();
        } else if (numericalInputNormalization == MEAN_0_STANDARD_DEVIATION_1_NORMALIZATION) {
            normalizationFunction = new Mean0StdDeviation1Normalization();
        }
        inputLayerIndexes = new int[numAttributes - 1];
        int counter = 1;
        Attribute att;
        inputLayerIndexes[0] = 0;
        for (int i = 0; i < (numAttributes - 1); i++) {
            att = attributeVector[i];
            if (i != classIndex) {
                if (att.isNumeric()) {
                    inputLayerIndexes[counter] = inputLayerIndexes[counter - 1] + 1;
                } else {
                    inputLayerIndexes[counter] = inputLayerIndexes[counter - 1] + att.numValues();
                }
                counter++;
                if (inputLayerIndexes.length == counter) {
                    break;
                }
            }
        }
        for (int i = 0; i < numAttributes; i++) {
            if (i != classIndex) {
                if (instanceSet.getAttribute(i).isNumeric()) {
                    inputLayerSize++;
                } else {
                    inputLayerSize = inputLayerSize + instanceSet.getAttribute(i).numValues();
                }
            }
        }
        inputLayer = new float[inputLayerSize];
        if (hiddenLayerSize == AUTO_HIDDEN_LAYER_SIZE) {
            hiddenLayerSize = (numAttributes + 1) / 2;
            if (hiddenLayerSize < 3) {
                hiddenLayerSize = 3;
            }
        }
        hiddenLayer = new HiddenNeuron[hiddenLayerSize];
        for (int i = 0; i < hiddenLayer.length; i++) {
            hiddenLayer[i] = new HiddenNeuron(activationFunction, inputLayerSize, momentum);
        }
        hiddenLayerOutput = new float[hiddenLayerSize];
        if (numericOutput) {
            outputLayer = new OutputNeuron[1];
            outputLayer[0] = new OutputNeuron(activationFunction, hiddenLayer.length, momentum);
        } else {
            outputLayer = new OutputNeuron[numClasses];
            for (int i = 0; i < outputLayer.length; i++) {
                outputLayer[i] = new OutputNeuron(activationFunction, hiddenLayer.length, momentum);
            }
        }
        int inst;
        float instanceWeight;
        for (int epoch = 0; epoch < trainingTime; epoch++) {
            oldQuadraticError = quadraticError;
            quadraticError = 0;
            if (learningRateDecay) {
                learningRate = originalLearningRate / epoch;
            }
            for (inst = 0; inst < numInstances; inst++) {
                instance = instanceSet.getInstance(inst);
                instanceWeight = instance.getWeight();
                for (int i = 0; i < instanceWeight; i++) {
                    quadraticError = quadraticError + learn(instance);
                }
            }
            quadraticError = quadraticError / numWeightedInstances;
            if (meanSquaredError != null) {
                meanSquaredError.setMeanSquaredError(epoch, quadraticError);
            }
            if (minimumErrorVariation != NO_ERROR_VARIATION_STOP_CRITERION) {
                if (Math.abs((oldQuadraticError - quadraticError) * 100 / oldQuadraticError) < minimumErrorVariation) {
                    trainingTime = epoch;
                    break;
                }
            }
        }
    }
