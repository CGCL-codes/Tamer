        public Layer(int num, ALayer pl, int inputs_per_neuron) {
            prevLayer = pl;
            index = num;
            ANeuron[] copyN;
            ANeuron[] inputsChoosen = new ANeuron[inputs_per_neuron];
            prevLayer = pl;
            ALayer aLayer;
            ANeuron choosen;
            Neuron berle = new Neuron();
            layerProgress = 0;
            boolean tryAgain;
            inputsToNeuron = inputs_per_neuron;
            number = c.getLayerNeuronsNumber(num);
            inumber = c.getLayerInitialNeuronsNumber(num);
            if (number > inumber) {
                number = inumber;
            }
            int actInput, golayer, which, s;
            berle.init(ja, inputsChoosen, inputs_per_neuron);
            mode = PASSIVE;
            prevLayer.computeOutputs();
            for (int i = 0; i < inumber; i++) {
                copyN = prevLayer.getRandomNeurons(2);
                inputsChoosen[0] = copyN[0];
                inputsChoosen[1] = copyN[1];
                actInput = 2;
                while (actInput < inputs_per_neuron) {
                    do {
                        aLayer = prevLayer;
                        switch(c.getParents()) {
                            case NetworkConfiguration.YOUNG:
                                if (actInput >= aLayer.getNumber()) {
                                    aLayer = aLayer.getPreviousLayer();
                                }
                                break;
                            case NetworkConfiguration.YOUNGER:
                                for (int j = s = 0; j < num + 1; j++) {
                                    s += (j + 1);
                                }
                                s = myRandom.nextInt(s + 1) - num - 1;
                                for (int j = num; s > 0; s -= j--) {
                                    if (aLayer != null) {
                                        aLayer = aLayer.getPreviousLayer();
                                    }
                                }
                                break;
                            case NetworkConfiguration.MIDDLE:
                                s = myRandom.nextInt(num + 1);
                                for (int j = 0; j < s; j++) {
                                    if (aLayer != null) {
                                        aLayer = aLayer.getPreviousLayer();
                                    }
                                }
                                break;
                            case NetworkConfiguration.OLDER:
                                for (int j = s = 0; j < num + 1; j++) {
                                    s += (j + 1);
                                }
                                s = myRandom.nextInt(s + 1) - num - 1;
                                for (int j = num; s > 0; s -= (num - (--j))) {
                                    if (aLayer != null) {
                                        aLayer = aLayer.getPreviousLayer();
                                    }
                                }
                                break;
                            case NetworkConfiguration.OLD:
                                if (actInput >= aLayer.getNumber()) {
                                    while ((aLayer.getPreviousLayer() != null) && (aLayer.getPreviousLayer().getPreviousLayer() != null)) {
                                        aLayer = aLayer.getPreviousLayer();
                                    }
                                } else {
                                    while ((aLayer != null) && (aLayer.getPreviousLayer() != null)) {
                                        aLayer = aLayer.getPreviousLayer();
                                    }
                                }
                                break;
                        }
                        choosen = aLayer.getNeuron(myRandom.nextInt(aLayer.getNumber()));
                        tryAgain = false;
                        for (int j = 0; j < actInput; j++) {
                            if (inputsChoosen[j].equals(choosen)) {
                                tryAgain = true;
                            }
                        }
                    } while (tryAgain);
                    inputsChoosen[actInput] = choosen;
                    actInput++;
                }
                do {
                    which = myRandom.nextInt(u.getNeuronsNumber());
                } while (!c.neuronTypeAllowed(which));
                int train;
                do {
                    train = myRandom.nextInt(u.getTrainersNumber());
                } while (!GlobalConfig.getInstance().getGac().neuronTrainerAllowed(train));
                try {
                    n[i] = (Neuron) u.getNeuronClass(which).newInstance();
                    n[i].init(ja, inputsChoosen, inputs_per_neuron, u.getNeuronConfig(which));
                    Trainer tt = (Trainer) u.getTrainerClass(train).newInstance();
                    tt.init(n[i], tt, n[i].getCoefsNumber());
                    n[i].setTrainer(tt);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    i--;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    i--;
                }
            }
            mode = ACTIVE;
        }
