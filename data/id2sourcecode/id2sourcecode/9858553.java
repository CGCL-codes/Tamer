    public Extent scale(double s) {
        double mid = (end + start) / 2;
        double half = s * (end - start) / 2;
        return Extent.of(mid - half, mid + half);
    }
