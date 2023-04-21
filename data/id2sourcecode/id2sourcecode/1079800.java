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
        String[] titles = new String[sLength];
        for (int i = 0; i < sLength; i++) {
            titles[i] = mDataset.getCategory(i);
        }
        if (mRenderer.isFitLegend()) {
            legendSize = drawLegend(canvas, mRenderer, titles, left, right, y, width, height, legendSize, paint, true);
        }
        int bottom = y + height - legendSize;
        drawBackground(mRenderer, canvas, x, y, width, height, paint, false, DefaultRenderer.NO_COLOR);
        int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
        int radius = (int) (mRadius * 0.35 * mRenderer.getScale());
        if (mCenterX == NO_VALUE) {
            mCenterX = (left + right) / 2;
        }
        if (mCenterY == NO_VALUE) {
            mCenterY = (bottom + top) / 2;
        }
        float shortRadius = radius * 0.9f;
        float longRadius = radius * 1.1f;
        double min = mRenderer.getMinValue();
        double max = mRenderer.getMaxValue();
        double angleMin = mRenderer.getAngleMin();
        double angleMax = mRenderer.getAngleMax();
        if (!mRenderer.isMinValueSet() || !mRenderer.isMaxValueSet()) {
            int count = mRenderer.getSeriesRendererCount();
            for (int i = 0; i < count; i++) {
                double value = mDataset.getValue(i);
                if (!mRenderer.isMinValueSet()) {
                    min = Math.min(min, value);
                }
                if (!mRenderer.isMaxValueSet()) {
                    max = Math.max(max, value);
                }
            }
        }
        if (min == max) {
            min = min * 0.5;
            max = max * 1.5;
        }
        paint.setColor(mRenderer.getLabelsColor());
        double minorTicks = mRenderer.getMinorTicksSpacing();
        double majorTicks = mRenderer.getMajorTicksSpacing();
        if (minorTicks == MathHelper.NULL_VALUE) {
            minorTicks = (max - min) / 30;
        }
        if (majorTicks == MathHelper.NULL_VALUE) {
            majorTicks = (max - min) / 10;
        }
        drawTicks(canvas, min, max, angleMin, angleMax, mCenterX, mCenterY, longRadius, radius, minorTicks, paint, false);
        drawTicks(canvas, min, max, angleMin, angleMax, mCenterX, mCenterY, longRadius, shortRadius, majorTicks, paint, true);
        int count = mRenderer.getSeriesRendererCount();
        for (int i = 0; i < count; i++) {
            double angle = getAngleForValue(mDataset.getValue(i), angleMin, angleMax, min, max);
            paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
            boolean type = mRenderer.getVisualTypeForIndex(i) == Type.ARROW;
            drawNeedle(canvas, angle, mCenterX, mCenterY, shortRadius, type, paint);
        }
        drawLegend(canvas, mRenderer, titles, left, right, y, width, height, legendSize, paint, false);
        drawTitle(canvas, x, y, width, paint);
    }
