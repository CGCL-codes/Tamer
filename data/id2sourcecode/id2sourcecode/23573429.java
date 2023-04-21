    public int getPositionFromCm(int chromosome, double cM) {
        int end = chrend[chromosome - 1];
        int start;
        if (chromosome == 1) start = 0; else start = chrend[chromosome - 2] + 1;
        if (cM < markercm[start]) {
            double p2gRatio = (markerPosition[start + 1] - markerPosition[start]) / (markercm[start + 1] - markercm[start]);
            return markerPosition[start] - (int) Math.round((markercm[start] - cM) * p2gRatio);
        }
        if (cM > markercm[end]) {
            double p2gRatio = (markerPosition[end] - markerPosition[end - 1]) / (markercm[end] - markercm[end - 1]);
            return markerPosition[end] + (int) Math.round((cM - markercm[end]) * p2gRatio);
        }
        if (cM == markercm[start]) return markerPosition[start];
        if (cM == markercm[end]) return markerPosition[end];
        int left = start;
        int right = end;
        while (right - left > 1) {
            int mid = left + (right - left) / 2;
            if (cM == markercm[mid]) return markerPosition[mid];
            if (cM < markercm[mid]) right = mid; else left = mid;
        }
        double p2gRatio = (markerPosition[right] - markerPosition[left]) / (markercm[right] - markercm[left]);
        return markerPosition[left] + (int) Math.round((cM - markercm[left]) * p2gRatio);
    }
