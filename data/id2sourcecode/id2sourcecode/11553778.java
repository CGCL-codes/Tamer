    public static boolean areParallel(Line2D line1, Line2D line2, float threshold) {
        float slope1 = slope(line1);
        float slope2 = slope(line2);
        if (Float.isInfinite(slope1) && Float.isInfinite(slope2)) {
            return true;
        }
        Point2D midP1 = midpoint(line1);
        Point2D midP2 = midpoint(line2);
        double transXLine2 = midP2.getX() - midP1.getX();
        double transYLine2 = midP2.getY() - midP1.getY();
        Point2D p1Line2 = new Point2D.Double(line2.getX1() - transXLine2, line2.getY1() - transYLine2);
        Point2D p2Line2 = new Point2D.Double(line2.getX2() - transXLine2, line2.getY2() - transYLine2);
        Line2D transLine2 = new Line2D.Double(p1Line2, p2Line2);
        double dist1To2 = transLine2.ptLineDist(line1.getX1(), line1.getY1());
        double dist2To1 = line1.ptLineDist(transLine2.getX1(), transLine2.getY1());
        double avgPtLnDist = (dist1To2 + dist2To1) / 2;
        if (avgPtLnDist <= threshold) return true; else return false;
    }
