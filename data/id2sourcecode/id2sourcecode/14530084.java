    protected static String createLabel(GC gc, String strText, int nWidth) {
        if (strText == null) return null;
        final int nExtent = gc.textExtent(strText).x;
        if (nExtent > nWidth) {
            final int w = gc.textExtent(ELLIPSIS).x;
            if (nWidth <= w) return strText;
            final int l = strText.length();
            int nMax = l / 2;
            int nMin = 0;
            int nAvg = (nMax + nMin) / 2 - 1;
            if (nAvg <= 0) return strText;
            while (nMin < nAvg && nAvg < nMax) {
                final String s1 = strText.substring(0, nAvg);
                final String s2 = strText.substring(l - nAvg, l);
                final int l1 = gc.textExtent(s1).x;
                final int l2 = gc.textExtent(s2).x;
                if (l1 + w + l2 > nWidth) {
                    nMax = nAvg;
                    nAvg = (nMax + nMin) / 2;
                } else if (l1 + w + l2 < nWidth) {
                    nMin = nAvg;
                    nAvg = (nMax + nMin) / 2;
                } else nMin = nMax;
            }
            if (nAvg == 0) return strText;
            return strText.substring(0, nAvg) + ELLIPSIS + strText.substring(l - nAvg, l);
        }
        return strText;
    }
