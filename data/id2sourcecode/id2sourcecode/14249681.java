    private static void register(IEventHandler subscriber) {
        if (elementData == null) {
            elementData = new IEventHandler[2][];
            elementData[0] = new IEventHandler[2];
            levelSize = new int[2];
            elementData[0][0] = subscriber;
            size = 1;
            levelSize[0] = 1;
            elements = new IEventHandler[2];
            elSize = 1;
            elements[0] = subscriber;
            AbstractReferenceSet other = subscriber.readwriteset();
            if (readwriteset.add(other)) {
                for (int i = 0; i < publisherSize; i++) {
                    eps[i].addChanges(other);
                }
            }
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            IEventHandler[] temp = elementData[i];
            int tempSize = levelSize[i];
            for (int j = 0; j < tempSize; j++) if (subscriber == temp[j]) return;
        }
        AbstractReferenceSet other = subscriber.readwriteset();
        if (readwriteset.add(other)) {
            for (int i = 0; i < publisherSize; i++) {
                eps[i].addChanges(other);
            }
        }
        ensureSubscriberCapacity(elSize + 1);
        elements[elSize++] = subscriber;
        IEventHandler[] temp;
        for (int i = size - 1; i >= 0; i--) {
            temp = elementData[i];
            int tempSize = levelSize[i];
            for (int j = 0; j < tempSize; j++) {
                if (comparePredecessors(subscriber, temp[j])) {
                    if (size == i + 1) {
                        ensureCapacity(size + 1);
                        elementData[size][0] = subscriber;
                        levelSize[size++] = 1;
                        return;
                    }
                    temp = elementData[i + 1];
                    int oldCapacity = temp.length;
                    if (temp[oldCapacity - 1] != null) {
                        int newCapacity = oldCapacity << 1;
                        elementData[i + 1] = Arrays.copyOf(elementData[i + 1], newCapacity);
                    }
                    elementData[i + 1][levelSize[i + 1]] = subscriber;
                    levelSize[i + 1]++;
                    return;
                }
            }
        }
        temp = elementData[0];
        int oldCapacity = temp.length;
        if (temp[oldCapacity - 1] != null) {
            int newCapacity = oldCapacity << 1;
            elementData[0] = Arrays.copyOf(elementData[0], newCapacity);
        }
        elementData[0][levelSize[0]] = subscriber;
        levelSize[0]++;
    }
