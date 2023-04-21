    private static void reorder(AbstractReferenceSet other) {
        if (elSize == 0) {
            return;
        }
        if (readwriteset.add(other)) {
            for (int i = 0; i < publisherSize; i++) {
                eps[i].addChanges(other);
            }
        }
        IEventHandler[] ele = Arrays.copyOf(elements, elSize);
        int tempSize = elSize;
        elementData = null;
        elements = null;
        elSize = 0;
        levelSize = null;
        size = 0;
        for (int i = 0; i < tempSize; i++) {
            register2(ele[i]);
        }
    }
