    public void randomize(Individual<T> c, int min, int max) {
        IntegerChromosome chrom = (IntegerChromosome) c.getChromosome();
        int len = max - min + 1;
        int[] base = new int[len];
        for (int i = 0; i < len; i++) base[i] = chrom.getValue(min + i);
        int i = 0;
        while (len != 0) {
            int pos = Random.getInstance().nextInt(0, len);
            chrom.setValue(min + i, base[pos]);
            for (int j = pos; j < (len - 1); j++) {
                base[j] = base[j + 1];
            }
            len--;
            i++;
        }
    }
