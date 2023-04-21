    public static double distanceToLine(Point2D p, Point2D endA, Point2D endZ) {
        double AC = p.distance(endA);
        double BC = p.distance(endZ);
        double AB = endA.distance(endZ);
        if (AB == (AC + BC)) {
            return 0;
        }
        double ACs = AC * AC;
        double BCs = BC * BC;
        double AD_BD = (ACs - BCs) / AB;
        double AD = (AD_BD + AB) / 2;
        double CDs = ACs - (AD * AD);
        return Math.sqrt(CDs);
    }
