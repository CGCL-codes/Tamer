    public void testDistribution() {
        PureConsistentHashBalancerAlgorithm algo = new PureConsistentHashBalancerAlgorithm();
        for (Integer i = 0; i < 12199; i++) {
            System.out.println(Integer.toString(algo.digest(i.toString()), 2));
            if (!set.add(new Integer(algo.digest(i.toString())))) fail("duplicate for i " + i);
        }
        boolean bits[] = new boolean[32];
        for (int q = 0; q < 31; q++) {
            boolean has1 = false;
            boolean has0 = false;
            for (Integer i : set) {
                boolean isOne = (i & (1 << q)) > 0;
                if (isOne) has1 = true; else has0 = true;
                if (has1 && has0) break;
            }
            if (has1 && has0) {
                bits[q] = true;
            }
        }
        for (int q = 0; q < 31; q++) {
            if (!bits[q]) fail("Bit " + q + " is never changing");
        }
    }
