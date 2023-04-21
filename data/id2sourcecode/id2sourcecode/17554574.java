    @Override
    protected boolean disconnectInput(Node inputNode, int inputNodeOutputIndex) {
        int deleteIndex = -1;
        boolean removed = false;
        int numberOfInputs = inputNodes.length;
        do {
            deleteIndex = -1;
            for (int i = 0; i < inputNodes.length; i++) {
                if (inputNode == inputNodes[i] && (inputNodeOutputIndex == -1 || inputNodeOutputIndex == inputNodeOutputIndices[i])) {
                    deleteIndex = i;
                    break;
                }
            }
            if (deleteIndex >= 0) {
                for (int i = deleteIndex + 1; i < inputNodes.length; i++) {
                    inputNodes[i - 1] = inputNodes[i];
                    inputNodeOutputIndices[i - 1] = inputNodeOutputIndices[i];
                    weights[i] = weights[i + 1];
                    weightChanges[i] = weightChanges[i + 1];
                    inputNodes[i - 1].outputNodeInputIndices[inputNodeOutputIndices[i - 1]] = i - 1;
                }
                numberOfInputs--;
                removed = true;
            }
        } while (inputNodeOutputIndex == -1 && deleteIndex != -1);
        Node[] newInputNodes = new Node[numberOfInputs];
        System.arraycopy(inputNodes, 0, newInputNodes, 0, numberOfInputs);
        inputNodes = newInputNodes;
        int[] newInputNodeOutputIndices = new int[numberOfInputs];
        System.arraycopy(inputNodeOutputIndices, 0, newInputNodeOutputIndices, 0, numberOfInputs);
        inputNodeOutputIndices = newInputNodeOutputIndices;
        double[] newWeights = new double[numberOfInputs + 1];
        System.arraycopy(weights, 0, newWeights, 0, numberOfInputs + 1);
        weights = newWeights;
        double[] newWeightChanges = new double[numberOfInputs + 1];
        System.arraycopy(weightChanges, 0, newWeightChanges, 0, numberOfInputs + 1);
        weightChanges = newWeightChanges;
        return removed;
    }
