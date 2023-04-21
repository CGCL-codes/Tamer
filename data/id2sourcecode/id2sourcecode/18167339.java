    public void nfrSample() {
        double[][] pointsSets = { { 0, 0, 20, 22 }, { 20, 22, 40, 42 }, { 40, 42, 80, 82 }, { 80, 82, 100, 100 } };
        double[][] timeSets = { { 15, 15, 20, 25 }, { 20, 25, 35, 40 }, { 35, 40, 1000, 1000 } };
        NeuralNetwork nnet = new NeuroFuzzyPerceptron(pointsSets, timeSets);
        TrainingSet tSet = new TrainingSet();
        Layer setLayer = nnet.getLayerAt(1);
        int outClass = 0;
        for (int i = 0; i <= 3; i++) {
            Neuron icell = setLayer.getNeuronAt(i);
            Trapezoid tfi = (Trapezoid) icell.getTransferFunction();
            double r1i = tfi.getRightLow();
            double l2i = tfi.getLeftHigh();
            double r2i = tfi.getRightHigh();
            double right_intersection_i = r2i + (r1i - r2i) / 2;
            for (int j = 6; j >= 4; j--) {
                Neuron jcell = setLayer.getNeuronAt(j);
                Trapezoid tfj = (Trapezoid) jcell.getTransferFunction();
                double r1j = tfj.getRightLow();
                double l2j = tfj.getLeftHigh();
                double r2j = tfj.getRightHigh();
                double right_intersection_j = r2j + (r1j - r2j) / 2;
                String outputPattern;
                if (outClass <= 3) {
                    outputPattern = "1 0 0 0";
                } else if ((outClass >= 4) && (outClass <= 6)) {
                    outputPattern = "0 1 0 0";
                } else if ((outClass >= 7) && (outClass <= 9)) {
                    outputPattern = "0 0 1 0";
                } else {
                    outputPattern = "0 0 0 1";
                }
                String inputPattern = Double.toString(l2i) + " " + Double.toString(l2j);
                SupervisedTrainingElement tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(l2i) + " " + Double.toString(r2j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(l2i) + " " + Double.toString(right_intersection_j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(r2i) + " " + Double.toString(l2j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(r2i) + " " + Double.toString(r2j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(r2i) + " " + Double.toString(right_intersection_j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(right_intersection_i) + " " + Double.toString(l2j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(right_intersection_i) + " " + Double.toString(r2j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                inputPattern = Double.toString(right_intersection_i) + " " + Double.toString(right_intersection_j);
                tEl = new SupervisedTrainingElement(inputPattern, outputPattern);
                tSet.addElement(tEl);
                outClass++;
            }
        }
        nnet.setLabel("NFR sample");
        tSet.setLabel("NFR tset");
        NeuralNetworkTraining controller = new NeuralNetworkTraining(nnet, tSet);
        for (CreateNeuralNetworkFileServiceInterface fileservices : ServiceLoader.load(CreateNeuralNetworkFileServiceInterface.class)) {
            fileservices.serialise(nnet);
        }
        for (CreateTrainigSetFileServiceInterface fileservices : ServiceLoader.load(CreateTrainigSetFileServiceInterface.class)) {
            fileservices.serialise(tSet);
        }
        NFRSampleTopComponent frame = new NFRSampleTopComponent();
        frame.setNeuralNetworkTrainingController(controller);
        frame.open();
        frame.requestActive();
    }
