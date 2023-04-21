    @Test
    public void largeUniform() {
        int numDraws = 1000;
        double start = 10;
        double end = 20;
        sweep = new UniformDoubleSweep("param", start, end, numDraws);
        maps = sweep.generateMaps(rng);
        assertEquals(numDraws, maps.size());
        double total = 0;
        for (int i = 0; i < numDraws; i++) {
            double value = ((Double) maps.get(i).get("param")).doubleValue();
            assertTrue(value >= start && value < end);
            total += value;
        }
        double sampleMean = total / numDraws;
        double mean = (start + end) / 2;
        double stddev = (end - start) / sqrt(12);
        double confidenceInterval = 2 * stddev / sqrt(numDraws);
        assertEquals(mean, sampleMean, confidenceInterval);
    }
