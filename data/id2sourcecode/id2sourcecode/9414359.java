    private static void testForAdditionStress(Class<? extends ExtendedSet<Integer>> c) {
        ExtendedSet<Integer> previousBits = empty(c);
        ExtendedSet<Integer> currentBits = empty(c);
        TreeSet<Integer> currentItems = new TreeSet<Integer>();
        Random rnd = new MersenneTwister();
        for (int i = 0; i < 100000; i++) {
            int item = rnd.nextInt(10000 + 1);
            previousBits = currentBits;
            currentBits = currentBits.clone();
            System.out.format("Adding %d...\n", item);
            boolean itemExistsBefore = currentItems.contains(item);
            boolean itemAdded = currentItems.add(item);
            boolean itemExistsAfter = currentItems.contains(item);
            boolean bitExistsBefore = currentBits.contains(item);
            boolean bitAdded = currentBits.add(item);
            boolean bitExistsAfter = currentBits.contains(item);
            if (itemAdded ^ bitAdded) {
                System.out.println("wrong add() result");
                return;
            }
            if (itemExistsBefore ^ bitExistsBefore) {
                System.out.println("wrong contains() before");
                return;
            }
            if (itemExistsAfter ^ bitExistsAfter) {
                System.out.println("wrong contains() after");
                return;
            }
            if (!checkContent(currentBits, currentItems)) {
                System.out.println("add() error");
                System.out.println("Same elements: " + (currentItems.toString().equals(currentBits.toString())));
                System.out.println("\tcorrect: " + currentItems.toString());
                System.out.println("\twrong:   " + currentBits.toString());
                System.out.println("Original: " + currentItems);
                System.out.println(currentBits.debugInfo());
                System.out.println(previousBits.debugInfo());
                return;
            }
            ExtendedSet<Integer> otherBits = previousBits.convert(currentItems);
            if (otherBits.hashCode() != currentBits.hashCode()) {
                System.out.println("Representation error");
                System.out.println(currentBits.debugInfo());
                System.out.println(otherBits.debugInfo());
                System.out.println(previousBits.debugInfo());
                return;
            }
            ExtendedSet<Integer> singleBitSet = empty(c);
            singleBitSet.add(item);
            if (currentItems.size() != currentBits.unionSize(singleBitSet)) {
                System.out.println("Size error");
                System.out.println("Original: " + currentItems);
                System.out.println(currentBits.debugInfo());
                System.out.println(previousBits.debugInfo());
                return;
            }
        }
        System.out.println("Final");
        System.out.println(currentBits.debugInfo());
        System.out.println();
        System.out.println(IntSetStatistics.summary());
    }
