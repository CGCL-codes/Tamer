    private static void testForSubSetRemovalStress() {
        IntegerSet previousBits = new IntegerSet(new ConciseSet());
        IntegerSet currentBits = new IntegerSet(new ConciseSet());
        TreeSet<Integer> currentItems = new TreeSet<Integer>();
        currentBits.add(10001);
        currentBits.complement();
        currentItems.addAll(currentBits);
        if (currentItems.size() != 10001) {
            System.out.println("Unexpected error!");
            return;
        }
        Random rnd = new MersenneTwister();
        for (int j = 0; j < 100000; j++) {
            previousBits = currentBits;
            currentBits = currentBits.clone();
            int min = rnd.nextInt(10000);
            int max = min + 1 + rnd.nextInt(10000 - (min + 1) + 1);
            int item = rnd.nextInt(10000 + 1);
            System.out.println("Removing " + item + " from the subview from " + min + " to " + max + " - 1");
            SortedSet<Integer> subBits = currentBits.subSet(min, max);
            SortedSet<Integer> subItems = currentItems.subSet(min, max);
            boolean subBitsResult = subBits.remove(item);
            boolean subItemsResult = subItems.remove(item);
            if (subBitsResult != subItemsResult || subBits.size() != subItems.size() || !subBits.toString().equals(subItems.toString())) {
                System.out.println("Subset error!");
                return;
            }
            if (!checkContent(currentBits, currentItems)) {
                System.out.println("Subview not correct!");
                System.out.println("Same elements: " + (currentItems.toString().equals(currentBits.toString())));
                System.out.println("Original: " + currentItems);
                System.out.println(currentBits.debugInfo());
                System.out.println(previousBits.debugInfo());
                return;
            }
            IntegerSet otherBits = new IntegerSet(new ConciseSet());
            otherBits.addAll(currentItems);
            if (otherBits.hashCode() != currentBits.hashCode()) {
                System.out.println("Representation not correct!");
                System.out.println(currentBits.debugInfo());
                System.out.println(otherBits.debugInfo());
                System.out.println(previousBits.debugInfo());
                return;
            }
        }
        System.out.println(currentBits.debugInfo());
        System.out.println(IntSetStatistics.summary());
    }
