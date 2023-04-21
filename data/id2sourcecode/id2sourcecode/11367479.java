    @Override
    public void neighbor(PermutationGenotype<?> genotype) {
        int size = genotype.size();
        if (size > 1) {
            int a = random.nextInt(size - 1);
            int b;
            do {
                b = a + random.nextInt(size - a);
            } while (b == a);
            while (a < b) {
                Collections.swap(genotype, a, b);
                a++;
                b--;
            }
        }
    }
