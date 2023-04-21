    static GeneticOperator getTwiddleOperator(final EvolutionChamber c) {
        return new GeneticOperator() {

            @Override
            public void operate(Population arg0, List arg1) {
                for (int i = 0; i < arg0.size(); i++) {
                    if (Math.random() > c.getBaseMutationRate() / c.getChromosomeLength()) continue;
                    IChromosome chromosome = (IChromosome) ((ICloneable) arg0.getChromosome(i)).clone();
                    Gene[] beforeArray = chromosome.getGenes();
                    int randomPoint = (int) (Math.random() * beforeArray.length);
                    if (randomPoint < beforeArray.length - 1) {
                        Gene swap = beforeArray[randomPoint];
                        beforeArray[randomPoint] = beforeArray[randomPoint + 1];
                        beforeArray[randomPoint + 1] = swap;
                    }
                    try {
                        chromosome.setGenes(beforeArray);
                    } catch (InvalidConfigurationException e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        logger.severe(sw.toString());
                    }
                    arg1.add(chromosome);
                }
            }
        };
    }
