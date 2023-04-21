    @Override
    public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
        paint.setAntiAlias(mRenderer.isAntialiasing());
        paint.setStyle(Style.FILL);
        paint.setTextSize(mRenderer.getLabelsTextSize());
        int legendSize = getLegendSize(mRenderer, height / 5, 0);
        int left = x;
        int top = y;
        int right = x + width;
        int sLength = mDataset.getItemCount();
        double total = 0;
        String[] titles = new String[sLength];
        for (int i = 0; i < sLength; i++) {
            total += mDataset.getValue(i);
            titles[i] = mDataset.getCategory(i);
        }
        if (mRenderer.isFitLegend()) {
            legendSize = drawLegend(canvas, mRenderer, titles, left, right, y, width, height, legendSize, paint, true);
        }
        int bottom = y + height - legendSize;
        drawBackground(mRenderer, canvas, x, y, width, height, paint, false, DefaultRenderer.NO_COLOR);
        float currentAngle = 0;
        int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
        int radius = (int) (mRadius * 0.35 * mRenderer.getScale());
        if (mCenterX == NO_VALUE) {
            mCenterX = (left + right) / 2;
        }
        if (mCenterY == NO_VALUE) {
            mCenterY = (bottom + top) / 2;
        }
        mPieMapper.setDimensions(radius, mCenterX, mCenterY);
        boolean loadPieCfg = !mPieMapper.areAllSegmentPresent(sLength);
        if (loadPieCfg) {
            mPieMapper.clearPieSegments();
        }
        float shortRadius = radius * 0.9f;
        float longRadius = radius * 1.1f;
        RectF oval = new RectF(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
        List<RectF> prevLabelsBounds = new ArrayList<RectF>();
        for (int i = 0; i < sLength; i++) {
            paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
            float value = (float) mDataset.getValue(i);
            float angle = (float) (value / total * 360);
            canvas.drawArc(oval, currentAngle, angle, true, paint);
            drawLabel(canvas, mDataset.getCategory(i), mRenderer, prevLabelsBounds, mCenterX, mCenterY, shortRadius, longRadius, currentAngle, angle, left, right, mRenderer.getLabelsColor(), paint);
            if (loadPieCfg) {
                mPieMapper.addPieSegment(i, value, currentAngle, angle);
            }
            currentAngle += angle;
        }
        prevLabelsBounds.clear();
        drawLegend(canvas, mRenderer, titles, left, right, y, width, height, legendSize, paint, false);
        drawTitle(canvas, x, y, width, paint);
    }
