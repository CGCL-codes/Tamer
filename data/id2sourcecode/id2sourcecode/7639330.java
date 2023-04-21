    public Object[][] getInterval(int chromosome, int position) {
        int end = chrend[chromosome - 1];
        int start;
        if (chromosome == 1) start = 0; else start = chrend[chromosome - 2] + 1;
        if (position < markerPosition[start]) return new Object[][] { { null, null }, { marker[start], markerPosition[start] } };
        if (position > markerPosition[end]) return new Object[][] { { marker[end], markerPosition[end] }, { null, null } };
        if (position == markerPosition[start]) return new Object[][] { { marker[start], markerPosition[start] }, { marker[start], markerPosition[start] } };
        if (position == markerPosition[end]) return new Object[][] { { marker[end], markerPosition[end] }, { marker[end], markerPosition[end] } };
        int left = start;
        int right = end;
        while (right - left > 1) {
            int mid = left + (right - left) / 2;
            if (position == markerPosition[mid]) return new Object[][] { { marker[mid], markerPosition[mid] }, { marker[mid], markerPosition[mid] } };
            if (position < markerPosition[mid]) right = mid; else left = mid;
        }
        return new Object[][] { { marker[left], markerPosition[left] }, { marker[right], markerPosition[right] } };
    }
