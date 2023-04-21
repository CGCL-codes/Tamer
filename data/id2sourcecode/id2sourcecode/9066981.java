    public void computeVisibleRange(MediaFeed feed, LayoutInterface layout, Vector3f deltaAnchorPositionIn, IndexRange outVisibleRange, IndexRange outBufferedVisibleRange, IndexRange outCompleteRange, int state) {
        GridCamera camera = mCamera;
        Pool<Vector3f> pool = sPool;
        float offset = (camera.mLookAtX * camera.mScale);
        int itemWidth = camera.mItemWidth;
        float maxIncrement = camera.mWidth * 0.5f + itemWidth;
        float left = -maxIncrement + offset;
        float right = left + 2.0f * maxIncrement;
        if (state == GridLayer.STATE_MEDIA_SETS || state == GridLayer.STATE_TIMELINE) {
            right += (itemWidth * 0.5f);
        }
        float top = -maxIncrement;
        float bottom = camera.mHeight + maxIncrement;
        int numSlots = 0;
        if (feed != null) {
            numSlots = feed.getNumSlots();
        }
        synchronized (outCompleteRange) {
            outCompleteRange.set(0, numSlots - 1);
        }
        Vector3f position = pool.create();
        Vector3f deltaAnchorPosition = pool.create();
        try {
            int firstVisibleSlotIndex = 0;
            int lastVisibleSlotIndex = numSlots - 1;
            int leftEdge = firstVisibleSlotIndex;
            int rightEdge = lastVisibleSlotIndex;
            int index = (leftEdge + rightEdge) / 2;
            lastVisibleSlotIndex = firstVisibleSlotIndex;
            deltaAnchorPosition.set(deltaAnchorPositionIn);
            while (index != leftEdge) {
                GridCameraManager.getSlotPositionForSlotIndex(index, camera, layout, deltaAnchorPosition, position);
                if (FloatUtils.boundsContainsPoint(left, right, top, bottom, position.x, position.y)) {
                    firstVisibleSlotIndex = index;
                    lastVisibleSlotIndex = index;
                    break;
                } else {
                    if (position.x > left) {
                        rightEdge = index;
                    } else {
                        leftEdge = index;
                    }
                    index = (leftEdge + rightEdge) / 2;
                }
            }
            while (firstVisibleSlotIndex >= 0 && firstVisibleSlotIndex < numSlots) {
                GridCameraManager.getSlotPositionForSlotIndex(firstVisibleSlotIndex, camera, layout, deltaAnchorPosition, position);
                if (FloatUtils.boundsContainsPoint(left, right, top, bottom, position.x, position.y) == false) {
                    ++firstVisibleSlotIndex;
                    break;
                } else {
                    --firstVisibleSlotIndex;
                }
            }
            while (lastVisibleSlotIndex >= 0 && lastVisibleSlotIndex < numSlots) {
                GridCameraManager.getSlotPositionForSlotIndex(lastVisibleSlotIndex, camera, layout, deltaAnchorPosition, position);
                if (FloatUtils.boundsContainsPoint(left, right, top, bottom, position.x, position.y) == false) {
                    --lastVisibleSlotIndex;
                    break;
                } else {
                    ++lastVisibleSlotIndex;
                }
            }
            if (firstVisibleSlotIndex < 0) firstVisibleSlotIndex = 0;
            if (lastVisibleSlotIndex >= numSlots) lastVisibleSlotIndex = numSlots - 1;
            synchronized (outVisibleRange) {
                outVisibleRange.set(firstVisibleSlotIndex, lastVisibleSlotIndex);
            }
            if (feed != null) {
                feed.setVisibleRange(firstVisibleSlotIndex, lastVisibleSlotIndex);
            }
            final int buffer = 24;
            firstVisibleSlotIndex = ((firstVisibleSlotIndex - buffer) / buffer) * buffer;
            lastVisibleSlotIndex += buffer;
            lastVisibleSlotIndex = (lastVisibleSlotIndex / buffer) * buffer;
            if (firstVisibleSlotIndex < 0) {
                firstVisibleSlotIndex = 0;
            }
            if (lastVisibleSlotIndex >= numSlots) {
                lastVisibleSlotIndex = numSlots - 1;
            }
            synchronized (outBufferedVisibleRange) {
                outBufferedVisibleRange.set(firstVisibleSlotIndex, lastVisibleSlotIndex);
            }
        } finally {
            pool.delete(position);
            pool.delete(deltaAnchorPosition);
        }
    }
