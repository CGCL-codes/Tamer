    private int transferFromLargestOverlap(int count, AreaObject[] array) {
        int smallestWasted = Integer.MAX_VALUE;
        int bestPick = -1;
        for (int i = 0; i < count; i++) {
            int wasted = areaWasted(mBounds, array[i].getBounds());
            if (wasted <= smallestWasted) {
                smallestWasted = wasted;
                bestPick = i;
            }
        }
        mBounds.union(array[bestPick].getBounds());
        mStorage[mStorageCount++] = array[bestPick];
        if (!mLeafNode) {
            ((AreaNode) array[bestPick]).mParent = this;
        }
        if (bestPick != --count) {
            array[bestPick] = array[count];
        }
        array[count] = null;
        return count;
    }
