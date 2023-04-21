    public static int getPrevEqualsPointData(Vector<PointData> data, AbsTime ts) {
        synchronized (data) {
            System.err.println("Seeking first point before or equals " + ts.toString(AbsTime.Format.UTC_STRING));
            for (int i = 0; i < data.size(); i++) {
            }
            int fullsize = data.size();
            if (data.get(fullsize - 1).getTimestamp().isBeforeOrEquals(ts)) {
                return fullsize - 1;
            }
            if (data.get(0).getTimestamp().isAfter(ts)) {
                return -1;
            }
            int start = 0;
            int end = fullsize - 1;
            while ((end - start) > 1) {
                int mid = start + (end - start) / 2;
                if (data.get(mid).getTimestamp().isBeforeOrEquals(ts)) {
                    start = mid;
                } else {
                    end = mid - 1;
                }
                System.err.println("Checking span start=" + start + ", end=" + end);
            }
            System.err.println("Found result at " + start + " " + data.get(start).getTimestamp().toString(AbsTime.Format.UTC_STRING));
            return start;
        }
    }
