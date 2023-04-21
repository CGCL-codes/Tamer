    public static void plotMovingD(Set<Point> set1, Set<Point> set2, double from, double to, double width, double step) {
        for (double i = from; i <= to; i += step) {
            double border = i + width;
            double mid = (i + border) / 2;
            List<Double> s1 = new ArrayList<Double>();
            List<Double> s2 = new ArrayList<Double>();
            for (Point p : set1) if (p.x >= i && p.x <= border) s1.add(p.y);
            for (Point p : set2) if (p.x >= i && p.x <= border) s2.add(p.y);
            double[] d = getMinMaxD(s1, s2);
            double s2rat = s2.size() / (double) (s1.size() + s2.size());
            System.out.println(mid + "\t" + d[1] + "\t" + d[0] + "\t" + s2rat);
        }
    }
