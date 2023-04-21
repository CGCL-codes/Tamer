    public int findInOriginalTable(int index, String res, int handle) {
        int result = -1;
        if (res != null && wts != null && index > -1) {
            WordTable wt = wts.get(index);
            if (wt != null && wt.getCount() > 0) {
                int start = 0;
                int end = wt.getCount() - 1;
                int mid = (end + start) / 2;
                ArrayList<WordItem> wis = wt.getWords();
                while (start <= end) {
                    WordItem wi = wis.get(mid);
                    int cmpValue = GFString.compareTo(wi.getWord(), res);
                    if (cmpValue == 0 && (wi.getHandle() == handle || handle == -1)) {
                        if (handle == -1) {
                            while (mid >= 0 && res.compareTo(wis.get(mid).getWord()) == 0) {
                                mid--;
                            }
                            if (mid < 0 || res.compareTo(wis.get(mid).getWord()) != 0) mid++;
                        }
                        result = mid;
                        return result;
                    } else if (cmpValue < 0 || cmpValue == 0 && wi.getHandle() < handle && handle != -1) start = mid + 1; else if (cmpValue > 0 || cmpValue == 0 && wi.getHandle() > handle && handle != -1) end = mid - 1;
                    mid = (start + end) / 2;
                }
            }
        }
        return result;
    }
