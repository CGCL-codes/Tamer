    private int findSpan(float u) {
        int low, high, mid;
        if (u >= knots[numControlPoints]) return (numControlPoints - 1);
        low = order - 1;
        high = numControlPoints;
        mid = (low + high) / 2;
        while (u < knots[mid] || u >= knots[mid + 1]) {
            if (u < knots[mid]) high = mid; else low = mid;
            mid = (low + high) / 2;
        }
        return (mid);
    }
