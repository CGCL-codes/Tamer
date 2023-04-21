    public static final void sort(JPLITheoMSPeak[] peaks, int from, int to) {
        if (from >= to) {
            return;
        }
        int mid = (from + to) / 2;
        JPLITheoMSPeak middle = peaks[mid];
        JPLITheoMSPeak tmp;
        if (peaks[from].compareTo(middle) > 0) {
            peaks[mid] = peaks[from];
            peaks[from] = middle;
            middle = peaks[mid];
        }
        if (middle.compareTo(peaks[to]) > 0) {
            peaks[mid] = peaks[to];
            peaks[to] = middle;
            middle = peaks[mid];
            if (peaks[from].compareTo(middle) > 0) {
                peaks[mid] = peaks[from];
                peaks[from] = middle;
                middle = peaks[mid];
            }
        }
        int left = from + 1;
        int right = to - 1;
        if (left >= right) {
            return;
        }
        for (; ; ) {
            while (peaks[right].compareTo(middle) > 0) {
                right--;
            }
            while (left < right && peaks[left].compareTo(middle) <= 0) {
                left++;
            }
            if (left < right) {
                tmp = peaks[left];
                peaks[left] = peaks[right];
                peaks[right] = tmp;
                right--;
            } else {
                break;
            }
        }
        sort(peaks, from, left);
        sort(peaks, left + 1, to);
    }
