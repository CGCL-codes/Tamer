    public final int getElementIndexBeforeDiscoursePosition(int discPos) {
        int low = 0;
        int hi = size() - 1;
        int midpt = 0;
        int temp = 0;
        while (low <= hi) {
            midpt = (low + hi) / 2;
            if (isIndexSetByUser()) {
                temp = getElementAtIndexFromColumnToUse(midpt).getLeftmostDiscoursePosition();
            } else {
                temp = getTupleAtIndex(midpt).getLeftmostDiscoursePosition();
            }
            if (discPos == temp) {
                if (isIndexSetByUser()) {
                    int tempMidPt = midpt;
                    while (tempMidPt > 0 && getElementAtIndexFromColumnToUse(tempMidPt).getLeftmostDiscoursePosition() == temp) {
                        tempMidPt--;
                    }
                    midpt = tempMidPt;
                } else {
                    int tempMidPt = midpt;
                    while (tempMidPt > 0 && getTupleAtIndex(tempMidPt).getLeftmostDiscoursePosition() == temp) {
                        tempMidPt--;
                    }
                    midpt = tempMidPt;
                }
                return midpt;
            } else if (discPos < temp) {
                hi = midpt - 1;
            } else {
                low = midpt + 1;
            }
        }
        if (isIndexSetByUser()) {
            int tempMidPt = midpt;
            while (tempMidPt > 0 && getElementAtIndexFromColumnToUse(tempMidPt).getLeftmostDiscoursePosition() == temp) {
                tempMidPt--;
            }
            midpt = tempMidPt;
        } else {
            int tempMidPt = midpt;
            while (tempMidPt > 0 && getTupleAtIndex(tempMidPt).getLeftmostDiscoursePosition() == temp) {
                tempMidPt--;
            }
            midpt = tempMidPt;
        }
        return midpt;
    }
