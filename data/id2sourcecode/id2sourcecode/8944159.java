    private void initOutputLayer(Attribute label, int numberOfClasses, double min, double max, RandomGenerator randomGenerator) {
        double range = (max - min) / 2;
        double offset = (max + min) / 2;
        outputNodes = new OutputNode[numberOfClasses];
        for (int o = 0; o < numberOfClasses; o++) {
            if (!label.isNominal()) {
                outputNodes[o] = new OutputNode(label.getName(), label, range, offset);
            } else {
                outputNodes[o] = new OutputNode(label.getMapping().mapIndex(o), label, range, offset);
                outputNodes[o].setClassIndex(o);
            }
            InnerNode actualOutput = null;
            if (label.isNominal()) {
                String classValue = label.getMapping().mapIndex(o);
                actualOutput = new InnerNode("Class '" + classValue + "'", Node.OUTPUT, randomGenerator, SIGMOID_FUNCTION);
            } else {
                actualOutput = new InnerNode("Regression", Node.OUTPUT, randomGenerator, LINEAR_FUNCTION);
            }
            addNode(actualOutput);
            Node.connect(actualOutput, outputNodes[o]);
        }
    }
