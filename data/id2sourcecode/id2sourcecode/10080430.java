    public static int findNumCharsForWidth(GC gc, String strText, int nWidth) {
        int nEndHigh = strText.length();
        int nEndLow = 0;
        Point pt = gc.textExtent(strText);
        if (pt.x < nWidth) return nEndHigh;
        while (nEndLow + 1 < nEndHigh) {
            int nEndMiddle = (nEndLow + nEndHigh) / 2;
            pt = gc.textExtent(strText.substring(0, nEndMiddle));
            if (pt.x < nWidth) nEndLow = nEndMiddle; else nEndHigh = nEndMiddle;
        }
        return nEndLow;
    }
