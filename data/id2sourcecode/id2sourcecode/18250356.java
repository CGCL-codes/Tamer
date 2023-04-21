    private double getSizeMetric() {
        Map<Double, Integer> rasp = new HashMap<Double, Integer>();
        List<ITriangle> allTriangles = model.getTriangles();
        for (ITriangle triang : allTriangles) {
            double curS = TriangMath.getSquare(triang);
            double key = 0;
            for (double s : rasp.keySet()) {
                double del = eps * s;
                if (s - del < curS && curS < s + del) {
                    key = s;
                    break;
                }
            }
            int count = 1;
            if (key != 0) {
                count = rasp.get(key) + 1;
                rasp.remove(key);
                key = (key + curS) / 2;
            } else {
                key = curS;
            }
            rasp.put(key, count);
        }
        double bestS = 0;
        int maxCount = 0;
        for (double s : rasp.keySet()) {
            int curCount = rasp.get(s);
            if (curCount > maxCount) {
                bestS = s;
                maxCount = curCount;
            }
        }
        double res = 0;
        if (maxCount > minCount) {
            double trCount = 0;
            for (ITriangle triang : allTriangles) {
                double s = TriangMath.getSquare(triang);
                res += Math.abs(s - bestS) / bestS;
                trCount++;
            }
            res = res / trCount;
        }
        if (res == 0) return 0;
        return Math.exp(res * 10) / Math.exp(10);
    }
