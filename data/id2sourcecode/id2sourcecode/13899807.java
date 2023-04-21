    public static double[] getMidpoint(double x1, double x2, double y1, double y2) {
        double xx = (x1 + x2) / 2;
        double yy = (y1 + y2) / 2;
        double[] answer = new double[2];
        answer[0] = xx;
        answer[1] = yy;
        return answer;
    }
