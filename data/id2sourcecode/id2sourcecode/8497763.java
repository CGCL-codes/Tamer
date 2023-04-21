    @Override
    public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
        paint.setAntiAlias(mRenderer.isAntialiasing());
        paint.setStyle(Style.FILL);
        paint.setTextSize(mRenderer.getLabelsTextSize());
        int legendSize = getLegendSize(mRenderer, height / 5, 0);
        int left = x;
        int top = y;
        int right = x + width;
        int cLength = mDataset.getCategoriesCount();
        String[] categories = new String[cLength];
        for (int category = 0; category < cLength; category++) {
            categories[category] = mDataset.getCategory(category);
        }
        if (mRenderer.isFitLegend()) {
            legendSize = drawLegend(canvas, mRenderer, categories, left, right, y, width, height, legendSize, paint, true);
        }
        int bottom = y + height - legendSize;
        drawBackground(mRenderer, canvas, x, y, width, height, paint, false, DefaultRenderer.NO_COLOR);
        mStep = SHAPE_WIDTH * 3 / 4;
        int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
        double rCoef = 0.35 * mRenderer.getScale();
        double decCoef = 0.2 / cLength;
        int radius = (int) (mRadius * rCoef);
        if (mCenterX == NO_VALUE) {
            mCenterX = (left + right) / 2;
        }
        if (mCenterY == NO_VALUE) {
            mCenterY = (bottom + top) / 2;
        }
        float shortRadius = radius * 0.9f;
        float longRadius = radius * 1.1f;
        List<RectF> prevLabelsBounds = new ArrayList<RectF>();
        for (int category = 0; category < cLength; category++) {
            int sLength = mDataset.getItemCount(category);
            double total = 0;
            String[] titles = new String[sLength];
            for (int i = 0; i < sLength; i++) {
                total += mDataset.getValues(category)[i];
                titles[i] = mDataset.getTitles(category)[i];
            }
            float currentAngle = mRenderer.getStartAngle();
            RectF oval = new RectF(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
            for (int i = 0; i < sLength; i++) {
                paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
                float value = (float) mDataset.getValues(category)[i];
                float angle = (float) (value / total * 360);
                canvas.drawArc(oval, currentAngle, angle, true, paint);
                drawLabel(canvas, mDataset.getTitles(category)[i], mRenderer, prevLabelsBounds, mCenterX, mCenterY, shortRadius, longRadius, currentAngle, angle, left, right, mRenderer.getLabelsColor(), paint);
                currentAngle += angle;
            }
            radius -= (int) mRadius * decCoef;
            shortRadius -= mRadius * decCoef - 2;
            if (mRenderer.getBackgroundColor() != 0) {
                paint.setColor(mRenderer.getBackgroundColor());
            } else {
                paint.setColor(Color.WHITE);
            }
            paint.setStyle(Style.FILL);
            oval = new RectF(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
            canvas.drawArc(oval, 0, 360, true, paint);
            radius -= 1;
        }
        prevLabelsBounds.clear();
        drawLegend(canvas, mRenderer, categories, left, right, y, width, height, legendSize, paint, false);
        drawTitle(canvas, x, y, width, paint);
    }
