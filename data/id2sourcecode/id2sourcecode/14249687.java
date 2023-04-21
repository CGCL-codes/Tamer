    public static void addPublisher(IEventPublisher ep, final int publisherSize, final IEventHandler[][] elementData, final IEventHandler[] elements, final int elSize, final int[] levelSize, final int size, final AbstractReferenceSet readwriteset) {
        loadEventFrameState(publisherSize, elementData, elements, elSize, levelSize, size, readwriteset);
        addPublisher(ep);
    }
