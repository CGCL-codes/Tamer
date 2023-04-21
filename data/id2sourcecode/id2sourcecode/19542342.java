    private void matchTwo(int[][] uPoint, int[][] lPoint, int start1, int start2, int end1, int end2) {
        long start = System.currentTimeMillis();
        int bfor = 0;
        int bsec = 0;
        int weight = 0;
        boolean fast = needsFastAlgo(end1 - start1, end2 - start2);
        int center1 = (end1 + start1) / 2;
        int center2 = (end2 + start2) / 2;
        int offset = center1 + center2;
        for (int i = 0; i < uBinArray.length && processing; i++) {
            int s1 = uPoint[i][0];
            int s2 = lPoint[i][0];
            int e1 = uPoint[i][1];
            int e2 = lPoint[i][1];
            if (fast) {
                while (s1 < e1 && s2 < e2 && processing) {
                    int i1 = uBinArray[i][s1];
                    int i2 = lBinArray[i][s2];
                    s1++;
                    s2++;
                    int mw = 1;
                    int k = i1 + 1;
                    int l = i2 + 1;
                    while (k < end1 && l < end2 && !upperDelim[k] && !lowerDelim[l] && in1[k] == in2[l] && processing) {
                        mw++;
                        k++;
                        l++;
                    }
                    k = i1 - 1;
                    l = i2 - 1;
                    while (k >= start1 && l >= start2 && !upperDelim[k] && !lowerDelim[l] && in1[k] == in2[l] && processing) {
                        mw++;
                        k--;
                        l--;
                    }
                    i1 = k + 1;
                    i2 = l + 1;
                    if (mw >= weight && mw >= ignoreWeight) {
                        int coffset = Math.abs(center1 - i1) + Math.abs(center2 - i2);
                        if (mw > weight || coffset < offset) {
                            bfor = i1;
                            bsec = i2;
                            weight = mw;
                            offset = coffset;
                        }
                    }
                }
            } else {
                for (int j = s1; j < e1 && processing; j++) {
                    int i1 = uBinArray[i][j];
                    for (int k = s2; k < e2 && processing; k++) {
                        int i2 = lBinArray[i][k];
                        int mw = 1;
                        int l = i1 + 1;
                        int m = i2 + 1;
                        while (l < end1 && m < end2 && !upperDelim[l] && !lowerDelim[m] && in1[l] == in2[m] && processing) {
                            mw++;
                            l++;
                            m++;
                        }
                        if (mw >= weight && mw >= ignoreWeight) {
                            int coffset = Math.abs(center1 - i1) + Math.abs(center2 - i2);
                            if (mw > weight || coffset < offset) {
                                bfor = i1;
                                bsec = i2;
                                weight = mw;
                                offset = coffset;
                            }
                        }
                    }
                }
            }
        }
        if (weight != 0 && processing) {
            int comp1 = bfor + weight;
            int comp2 = bsec + weight;
            int[][] uPointFront = new int[uPoint.length][2];
            int[][] lPointFront = new int[lPoint.length][2];
            int[][] uPointBack = new int[uPoint.length][2];
            int[][] lPointBack = new int[lPoint.length][2];
            trimPointArray(uPoint, uPointFront, uPointBack, uBinArray, bfor, comp1);
            trimPointArray(lPoint, lPointFront, lPointBack, lBinArray, bsec, comp2);
            uPoint = null;
            lPoint = null;
            if (bfor > start1 && bsec > start2 && processing) matchTwo(uPointFront, lPointFront, start1, start2, bfor, bsec);
            uPointFront = null;
            lPointFront = null;
            firstMatch.add(new Integer(bfor));
            secondMatch.add(new Integer(bsec));
            weights.add(new Integer(weight));
            if (comp1 < end1 && comp2 < end2 && processing) matchTwo(uPointBack, lPointBack, comp1, comp2, end1, end2);
            uPointBack = null;
            lPointBack = null;
        }
        if (debug) System.out.println("\tMatchTwo Delay(" + (end1 - start1 > end2 - start2 ? end1 - start1 : end2 - start2) + "): " + (System.currentTimeMillis() - start));
    }
