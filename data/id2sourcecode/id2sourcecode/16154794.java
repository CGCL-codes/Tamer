    private CPResult getClosestPointsDC(DPoint[] x, DPoint[] y, DPoint[] z, int start, int end) {
        CPResult re;
        if (start + 1 == end) {
            re = new CPResult();
            re.p1 = x[start];
            re.p2 = x[end];
            re.dist = re.p1.distTo(re.p2);
            return re;
        } else if (start + 2 == end) {
            re = new CPResult();
            double d1, d2, d3;
            d1 = x[start].distTo(x[start + 1]);
            d2 = x[start].distTo(x[end]);
            d3 = x[start + 1].distTo(x[end]);
            if (d1 <= d2 && d1 <= d3) {
                re.p1 = x[start];
                re.p2 = x[start + 1];
                re.dist = d1;
            } else if (d2 <= d1 && d2 <= d3) {
                re.p1 = x[start];
                re.p2 = x[end];
                re.dist = d2;
            } else if (d3 <= d1 && d3 <= d2) {
                re.p1 = x[start + 1];
                re.p2 = x[end];
                re.dist = d3;
            }
            return re;
        }
        int mid = (start + end) / 2;
        int low = start;
        int up = mid + 1;
        for (int i = start; i <= end; ++i) {
            if (y[i].xIndex > mid) {
                z[up++] = y[i];
            } else {
                z[low++] = y[i];
            }
        }
        for (int i = start; i <= end; ++i) {
            y[i] = z[i];
        }
        CPResult re1 = getClosestPointsDC(x, z, y, start, mid);
        CPResult re2 = getClosestPointsDC(x, z, y, mid + 1, end);
        if (re1.dist < re2.dist) {
            re = re1;
        } else {
            re = re2;
        }
        this.merge(z, y, start, mid, end);
        int k = start;
        for (int i = start; i <= end; ++i) {
            if (Math.abs(x[mid].x - y[i].x) < re.dist) {
                z[k++] = y[i];
            }
        }
        double tmpDist;
        for (int i = start; i < k; ++i) {
            for (int j = i + 1; j < k && z[j].y - z[i].y < re.dist; ++j) {
                tmpDist = z[i].distTo(z[j]);
                if (tmpDist < re.dist) {
                    re.dist = tmpDist;
                    re.p1 = z[i];
                    re.p2 = z[j];
                }
            }
        }
        return re;
    }
