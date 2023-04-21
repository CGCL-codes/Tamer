        public Layer(int num, ALayer pl, int inputs_per_neuron) {
            prevLayer = pl;
            index = num;
            ANeuron[] copyN;
            ANeuron[] inputsChoosen;
            g = new Genome[maxNeuronsInLayer];
            prevLayer = pl;
            ALayer aLayer = prevLayer;
            ANeuron choosen;
            Dimension pos;
            new Dimension();
            layerProgress = 0;
            boolean tryAgain;
            inputsToNeuron = inputs_per_neuron;
            logger.info("New layer " + num + ", max inputs:" + inputs_per_neuron);
            inumber = c.getLayerInitialNeuronsNumber(num);
            logger.info("Number of neurons in the population:" + inumber);
            number = inumber;
            if (number > inumber) {
                number = inumber;
            }
            int actInput, golayer, which, s;
            inputsPossible = 0;
            while (aLayer != null) {
                inputsPossible += aLayer.getNumber();
                aLayer = aLayer.getPreviousLayer();
            }
            if (c.isJustTwo()) {
                inputs_per_neuron = inputsToNeuron = inputsPossible;
            }
            inputsChoosen = new ANeuron[inputs_per_neuron];
            mode = PASSIVE;
            prevLayer.computeOutputs();
            for (int i = 0; i < inumber; i++) {
                copyN = prevLayer.getRandomNeurons(1);
                aLayer = prevLayer;
                ALayer al;
                inputsChoosen[0] = copyN[0];
                int inputsPN;
                if (c.isJustTwo()) {
                    inputsPN = myRandom.nextInt(inputs_per_neuron);
                    inputsPN++;
                } else {
                    inputsPN = inputs_per_neuron;
                }
                if (c.isEmployPrevious()) {
                    actInput = 1;
                } else {
                    actInput = 0;
                }
                int full;
                while (actInput < inputsPN) {
                    do {
                        aLayer = prevLayer;
                        switch(c.getParents()) {
                            case NetworkConfiguration.YOUNG:
                                full = aLayer.getNumber();
                                while (actInput >= full) {
                                    aLayer = aLayer.getPreviousLayer();
                                    if (aLayer != null) {
                                        full += aLayer.getNumber();
                                    } else {
                                        break;
                                    }
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
                                full = 0;
                                do {
                                    al = getPreviousLayer();
                                    while (al.getPreviousLayer() != null) {
                                        al = al.getPreviousLayer();
                                    }
                                    aLayer = al;
                                    full += aLayer.getNumber();
                                } while (actInput >= full);
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
                g[i] = new Genome(iNum, inputsPossible, inputsPN);
                inputsToNeuron = inputsPN;
                do {
                    which = myRandom.nextInt(u.getNeuronsNumber());
                } while (!c.neuronTypeAllowed(which));
                int train;
                do {
                    train = myRandom.nextInt(u.getTrainersNumber());
                } while (!c.neuronTrainerAllowed(train));
                do {
                    n[i] = newNeuron(which, train, inputsChoosen);
                } while (n[i] == null);
                logger.trace("New neuron " + i + ", type id:" + which + ", trainer id:" + train);
                for (int j = 0; j < inputsPN; j++) {
                    pos = getNeuronParentPosition(n[i], j);
                    if (pos == null) {
                        int h = 0;
                    }
                    int ipos = (int) pos.getWidth();
                    int layerDist = (int) pos.getHeight();
                    ALayer pLay = this;
                    for (int k = 0; k < layerDist; k++) {
                        if (pLay != null) {
                            pLay = pLay.getPreviousLayer();
                        }
                    }
                    if (pLay != null) {
                        pLay = pLay.getPreviousLayer();
                    }
                    while (pLay != null) {
                        ipos += pLay.getNumber();
                        pLay = pLay.getPreviousLayer();
                    }
                    g[i].setInput(ipos, 1);
                }
                if (n[i].getClass().getName().compareTo("game.neurons.CombiNeuron") == 0) {
                    g[i] = new CombiGenome((CombiNeuron) n[i], g[i]);
                    ((CombiNeuron) n[i]).readGenome(((CombiGenome) g[i]));
                }
                logger.trace("Neuron input connections:" + g[i].toString());
            }
            mode = ACTIVE;
            inputsToNeuron = inputs_per_neuron;
        }
