    static boolean pointInConvexPolygon(Point2f p, int n, Point2f[] v) {
        int low = 0, high = n;
        do {
            int mid = (low + high) / 2;
            float area = signed2DTriArea(v[0], v[mid], p);
            if (area > 0) low = mid; else if (area < 0) high = mid; else {
                if (mid == 1) {
                    low = 1;
                    high = 2;
                } else {
                    high = mid;
                    low = mid - 1;
                }
            }
        } while (low + 1 < high);
        if (low == 0 || high == n) return false;
        return signed2DTriArea(v[low], v[high], p) >= 0;
    }
