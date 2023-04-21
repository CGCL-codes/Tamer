    public static int getNextPointData(Vector<PointData> data, AbsTime ts) {
        synchronized (data) {
            System.err.println("Seeking first point after " + ts.toString(AbsTime.Format.UTC_STRING));
            for (int i = 0; i < data.size(); i++) {
            }
            int fullsize = data.size();
            if (data.get(0).getTimestamp().isAfter(ts)) {
                return 0;
            }
            if (data.get(fullsize - 1).getTimestamp().isBeforeOrEquals(ts)) {
                return -1;
            }
            int start = 0;
            int end = fullsize - 1;
            while ((end - start) > 1) {
                int mid = start + (end - start) / 2;
                if (data.get(mid).getTimestamp().isBeforeOrEquals(ts)) {
                    start = mid + 1;
                } else {
                    end = mid;
                }
                System.err.println("Checking span start=" + start + ", end=" + end);
            }
            if (data.get(start).getTimestamp().isBeforeOrEquals(ts)) {
                start++;
            }
            System.err.println("Found result at " + start + " " + data.get(start).getTimestamp().toString(AbsTime.Format.UTC_STRING));
            return start;
        }
    }
