    private int _FindFileNumByNO(DictFileInfo dictFile, boolean bWordFile, long wordNO, int nums) {
        int low, high, cur;
        long val;
        low = 0;
        high = nums;
        cur = (low + high) / 2;
        while (cur != low && cur != high) {
            if (bWordFile) {
                val = dictFile.StartWordNum[cur];
            } else {
                val = dictFile.starContentNum[cur];
            }
            if (val == wordNO) return cur; else if (val > wordNO) high = cur; else low = cur;
            cur = (low + high) / 2;
        }
        return cur;
    }
