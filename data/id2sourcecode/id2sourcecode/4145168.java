    private void matchBlocks(int start1, int start2, int end1, int end2, int lstart1, int lstart2, int lend1, int lend2) {
        long start = System.currentTimeMillis();
        int centerU = (end1 + start1) / 2;
        TreeMap leftU = map(in1, start1, centerU);
        TreeMap rightU = map(in1, centerU, end1);
        int centerL = (end2 + start2) / 2;
        TreeMap leftL = map(in2, start2, centerL);
        TreeMap rightL = map(in2, centerL, end2);
        int s1 = start1;
        int s2 = start2;
        int e1 = centerU;
        int e2 = centerL;
        int error = getError(leftU, leftL);
        int terror = getError(leftU, rightL);
        if (terror < error) {
            error = terror;
            s2 = centerL;
            e2 = end2;
        }
        terror = getError(rightU, leftL);
        if (terror < error) {
            error = terror;
            s1 = centerU;
            e1 = end1;
        }
        terror = getError(rightU, rightL);
        if (terror < error) {
            error = terror;
            s1 = centerU;
            e1 = end1;
            s2 = centerL;
            e2 = end2;
        }
        long cur = System.currentTimeMillis() - start;
        if (needsFastAlgo(e2 - s2, e1 - s1)) matchBlocks(s1, s2, e1, e2, lstart1, lstart2, lend1, lend2); else matchSection(s1, s2, e1, e2, lstart1, lstart2, lend1, lend2);
        if (debug) System.out.println("\tMatchBlocks Delay(" + (end1 - start1) + "~" + (end2 - start2) + "): " + (System.currentTimeMillis() - start) + " - " + cur);
    }
