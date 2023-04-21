    @Override
    public void mutate(PermutationGenotype<?> genotype, double p) {
        int size = genotype.size();
        if (size > 1) {
            for (int a = 0; a < size - 1; a++) {
                if (random.nextDouble() < p) {
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
        }
    }
