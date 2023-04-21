    public void CreatePopulation(int popSize) throws Exception {
        InitPopulation(popSize);
        int segmentation = m_numAttribs / 2;
        for (int i = 0; i < m_popSize; i++) {
            List<Subset> attributeRankingCopy = new ArrayList<Subset>();
            for (int j = 0; j < m_attributeRanking.size(); j++) attributeRankingCopy.add(m_attributeRanking.get(j));
            double last_evaluation = -999;
            double current_evaluation = 0;
            boolean doneAnew = true;
            while (true) {
                int random_number = m_random.nextInt(segmentation + 1);
                if (doneAnew && i <= segmentation) random_number = i;
                doneAnew = false;
                Subset s1 = ((Subset) attributeRankingCopy.get(random_number)).clone();
                Subset s2 = ((Subset) m_population.get(i)).clone();
                Subset joiners = joinSubsets(s1, s2);
                current_evaluation = joiners.merit;
                if (current_evaluation > last_evaluation) {
                    m_population.set(i, joiners);
                    last_evaluation = current_evaluation;
                    try {
                        attributeRankingCopy.set(random_number, attributeRankingCopy.get(segmentation + 1));
                        attributeRankingCopy.remove(segmentation + 1);
                    } catch (IndexOutOfBoundsException ex) {
                        attributeRankingCopy.set(random_number, new Subset(new BitSet(m_numAttribs), 0));
                        continue;
                    }
                } else {
                    break;
                }
            }
        }
    }
