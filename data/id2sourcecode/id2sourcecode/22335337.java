    private void drawLabels() {
        assert SwingUtilities.isEventDispatchThread();
        assert totalCount > 0;
        labelXs = new Object[iVisibleWidth + 1];
        double divisor = barWidthRatio();
        for (Iterator it = slowVisibleChildIterator(); it.hasNext(); ) {
            Object child = it.next();
            int iMaxX = maxBarPixel(child, divisor);
            int iMinX = minBarPixel(child, divisor);
            assert iMaxX >= 0 && iMinX <= iVisibleWidth : printBar(child, iMinX, iMaxX, "label");
            int iMidX = (iMaxX + iMinX) / 2;
            if (getCount(child) > priority(iMidX)) labelXs[iMidX] = child;
        }
        drawComputedLabel(null, divisor);
    }
