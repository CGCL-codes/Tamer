    private static final BigInteger[] sort(BigInteger[] result) {
        while (true) {
            int i = 0;
            while ((i < 3) && (result[i].compareTo(result[i + 1]) < 1)) i++;
            if (i == 3) return result;
            BigInteger t = result[i];
            result[i] = result[i + 1];
            result[i + 1] = t;
        }
    }
