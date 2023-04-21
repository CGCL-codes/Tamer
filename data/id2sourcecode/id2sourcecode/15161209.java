    private static Credential[] normalise(Credential[] product) {
        Credential[] p1 = new Credential[product.length];
        System.arraycopy(product, 0, p1, 0, p1.length);
        for (int i = p1.length - 1; i-- > 0; ) {
            Credential c = p1[i];
            int j;
            for (j = i; j < p1.length - 1; j++) {
                if (c.contains(p1[j + 1])) {
                    break;
                } else {
                    p1[j] = p1[j + 1];
                }
            }
            p1[j] = c;
        }
        return p1;
    }
