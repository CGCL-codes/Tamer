    private TravellerChromosome algorithm(TravellerChromosome parent) {
        MersenneTwister mt = MersenneTwister.getTwister();
        TravellerChromosome offspring = new TravellerChromosome(parent);
        offspring.canonicalize();
        double startingFitness = offspring.testFitness();
        int genomeLength = TravellerWorld.getTravellerWorld().getNumberOfCities();
        int permuteGlobalLimit = PermutationController.getGlobalPermuteLimit();
        int permuteStartingLimit = PermutationController.getStartingPermuteLimit();
        int permuteCurrentLimit = PermutationController.getCurrentPermuteLimit();
        double selectionChance = 0.0D;
        if (genomeLength > permuteGlobalLimit) {
            selectionChance = ((double) permuteCurrentLimit) / ((double) (permuteStartingLimit + permuteGlobalLimit));
        } else {
            selectionChance = ((double) permuteGlobalLimit) / 100.0D;
        }
        int selectees[] = new int[genomeLength];
        boolean selected[] = new boolean[genomeLength];
        for (int i = 0; i < genomeLength; i++) {
            selectees[i] = -1;
            selected[i] = false;
        }
        int offset = mt.nextInt(genomeLength);
        int selectionCount = 0;
        for (int i = 0; i < genomeLength; i++) {
            if (mt.nextDouble() < selectionChance) {
                selectees[selectionCount] = parent.getCity(i + offset);
                selected[(i + offset) % genomeLength] = true;
                selectionCount++;
            }
            if (selectionCount >= permuteCurrentLimit) {
                break;
            }
        }
        if (selectionCount != 0) {
            int unselectedIndex = 0;
            int copiedCount = 0;
            int lefties = mt.nextInt(genomeLength - selectionCount);
            while (copiedCount < lefties) {
                while (selected[unselectedIndex]) {
                    unselectedIndex++;
                }
                offspring.setCity(copiedCount, parent.getCity(unselectedIndex));
                unselectedIndex++;
                copiedCount++;
            }
            for (int currentSlot = selectionCount - 1; currentSlot > 0; currentSlot--) {
                int swapSlot = mt.nextInt(currentSlot + 1);
                int temp = selectees[swapSlot];
                selectees[swapSlot] = selectees[currentSlot];
                selectees[currentSlot] = temp;
            }
            for (int i = 0; i < selectionCount; i++) {
                offspring.setCity(copiedCount, selectees[i]);
                copiedCount++;
            }
            while (copiedCount < genomeLength) {
                while (selected[unselectedIndex]) {
                    unselectedIndex++;
                }
                offspring.setCity(copiedCount, parent.getCity(unselectedIndex));
                unselectedIndex++;
                copiedCount++;
            }
        }
        offspring.canonicalize();
        double finalFitness = offspring.testFitness();
        if (adaptPermutation) {
            if ((finalFitness > startingFitness) || (Math.abs(finalFitness - startingFitness) < TravellerStatus.LITTLE_FUZZ)) {
                PermutationController.reportFailure();
            } else {
                PermutationController.reportSuccess();
            }
        }
        return offspring;
    }
