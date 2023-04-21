    public void initInputLayer(ExampleSet exampleSet, boolean normalize) {
        inputNodes = new InputNode[exampleSet.getAttributes().size()];
        int a = 0;
        for (Attribute attribute : exampleSet.getAttributes()) {
            inputNodes[a] = new InputNode(attribute.getName());
            double range = 1;
            double offset = 0;
            if (normalize) {
                double min = exampleSet.getStatistics(attribute, Statistics.MINIMUM);
                double max = exampleSet.getStatistics(attribute, Statistics.MAXIMUM);
                range = (max - min) / 2;
                offset = (max + min) / 2;
            }
            inputNodes[a].setAttribute(attribute, range, offset, normalize);
            a++;
        }
    }
