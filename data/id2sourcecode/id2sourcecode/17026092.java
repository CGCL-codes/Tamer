    private Rect touchMove(MotionEvent event) {
        Rect areaToRefresh = null;
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;
        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);
        if (dx >= GestureStroke.TOUCH_TOLERANCE || dy >= GestureStroke.TOUCH_TOLERANCE) {
            areaToRefresh = mInvalidRect;
            final int border = mInvalidateExtraBorder;
            areaToRefresh.set((int) mCurveEndX - border, (int) mCurveEndY - border, (int) mCurveEndX + border, (int) mCurveEndY + border);
            float cX = mCurveEndX = (x + previousX) / 2;
            float cY = mCurveEndY = (y + previousY) / 2;
            mPath.quadTo(previousX, previousY, cX, cY);
            areaToRefresh.union((int) previousX - border, (int) previousY - border, (int) previousX + border, (int) previousY + border);
            areaToRefresh.union((int) cX - border, (int) cY - border, (int) cX + border, (int) cY + border);
            mX = x;
            mY = y;
            mStrokeBuffer.add(new GesturePoint(x, y, event.getEventTime()));
            if (mHandleGestureActions && !mIsGesturing) {
                mTotalLength += (float) Math.sqrt(dx * dx + dy * dy);
                if (mTotalLength > mGestureStrokeLengthThreshold) {
                    final OrientedBoundingBox box = GestureUtilities.computeOrientedBoundingBox(mStrokeBuffer);
                    float angle = Math.abs(box.orientation);
                    if (angle > 90) {
                        angle = 180 - angle;
                    }
                    if (box.squareness > mGestureStrokeSquarenessTreshold || (mOrientation == ORIENTATION_VERTICAL ? angle < mGestureStrokeAngleThreshold : angle > mGestureStrokeAngleThreshold)) {
                        mIsGesturing = true;
                        setCurrentColor(mCertainGestureColor);
                        final ArrayList<OnGesturingListener> listeners = mOnGesturingListeners;
                        int count = listeners.size();
                        for (int i = 0; i < count; i++) {
                            listeners.get(i).onGesturingStarted(this);
                        }
                    }
                }
            }
            final ArrayList<OnGestureListener> listeners = mOnGestureListeners;
            final int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.get(i).onGesture(this, event);
            }
        }
        return areaToRefresh;
    }
