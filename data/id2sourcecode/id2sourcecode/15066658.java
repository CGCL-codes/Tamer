    public void crossOver(Object ao, Object bo, Object a1o, Object b1o) {
        Gene a = (Gene) ao;
        Gene b = (Gene) bo;
        Gene a1 = (Gene) a1o;
        Gene b1 = (Gene) b1o;
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
