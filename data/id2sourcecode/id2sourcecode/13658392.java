    public void transpose() {
        int temp;
        if (bounds != null) bounds.transpose();
        for (int i = 0; i < size * 2; i += 2) {
            temp = points[i];
            points[i] = points[i + 1];
            points[i + 1] = temp;
        }
    }
