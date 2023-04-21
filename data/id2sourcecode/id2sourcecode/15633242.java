    private void setupHiddenLayer() {
        StringTokenizer tok = new StringTokenizer(m_hiddenLayers, ",");
        int val = 0;
        int prev = 0;
        int num = tok.countTokens();
        String c;
        for (int noa = 0; noa < num; noa++) {
            c = tok.nextToken().trim();
            if (c.equals("a")) {
                val = (m_numAttributes + m_numClasses) / 2;
            } else if (c.equals("i")) {
                val = m_numAttributes;
            } else if (c.equals("o")) {
                val = m_numClasses;
            } else if (c.equals("t")) {
                val = m_numAttributes + m_numClasses;
            } else {
                val = Double.valueOf(c).intValue();
            }
            for (int nob = 0; nob < val; nob++) {
                NeuralNode temp = new NeuralNode(String.valueOf(m_nextId), m_random, m_sigmoidUnit);
                m_nextId++;
                temp.setX(.5 / (num) * noa + .25);
                temp.setY((nob + 1.0) / (val + 1));
                addNode(temp);
                if (noa > 0) {
                    for (int noc = m_neuralNodes.length - nob - 1 - prev; noc < m_neuralNodes.length - nob - 1; noc++) {
                        NeuralConnection.connect(m_neuralNodes[noc], temp);
                    }
                }
            }
            prev = val;
        }
        tok = new StringTokenizer(m_hiddenLayers, ",");
        c = tok.nextToken();
        if (c.equals("a")) {
            val = (m_numAttributes + m_numClasses) / 2;
        } else if (c.equals("i")) {
            val = m_numAttributes;
        } else if (c.equals("o")) {
            val = m_numClasses;
        } else if (c.equals("t")) {
            val = m_numAttributes + m_numClasses;
        } else {
            val = Double.valueOf(c).intValue();
        }
        if (val == 0) {
            for (int noa = 0; noa < m_numAttributes; noa++) {
                for (int nob = 0; nob < m_numClasses; nob++) {
                    NeuralConnection.connect(m_inputs[noa], m_neuralNodes[nob]);
                }
            }
        } else {
            for (int noa = 0; noa < m_numAttributes; noa++) {
                for (int nob = m_numClasses; nob < m_numClasses + val; nob++) {
                    NeuralConnection.connect(m_inputs[noa], m_neuralNodes[nob]);
                }
            }
            for (int noa = m_neuralNodes.length - prev; noa < m_neuralNodes.length; noa++) {
                for (int nob = 0; nob < m_numClasses; nob++) {
                    NeuralConnection.connect(m_neuralNodes[noa], m_neuralNodes[nob]);
                }
            }
        }
    }
