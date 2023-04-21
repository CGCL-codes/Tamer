    public static String createLabel(String text, GC gc, int width) {
        if (text == null) return null;
        final int extent = gc.textExtent(text).x;
        if (extent > width) {
            final int w = gc.textExtent(ELLIPSIS).x;
            if (width <= w) {
                return text;
            }
            final int l = text.length();
            int max = l / 2;
            int min = 0;
            int mid = (max + min) / 2 - 1;
            if (mid <= 0) {
                return text;
            }
            while (min < mid && mid < max) {
                final String s1 = text.substring(0, mid);
                final String s2 = text.substring(l - mid, l);
                final int l1 = gc.textExtent(s1).x;
                final int l2 = gc.textExtent(s2).x;
                if (l1 + w + l2 > width) {
                    max = mid;
                    mid = (max + min) / 2;
                } else if (l1 + w + l2 < width) {
                    min = mid;
                    mid = (max + min) / 2;
                } else {
                    min = max;
                }
            }
            if (mid == 0) {
                return text;
            }
            String result = text.substring(0, mid) + ELLIPSIS + text.substring(l - mid, l);
            return result;
        } else {
            return text;
        }
    }
