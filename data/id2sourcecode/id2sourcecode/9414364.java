    private static void testForSubSetRandomOperationsStress() {
        IntegerSet bits = new IntegerSet(new ConciseSet());
        IntegerSet bitsPrevious = new IntegerSet(new ConciseSet());
        TreeSet<Integer> items = new TreeSet<Integer>();
        Random rnd = new MersenneTwister();
        for (int i = 0; i < 100000; i++) {
            System.out.print("Test " + i + ": ");
            bitsPrevious = bits.clone();
            if (!bitsPrevious.toString().equals(bits.toString())) throw new RuntimeException("clone() error!");
            bits.clear();
            items.clear();
            final int size = 1 + rnd.nextInt(10000);
            final int min = 1 + rnd.nextInt(10000 - 1);
            final int max = min + rnd.nextInt(10000 - min + 1);
            final int minSub = 1 + rnd.nextInt(10000 - 1);
            final int maxSub = minSub + rnd.nextInt(10000 - minSub + 1);
            for (int j = 0; j < size; j++) {
                int item = min + rnd.nextInt(max - min + 1);
                bits.add(item);
                items.add(item);
            }
            SortedSet<Integer> bitsSubSet = bits.subSet(minSub, maxSub);
            SortedSet<Integer> itemsSubSet = items.subSet(minSub, maxSub);
            if (!bitsSubSet.toString().equals(itemsSubSet.toString())) {
                System.out.println("toString() difference!");
                System.out.println("value: " + bitsSubSet.toString());
                System.out.println("actual: " + itemsSubSet.toString());
                return;
            }
            if (bitsSubSet.size() != itemsSubSet.size()) {
                System.out.println("size() difference!");
                System.out.println("value: " + bitsSubSet.size());
                System.out.println("actual: " + itemsSubSet.size());
                System.out.println("bits: " + bits.toString());
                System.out.println("items: " + items.toString());
                System.out.println("bitsSubSet: " + bitsSubSet.toString());
                System.out.println("itemsSubSet: " + itemsSubSet.toString());
                return;
            }
            if (!itemsSubSet.isEmpty() && (!bitsSubSet.first().equals(itemsSubSet.first()))) {
                System.out.println("first() difference!");
                System.out.println("value: " + bitsSubSet.first());
                System.out.println("actual: " + itemsSubSet.first());
                System.out.println("bits: " + bits.toString());
                System.out.println("items: " + items.toString());
                System.out.println("bitsSubSet: " + bitsSubSet.toString());
                System.out.println("itemsSubSet: " + itemsSubSet.toString());
                return;
            }
            if (!itemsSubSet.isEmpty() && (!bitsSubSet.last().equals(itemsSubSet.last()))) {
                System.out.println("last() difference!");
                System.out.println("value: " + bitsSubSet.last());
                System.out.println("actual: " + itemsSubSet.last());
                System.out.println("bits: " + bits.toString());
                System.out.println("items: " + items.toString());
                System.out.println("bitsSubSet: " + bitsSubSet.toString());
                System.out.println("itemsSubSet: " + itemsSubSet.toString());
                return;
            }
            boolean resBits = false;
            boolean resItems = false;
            boolean exceptionBits = false;
            boolean exceptionItems = false;
            switch(1 + rnd.nextInt(4)) {
                case 1:
                    System.out.format(" addAll() of %d elements on %d elements... ", bitsPrevious.size(), bits.size());
                    try {
                        resBits = bitsSubSet.addAll(bitsPrevious);
                    } catch (Exception e) {
                        bits.clear();
                        System.out.print("\n\tEXCEPTION on bitsSubSet: " + e.getClass() + " ");
                        exceptionBits = true;
                    }
                    try {
                        resItems = itemsSubSet.addAll(bitsPrevious);
                    } catch (Exception e) {
                        items.clear();
                        System.out.print("\n\tEXCEPTION on itemsSubSet: " + e.getClass() + " ");
                        exceptionItems = true;
                    }
                    break;
                case 2:
                    System.out.format(" removeAll() of %d elements on %d elements... ", bitsPrevious.size(), bits.size());
                    try {
                        resBits = bitsSubSet.removeAll(bitsPrevious);
                    } catch (Exception e) {
                        bits.clear();
                        System.out.print("\n\tEXCEPTION on bitsSubSet: " + e.getClass() + " ");
                        exceptionBits = true;
                    }
                    try {
                        resItems = itemsSubSet.removeAll(bitsPrevious);
                    } catch (Exception e) {
                        items.clear();
                        System.out.print("\n\tEXCEPTION on itemsSubSet: " + e.getClass() + " ");
                        exceptionItems = true;
                    }
                    break;
                case 3:
                    System.out.format(" retainAll() of %d elements on %d elements... ", bitsPrevious.size(), bits.size());
                    try {
                        resBits = bitsSubSet.retainAll(bitsPrevious);
                    } catch (Exception e) {
                        bits.clear();
                        System.out.print("\n\tEXCEPTION on bitsSubSet: " + e.getClass() + " ");
                        exceptionBits = true;
                    }
                    try {
                        resItems = itemsSubSet.retainAll(bitsPrevious);
                    } catch (Exception e) {
                        items.clear();
                        System.out.print("\n\tEXCEPTION on itemsSubSet: " + e.getClass() + " ");
                        exceptionItems = true;
                    }
                    break;
                case 4:
                    System.out.format(" clear() of %d elements on %d elements... ", bitsPrevious.size(), bits.size());
                    try {
                        bitsSubSet.clear();
                    } catch (Exception e) {
                        bits.clear();
                        System.out.print("\n\tEXCEPTION on bitsSubSet: " + e.getClass() + " ");
                        exceptionBits = true;
                    }
                    try {
                        itemsSubSet.clear();
                    } catch (Exception e) {
                        items.clear();
                        System.out.print("\n\tEXCEPTION on itemsSubSet: " + e.getClass() + " ");
                        exceptionItems = true;
                    }
                    break;
            }
            if (exceptionBits != exceptionItems) {
                System.out.println("Incorrect exception!");
                return;
            }
            if (resBits != resItems) {
                System.out.println("Incorrect results!");
                System.out.println("resBits: " + resBits);
                System.out.println("resItems: " + resItems);
                return;
            }
            if (!checkContent(bits, items)) {
                System.out.println("Subview not correct!");
                System.out.format("min: %d, max: %d, minSub: %d, maxSub: %d\n", min, max, minSub, maxSub);
                System.out.println("Same elements: " + (items.toString().equals(bits.toString())));
                System.out.println("Original: " + items);
                System.out.println(bits.debugInfo());
                System.out.println(bitsPrevious.debugInfo());
                return;
            }
            IntegerSet otherBits = new IntegerSet(new ConciseSet());
            otherBits.addAll(items);
            if (otherBits.hashCode() != bits.hashCode()) {
                System.out.println("Representation not correct!");
                System.out.format("min: %d, max: %d, minSub: %d, maxSub: %d\n", min, max, minSub, maxSub);
                System.out.println(bits.debugInfo());
                System.out.println(otherBits.debugInfo());
                System.out.println(bitsPrevious.debugInfo());
                return;
            }
            System.out.println("done.");
        }
    }
