    public TravellerChromosome(TravellerWorld world, String originatorName) {
        super();
        m_world = world;
        m_originator = new String(originatorName);
        if (m_mt == null) {
            m_mt = MersenneTwister.getTwister();
        }
        m_fitness = Double.MAX_VALUE;
        m_fitnessValid = false;
        m_cityList = new Codon[m_world.getNumberOfCities()];
        for (int i = 0; i < m_cityList.length; i++) {
            m_cityList[i] = new Codon(t_model.nodeIndex[i]);
        }
        for (int currentSlot = m_cityList.length - 1; currentSlot > 0; currentSlot--) {
            int swapSlot = m_mt.nextInt(currentSlot + 1);
            int temp = m_cityList[swapSlot].get();
            m_cityList[swapSlot].set(m_cityList[currentSlot].get());
            m_cityList[currentSlot].set(temp);
        }
    }
