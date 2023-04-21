    private void computeLabels() {
        int iVisibleWidth = (int) visibleWidth();
        labelXs = new Perspective[iVisibleWidth + 1];
        double divisor = barWidthRatio();
        Perspective[] restrictions = p.allRestrictions().toArray(new Perspective[0]);
        int restrictionBonus = query().getTotalCount();
        boolean isQueryRestricted = query().isRestricted();
        for (Iterator<Perspective> it = visibleChildIterator(); it.hasNext(); ) {
            Perspective child = it.next();
            int totalCount = child.getTotalCount();
            if (totalCount > 0) {
                int iMaxX = maxBarPixel(child, divisor);
                int iMinX = minBarPixel(child, divisor);
                assert iMaxX >= 0 && iMinX <= iVisibleWidth : printBar(child, iMinX, iMaxX, "label");
                int iMidX = (iMaxX + iMinX) / 2;
                if (priorityCount(child, restrictions, isQueryRestricted, restrictionBonus) > itemOnCount(iMidX, restrictions, isQueryRestricted, restrictionBonus)) labelXs[iMidX] = child;
            }
        }
        drawComputedLabel(null, divisor, restrictions, isQueryRestricted, restrictionBonus);
    }
