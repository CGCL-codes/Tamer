    private static void loadEventFrameState(final int apublisherSize, final IEventHandler[][] aelementData, final IEventHandler[] aelements, final int aelSize, final int[] alevelSize, final int asize, final AbstractReferenceSet areadwriteset) {
        publisherSize = apublisherSize;
        elementData = aelementData;
        elements = aelements;
        elSize = aelSize;
        levelSize = alevelSize;
        readwriteset = areadwriteset;
        size = asize;
        if (readwriteset == null) {
            readwriteset = new AbstractReferenceSet();
        }
    }
