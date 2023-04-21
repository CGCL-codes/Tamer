    public static void plotMovingD(Set<Point> set, double from, double to, double width, double step) {
        for (double i = from; i <= to; i += step) {
            double border = i + width;
            double mid = (i + border) / 2;
            List<Double> list = new ArrayList<Double>();
            for (Point p : set) if (p.x >= i && p.x <= border) {
                list.add(p.y);
            }
            Double[] ys = list.toArray(new Double[list.size()]);
            Arrays.sort(ys);
            double median = ys[ys.length / 2];
            List<Double> s1 = new ArrayList<Double>();
            List<Double> s2 = new ArrayList<Double>();
            for (Point p : set) if (p.x >= i && p.x <= border) {
                if (p.y < median) s1.add(p.z); else s2.add(p.z);
            }
            double d = calcDStat(s1, s2);
            System.out.println(mid + "\t" + d + "\t" + median);
        }
    }
