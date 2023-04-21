    private static void testForSkip(Class<? extends ExtendedSet<Integer>> c) {
        ExtendedSet<Integer> bits = empty(c);
        Random rnd = new MersenneTwister(31);
        for (int i = 0; i < 10000; i++) {
            int max = rnd.nextInt(10000);
            bits = bits.convert(new RandomNumbers.Uniform(rnd.nextInt(1000), rnd.nextDouble() * 0.999, rnd.nextInt(100)).generate());
            for (int j = 0; j < 100; j++) {
                int skip = rnd.nextInt(max + 1);
                boolean reverse = rnd.nextBoolean();
                System.out.format("%d) size=%d, skip=%d, reverse=%b ---> ", (i * 100) + j + 1, bits.size(), skip, reverse);
                ExtendedIterator<Integer> itr1, itr2;
                if (!reverse) {
                    itr1 = bits.iterator();
                    itr2 = bits.iterator();
                    while (itr1.hasNext() && itr1.next() < skip) {
                    }
                } else {
                    itr1 = bits.descendingIterator();
                    itr2 = bits.descendingIterator();
                    while (itr1.hasNext() && itr1.next() > skip) {
                    }
                }
                if (!itr1.hasNext()) {
                    System.out.println("Skipped!");
                    continue;
                }
                itr2.skipAllBefore(skip);
                itr2.next();
                Integer i1, i2;
                if (!(i1 = itr1.next()).equals(i2 = itr2.next())) {
                    System.out.println("Error!");
                    System.out.println("i1 = " + i1);
                    System.out.println("i2 = " + i2);
                    System.out.println(bits.debugInfo());
                    return;
                }
                System.out.println("OK!");
            }
        }
        System.out.println("Done!");
    }
