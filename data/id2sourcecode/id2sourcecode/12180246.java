    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float lr_width = width * 0.20F;
        float space = width * 0.03F;
        float outer_radius = Math.min(width, height) * 0.48f;
        float touch_feedback_ring = center_radius + 2 * mCenterPaint.getStrokeWidth();
        mRadius = (outer_radius + touch_feedback_ring) / 2;
        Shader s = new LinearGradient(0, 0, 0, height, mColor1, mColor2, Shader.TileMode.CLAMP);
        mLeftPaint.setShader(s);
        canvas.drawRect(space, 0, lr_width, height, mLeftPaint);
        Shader sr = new LinearGradient(0, 0, 0, height, mColor3, mColor4, Shader.TileMode.CLAMP);
        mRightPaint.setShader(sr);
        canvas.drawRect(width - lr_width, 0, width - space, height, mRightPaint);
        canvas.translate(width / 2, height / 2);
        mPaint.setStrokeWidth(outer_radius - touch_feedback_ring);
        canvas.drawCircle(0, 0, mRadius, mPaint);
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
