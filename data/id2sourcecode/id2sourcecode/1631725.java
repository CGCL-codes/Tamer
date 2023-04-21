    public static boolean[] joinBooleanArrays(boolean[]... bools) {
        if (bools.length == 0) {
            return new boolean[] {};
        }
        if (bools.length == 1) {
            return bools[0];
        } else {
            boolean[] c = joinBooleanArrays(bools[0], bools[1]);
            boolean[][] rek = new boolean[bools.length - 1][];
            rek[0] = c;
            for (int i = 1; i < rek.length; i++) {
                rek[i] = bools[i + 1];
            }
            return joinBooleanArrays(rek);
        }
    }
