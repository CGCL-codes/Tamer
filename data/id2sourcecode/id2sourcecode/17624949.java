        public double teachLayer(InputLayer iLayer) {
            double squareError, error;
            Neuron swap;
            int lastIndex;
            boolean anyChange;
            int vTest = (int) (group * (c.getVectorsInTestingSet() / 100.0));
            int rVector;
            job = ENTERING_PASSIVE_MODE;
            mode = PASSIVE;
            myRandom.resetRandom();
            for (int i = 0; i < group - vTest; i++) {
                rVector = myRandom.getRandomLearningVector();
                iLayer.setActualVector(rVector);
                layerProgress = (int) (100.0 * i / (double) (group - vTest - 1));
                if (layerProgress % 100 == 1) {
                    myGraph.redraw();
                }
                prevLayer.computeOutputs();
                for (int act = 0; act < inumber; act++) {
                    n[act].storeInputValue(rVector);
                }
            }
            myGraph.redraw();
            mode = ACTIVE;
            job = NEW_NEURON;
            for (int act = 0; act < inumber; act++) {
                actualNeuron = act;
                n[act].learnYourself((Unit) n[act]);
            }
            job = ERROR_COMPUTING;
            myRandom.resetRandom();
            for (int i = 0; i < group; i++) {
                rVector = 0;
                if (i < group - vTest) {
                    rVector = myRandom.getRandomLearningVector();
                }
                if (i == group - vTest) {
                    myRandom.resetRandom();
                }
                if (i >= group - vTest) {
                    rVector = myRandom.getRandomTestingVector();
                }
                iLayer.setActualVector(rVector);
                mode = PASSIVE;
                prevLayer.computeOutputs();
                for (int act = 0; act < inumber; act++) {
                    error = n[act].getError(rVector);
                    n[act].setSquareError(n[act].getSquareError() + error * error);
                    layerProgress = (int) (100.0 * (i + (double) act / (double) (inumber - 1)) / (double) (group));
                }
                if (layerProgress % 100 == 1) {
                    myGraph.redraw();
                }
                mode = ACTIVE;
            }
            myGraph.redraw();
            job = NEURONS_SORTING;
            lastIndex = inumber - 1;
            do {
                anyChange = false;
                for (int i = 0; i < lastIndex; i++) {
                    if (n[i].getSquareError() > n[i + 1].getSquareError()) {
                        anyChange = true;
                        swap = n[i];
                        n[i] = n[i + 1];
                        n[i + 1] = swap;
                    }
                }
                lastIndex--;
                layerProgress = 100 * (inumber - lastIndex - 1) / (inumber - 1);
                if (layerProgress % 100 == 1) {
                    myGraph.redraw();
                }
            } while (anyChange);
            layerProgress = 100;
            myGraph.redraw();
            job = NONE;
            for (int i = number; i < inumber; i++) {
                n[i] = null;
            }
            if (c.isCommonResponse()) {
                double ret = 0, div = 0;
                for (int i = 0; i < number; i++) {
                    ret += (number - i) * (number - i) * n[i].getSquareError();
                    div += (number - i) * (number - i);
                }
                return ret / div;
            }
            return n[0].getSquareError();
        }
