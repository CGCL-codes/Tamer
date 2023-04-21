    protected int followingAnnotation(int idx) {
        if (annotationFollowing[idx] == NOT_CALCULATED) {
            long endOffset = annotation[idx].getEndNode().getOffset();
            int from = idx;
            int to = annotation.length - 1;
            while (from <= to) {
                int mid = (from + to) / 2;
                long midStartOffset = annotation[mid].getStartNode().getOffset();
                if (midStartOffset > endOffset) {
                    while (mid > from && annotationNextOffset[mid - 1] == annotationNextOffset[mid]) {
                        mid--;
                    }
                    if (annotation[mid - 1].getStartNode().getOffset() < endOffset) {
                        annotationFollowing[idx] = mid;
                        break;
                    } else {
                        to = mid - 1;
                    }
                } else if (midStartOffset < endOffset) {
                    mid = annotationNextOffset[mid];
                    if (mid > annotation.length) {
                        annotationFollowing[idx] = Integer.MAX_VALUE;
                        break;
                    } else if (annotation[mid].getStartNode().getOffset() > endOffset) {
                        annotationFollowing[idx] = mid;
                        break;
                    } else {
                        from = mid;
                    }
                } else {
                    while (mid > from && annotation[mid - 1].getStartNode().getOffset() == midStartOffset) {
                        mid--;
                    }
                    annotationFollowing[idx] = mid;
                    break;
                }
            }
            if (annotationFollowing[idx] == NOT_CALCULATED) {
                if (from < annotation.length) {
                    annotationFollowing[idx] = from;
                } else {
                    annotationFollowing[idx] = Integer.MAX_VALUE;
                }
            }
        }
        return annotationFollowing[idx];
    }
