    public void crossOver(Gene a, Gene b, Gene a1, Gene b1) {
        int point = MyRandom.nextInt(bitsPerGene + 1);
        for (int j = 0; j < bitsPerGene; j++) {
            if (j < point) {
                a1.bits[j] = a.bits[j];
                b1.bits[j] = b.bits[j];
            } else {
                a1.bits[j] = b.bits[j];
                b1.bits[j] = a.bits[j];
            }
        }
    }
