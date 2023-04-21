    @Override
    public void crossover(AttributeWeightedExampleSet es1, AttributeWeightedExampleSet es2) {
        LinkedList<AttributeWeightContainer> dummyList1 = new LinkedList<AttributeWeightContainer>();
        LinkedList<AttributeWeightContainer> dummyList2 = new LinkedList<AttributeWeightContainer>();
        int maxSize = Math.max(es1.getAttributes().size(), es2.getAttributes().size());
        if (maxSize < 2) return;
        switch(getType()) {
            case SelectionCrossover.ONE_POINT:
                int splitPoint = 1 + random.nextInt(maxSize - 2);
                Iterator<Attribute> it = es1.getAttributes().iterator();
                int counter = 0;
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    if (counter > splitPoint) {
                        double weight = es1.getWeight(attribute);
                        it.remove();
                        dummyList1.add(new AttributeWeightContainer(attribute, weight));
                    }
                    counter++;
                }
                it = es2.getAttributes().iterator();
                counter = 0;
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    if (counter > splitPoint) {
                        double weight = es2.getWeight(attribute);
                        it.remove();
                        dummyList2.add(new AttributeWeightContainer(attribute, weight));
                    }
                    counter++;
                }
                break;
            case SelectionCrossover.UNIFORM:
                it = es1.getAttributes().iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    if (random.nextBoolean()) {
                        double weight = es1.getWeight(attribute);
                        dummyList1.add(new AttributeWeightContainer(attribute, weight));
                        it.remove();
                    }
                }
                it = es2.getAttributes().iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    if (random.nextBoolean()) {
                        double weight = es2.getWeight(attribute);
                        dummyList2.add(new AttributeWeightContainer(attribute, weight));
                        it.remove();
                    }
                }
                break;
            case SelectionCrossover.SHUFFLE:
                double prob1 = (double) (random.nextInt(es1.getAttributes().size() - 1) + 1) / (double) es1.getAttributes().size();
                it = es1.getAttributes().iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    if (random.nextDouble() < prob1) {
                        double weight = es1.getWeight(attribute);
                        dummyList1.add(new AttributeWeightContainer(attribute, weight));
                        it.remove();
                    }
                }
                double prob2 = (double) (random.nextInt(es2.getAttributes().size() - 1) + 1) / (double) es2.getAttributes().size();
                it = es2.getAttributes().iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    if (random.nextDouble() < prob2) {
                        double weight = es2.getWeight(attribute);
                        dummyList2.add(new AttributeWeightContainer(attribute, weight));
                        it.remove();
                    }
                }
                break;
            default:
                break;
        }
        mergeAttributes(es1, dummyList2);
        mergeAttributes(es2, dummyList1);
    }
