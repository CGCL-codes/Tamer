    @SuppressWarnings("null")
    private static AreaNode splitNode(AreaNode node) {
        AreaObject oldStorage[] = new AreaObject[MAX_PER_NODE + 1];
        int oldStorageCount = node.mStorageCount;
        int largestWasted = -1;
        AreaObject firstShape = null;
        AreaObject secondShape = null;
        boolean addToFirst = true;
        AreaNode split;
        int i;
        Rectangle bounds;
        if (oldStorageCount > 0) {
            System.arraycopy(node.mStorage, 0, oldStorage, 0, node.mStorageCount);
        }
        split = new AreaNode(node.mParent, node.mLeafNode);
        for (i = 0; i < oldStorageCount - 1; i++) {
            for (int j = i + 1; j < oldStorageCount; j++) {
                int wasted = areaWasted(oldStorage[i].getBounds(), oldStorage[j].getBounds());
                if (wasted >= largestWasted) {
                    largestWasted = wasted;
                    firstShape = oldStorage[i];
                    secondShape = oldStorage[j];
                }
            }
        }
        while (node.mStorageCount > 0) {
            node.mStorage[--node.mStorageCount] = null;
        }
        node.mStorage[node.mStorageCount++] = firstShape;
        bounds = firstShape.getBounds();
        node.mBounds.x = bounds.x;
        node.mBounds.y = bounds.y;
        node.mBounds.width = bounds.width;
        node.mBounds.height = bounds.height;
        oldStorageCount = removeFromArray(oldStorageCount, oldStorage, firstShape);
        if (!node.mLeafNode) {
            ((AreaNode) firstShape).mParent = node;
        }
        split.mStorage[split.mStorageCount++] = secondShape;
        oldStorageCount = removeFromArray(oldStorageCount, oldStorage, secondShape);
        bounds = secondShape.getBounds();
        split.mBounds.x = bounds.x;
        split.mBounds.y = bounds.y;
        split.mBounds.width = bounds.width;
        split.mBounds.height = bounds.height;
        if (!split.mLeafNode) {
            ((AreaNode) secondShape).mParent = split;
        }
        while (oldStorageCount > 0) {
            if (addToFirst) {
                oldStorageCount = node.transferFromLargestOverlap(oldStorageCount, oldStorage);
                addToFirst = false;
            } else {
                oldStorageCount = split.transferFromLargestOverlap(oldStorageCount, oldStorage);
                addToFirst = true;
            }
        }
        return split;
    }
