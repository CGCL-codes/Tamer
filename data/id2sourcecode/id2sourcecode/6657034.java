    public Part[] getPartsOfChannel(int ch) {
        int pchcount = 0;
        Object[] tparr = partList.toArray();
        int[] pcloc = new int[tparr.length];
        Part[] toRetpc;
        for (int i = 0; i < tparr.length; i++) {
            if (((Part) tparr[i]).getChannel() == ch) pcloc[pchcount++] = i;
        }
        toRetpc = new Part[pchcount];
        for (int i = 0; i < toRetpc.length; i++) {
            toRetpc[i] = ((Part) tparr[pcloc[i]]);
        }
        return toRetpc;
    }
