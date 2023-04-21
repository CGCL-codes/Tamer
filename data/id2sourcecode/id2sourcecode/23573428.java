    public double getCmFromPosition(int chromosome, int position) {
        int end = chrend[chromosome - 1];
        int start;
        if (chromosome == 1) start = 0; else start = chrend[chromosome - 2] + 1;
        if (position < markerPosition[start]) {
            double g2pRatio = (markercm[start + 1] - markercm[start]) / (markerPosition[start + 1] - markerPosition[start]);
            return markercm[start] - (markerPosition[start] - position) * g2pRatio;
        }
        if (position > markerPosition[end]) {
            double g2pRatio = (markercm[end] - markercm[end - 1]) / (markerPosition[end] - markerPosition[end - 1]);
            return markercm[end] + (position - markerPosition[end]) * g2pRatio;
        }
        if (position == markerPosition[start]) return markercm[start];
        if (position == markerPosition[end]) return markercm[end];
        int left = start;
        int right = end;
        while (right - left > 1) {
            int mid = left + (right - left) / 2;
            if (position == markerPosition[mid]) return markercm[mid];
            if (position < markerPosition[mid]) right = mid; else left = mid;
        }
        double g2pRatio = (markercm[right] - markercm[left]) / (markerPosition[right] - markerPosition[left]);
        return markercm[left] + (position - markerPosition[left]) * g2pRatio;
    }
