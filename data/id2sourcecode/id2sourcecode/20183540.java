    @Override
    protected void onDraw(Canvas canvas) {
        float outer_radius = Math.min(getWidth(), getHeight()) / 2;
        float touch_feedback_ring = center_radius + 2 * mCenterPaint.getStrokeWidth();
        float r = (outer_radius + touch_feedback_ring) / 2;
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPaint.setStrokeWidth(outer_radius - touch_feedback_ring);
        canvas.drawCircle(0, 0, r, mPaint);
        canvas.drawCircle(0, 0, center_radius, mCenterPaint);
        if (mTrackingCenter) {
            int c = mCenterPaint.getColor();
            mCenterPaint.setStyle(Paint.Style.STROKE);
            if (mHighlightCenter) {
                mCenterPaint.setAlpha(0xFF);
            } else {
                mCenterPaint.setAlpha(0x80);
            }
            canvas.drawCircle(0, 0, center_radius + mCenterPaint.getStrokeWidth(), mCenterPaint);
            mCenterPaint.setStyle(Paint.Style.FILL);
            mCenterPaint.setColor(c);
        }
    }
