    private int getPositionAtOffset(int offset) {
        int start = 0;
        int end = positionCount - 1;
        PosBottomHalf bh;
        loop: for (; ; ) {
            switch(end - start) {
                case 0:
                    bh = positions[start];
                    if (bh.offset < offset) start++;
                    break loop;
                case 1:
                    bh = positions[end];
                    if (bh.offset < offset) {
                        start = end + 1;
                    } else {
                        bh = positions[start];
                        if (bh.offset < offset) {
                            start++;
                        }
                    }
                    break loop;
                default:
                    int pivot = (start + end) / 2;
                    bh = positions[pivot];
                    if (bh.offset > offset) end = pivot - 1; else start = pivot + 1;
                    break;
            }
        }
        return start;
    }
