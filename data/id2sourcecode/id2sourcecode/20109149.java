    private void initCenters(final Gesture collectGesture) {
        final int min = collectGesture.getMinValue();
        final int max = collectGesture.getMaxValue();
        final int r = (min + max) / 2;
        final double u = Math.cos(Math.PI / 4) * r;
        centers = new HashMap<Integer, double[]>(14);
        centers.put(0, new double[] { r, 0, 0 });
        centers.put(1, new double[] { u, u, 0 });
        centers.put(2, new double[] { 0, r, 0 });
        centers.put(3, new double[] { -u, u, 0 });
        centers.put(4, new double[] { -r, 0, 0 });
        centers.put(5, new double[] { -u, -u, 0 });
        centers.put(6, new double[] { 0, -r, 0 });
        centers.put(7, new double[] { u, -u, 0 });
        centers.put(8, new double[] { 0, 0, r });
        centers.put(9, new double[] { -u, 0, u });
        centers.put(10, new double[] { -u, 0, -u });
        centers.put(11, new double[] { 0, 0, -r });
        centers.put(12, new double[] { u, 0, -u });
        centers.put(13, new double[] { u, 0, u });
    }
