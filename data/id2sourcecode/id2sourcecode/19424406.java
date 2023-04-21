    private static int getTargetSegmentOfBlossomingAtDegreeElevation(double targetParams[], int oldDegree, BsplineKnotVector.ValidSegmentInfo oldValidSegments) {
        int oldNValidSegments = oldValidSegments.nSegments();
        int lowerSegmentIndex = -1;
        for (int k = 0; k < oldNValidSegments; k++) {
            if (!(targetParams[0] > oldValidSegments.tailKnotPoint(k))) {
                lowerSegmentIndex = k;
                break;
            }
        }
        int upperSegmentIndex = oldNValidSegments;
        for (int k = (oldNValidSegments - 1); k >= 0; k--) {
            if (!(oldValidSegments.headKnotPoint(k) > targetParams[oldDegree])) {
                upperSegmentIndex = k;
                break;
            }
        }
        int targetSegment;
        if (lowerSegmentIndex == (-1)) {
            targetSegment = oldValidSegments.segmentNumber(upperSegmentIndex);
        } else if (upperSegmentIndex == oldNValidSegments) {
            targetSegment = oldValidSegments.segmentNumber(lowerSegmentIndex);
        } else {
            if (targetParams[0] < oldValidSegments.headKnotPoint(lowerSegmentIndex)) {
                targetSegment = oldValidSegments.segmentNumber(lowerSegmentIndex);
            } else if (targetParams[oldDegree] > oldValidSegments.tailKnotPoint(upperSegmentIndex)) {
                targetSegment = oldValidSegments.segmentNumber(upperSegmentIndex);
            } else {
                int targetSegmentIndex = (lowerSegmentIndex + upperSegmentIndex) / 2;
                targetSegment = oldValidSegments.segmentNumber(targetSegmentIndex);
                if (targetSegmentIndex != (oldNValidSegments - 1)) {
                    targetSegmentIndex++;
                    double diff1 = Math.abs(targetParams[0] - oldValidSegments.headKnotPoint(targetSegmentIndex));
                    double diff2 = Math.abs(targetParams[oldDegree] - oldValidSegments.tailKnotPoint(targetSegmentIndex));
                    double pTol = ConditionOfOperation.getCondition().getToleranceForParameter();
                    if ((diff1 < pTol) && (diff2 < pTol)) targetSegment = oldValidSegments.segmentNumber(targetSegmentIndex);
                }
            }
        }
        return targetSegment;
    }
