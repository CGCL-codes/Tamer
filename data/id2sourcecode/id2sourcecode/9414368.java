    private static void testForPosition(Class<? extends ExtendedSet<Integer>> c) {
        ExtendedSet<Integer> bits = empty(c);
        Random rnd = new MersenneTwister(31);
        for (int i = 0; i < 1000; i++) {
            bits.clear();
            final int size = 1 + rnd.nextInt(10000);
            final int min = 1 + rnd.nextInt(10000 - 1);
            final int max = min + rnd.nextInt(10000 - min + 1);
            for (int j = 0; j < size; j++) {
                int item = min + rnd.nextInt(max - min + 1);
                bits.add(item);
            }
            String good = bits.toString();
            StringBuilder other = new StringBuilder();
            int s = bits.size();
            other.append('[');
            for (int j = 0; j < s; j++) {
                other.append(bits.get(j));
                if (j < s - 1) other.append(", ");
            }
            other.append(']');
            if (good.equals(other.toString())) {
                System.out.println(i + ") OK");
            } else {
                System.out.println("ERROR");
                System.out.println(bits.debugInfo());
                System.out.println(bits);
                System.out.println(other);
                return;
            }
            int pos = 0;
            for (Integer x : bits) {
                if (bits.indexOf(x) != pos) {
                    System.out.println("ERROR! " + pos + " != " + bits.indexOf(x) + " for element " + x);
                    System.out.println(bits.debugInfo());
                    return;
                }
                pos++;
            }
        }
    }
