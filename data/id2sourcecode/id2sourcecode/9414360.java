    private static void testForRemovalStress(Class<? extends ExtendedSet<Integer>> c) {
        ExtendedSet<Integer> previousBits = empty(c);
        ExtendedSet<Integer> currentBits = empty(c);
        TreeSet<Integer> currentItems = new TreeSet<Integer>();
        Random rnd = new MersenneTwister();
        currentBits.add((1 << MatrixIntSet.COL_POW) * 5 - 1);
        currentBits.complement();
        currentItems.addAll(currentBits);
        if (currentItems.size() != (1 << MatrixIntSet.COL_POW) * 5 - 1) {
            System.out.println("Unexpected error!");
            System.out.println(currentBits.size());
            System.out.println(currentItems.size());
            return;
        }
        for (int i = 0; i < 100000 & !currentBits.isEmpty(); i++) {
            int item = rnd.nextInt(10000 + 1);
            previousBits = currentBits;
            currentBits = currentBits.clone();
            System.out.format("Removing %d...\n", item);
            boolean itemExistsBefore = currentItems.contains(item);
            boolean itemRemoved = currentItems.remove(item);
            boolean itemExistsAfter = currentItems.contains(item);
            boolean bitExistsBefore = currentBits.contains(item);
            boolean bitRemoved = currentBits.remove(item);
            boolean bitExistsAfter = currentBits.contains(item);
            if (itemRemoved ^ bitRemoved) {
                System.out.println("wrong remove() result");
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
                System.out.println("remove() error");
                System.out.println("Same elements: " + (currentItems.toString().equals(currentBits.toString())));
                System.out.println("Original: " + currentItems);
                System.out.println(currentBits.debugInfo());
                System.out.println(previousBits.debugInfo());
                return;
            }
            ExtendedSet<Integer> otherBits = empty(c);
            otherBits.addAll(currentItems);
            if (otherBits.hashCode() != currentBits.hashCode()) {
                System.out.println("Representation error");
                System.out.println(currentBits.debugInfo());
                System.out.println(otherBits.debugInfo());
                System.out.println(previousBits.debugInfo());
                return;
            }
            ExtendedSet<Integer> singleBitSet = empty(c);
            singleBitSet.add(item);
            if (currentItems.size() != currentBits.differenceSize(singleBitSet)) {
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
