    private static RingEdgeDeletionOut deleteEdge(LineString lr, LineEdge seg) {
        Coordinate[] cs = lr.getCoordinates();
        if (cs.length <= 4) return new RingEdgeDeletionOut(null, false);
        Coordinate p1 = cs[seg.id];
        Coordinate p2 = cs[seg.id + 1];
        Coordinate p0 = seg.id == 0 ? cs[cs.length - 2] : cs[seg.id - 1];
        Coordinate p3 = seg.id + 2 == cs.length ? cs[1] : cs[seg.id + 2];
        double a = Math.atan2(p3.y - p2.y, p3.x - p2.x) - Math.atan2(p0.y - p1.y, p0.x - p1.x);
        if (a <= -Math.PI) a += 2 * Math.PI; else if (a > Math.PI) a -= 2 * Math.PI;
        if (Math.abs(a) <= Math.PI / 2 + TOL_ANGLE && Math.abs(a) >= Math.PI / 2 - TOL_ANGLE) {
            double x1 = p0.x - p1.x, ya = p0.y - p1.y;
            double x2 = p3.x - p2.x, yb = p3.y - p2.y;
            double t = (x2 * (p1.y - p2.y) - yb * (p1.x - p2.x)) / (x1 * yb - ya * x2);
            Coordinate c = new Coordinate(p1.x + t * x1, p1.y + t * ya);
            Coordinate[] cs_ = new Coordinate[cs.length - 1];
            if (seg.id != 0) {
                for (int i = 0; i < seg.id; i++) cs_[i] = cs[i];
                cs_[seg.id] = c;
                for (int i = seg.id + 1; i < cs.length - 1; i++) cs_[i] = cs[i + 1];
                if (seg.id == cs.length - 2) cs_[0] = c;
            } else {
                cs_[0] = c;
                for (int i = 1; i < cs.length - 2; i++) cs_[i] = cs[i - 1];
                cs_[cs.length - 2] = c;
            }
            if (cs_[0].x != cs_[cs_.length - 1].x || cs_[0].y != cs_[cs_.length - 1].y) return new RingEdgeDeletionOut(null, false); else if (cs_.length <= 3) return new RingEdgeDeletionOut(null, false); else return new RingEdgeDeletionOut(new GeometryFactory().createLinearRing(cs_), true);
        } else if (Math.abs(a) >= Math.PI - TOL_ANGLE) {
            double dx = p1.x - p0.x + p3.x - p2.x;
            double dy = p1.y - p0.y + p3.y - p2.y;
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = dx / length;
            dy = dy / length;
            double xMid = (p0.x + p3.x) * 0.5, yMid = (p0.y + p3.y) * 0.5;
            double t1 = (p0.x - xMid) * dx + (p0.y - yMid) * dy;
            double t2 = (p3.x - xMid) * dx + (p3.y - yMid) * dy;
            Coordinate c1 = new Coordinate(xMid + t1 * dx, yMid + t1 * dy);
            Coordinate c2 = new Coordinate(xMid + t2 * dx, yMid + t2 * dy);
            Coordinate[] cs_ = new Coordinate[cs.length - 2];
            cs_[0] = c1;
            cs_[1] = c2;
            if (seg.id != 0) {
                for (int i = seg.id + 3; i < cs.length; i++) cs_[i - seg.id - 1] = cs[i];
                for (int i = 1; i < seg.id - 1; i++) cs_[cs.length - seg.id - 2 + i] = cs[i];
                cs_[cs.length - 3] = c1;
            } else {
                for (int i = 2; i < cs.length - 3; i++) cs_[i] = cs[i + 1];
                cs_[cs.length - 3] = c1;
            }
            if (cs_[0].x != cs_[cs_.length - 1].x || cs_[0].y != cs_[cs_.length - 1].y) return new RingEdgeDeletionOut(null, false); else if (cs_.length <= 3) return new RingEdgeDeletionOut(null, false); else return new RingEdgeDeletionOut(new GeometryFactory().createLinearRing(cs_), true);
        } else if (Math.abs(a) <= TOL_ANGLE) {
            if (p0 == p3) return new RingEdgeDeletionOut(null, false);
            double t1 = ((p2.x - p3.x) * (p0.x - p3.x) + (p2.y - p3.y) * (p0.y - p3.y)) / ((p2.x - p3.x) * (p2.x - p3.x) + (p2.y - p3.y) * (p2.y - p3.y));
            double t2 = ((p1.x - p0.x) * (p3.x - p0.x) + (p1.y - p0.y) * (p3.y - p0.y)) / ((p1.x - p0.x) * (p1.x - p0.x) + (p1.y - p0.y) * (p1.y - p0.y));
            Coordinate c1_ = new Coordinate(p3.x + t1 * (p2.x - p3.x), p3.y + t1 * (p2.y - p3.y));
            Coordinate c2_ = new Coordinate(p0.x + t2 * (p1.x - p0.x), p0.y + t2 * (p1.y - p0.y));
            boolean v1 = (p3.x - c1_.x) * (p2.x - c1_.x) + (p3.y - c1_.y) * (p2.y - c1_.y) < 0;
            boolean v2 = (p0.x - c2_.x) * (p1.x - c2_.x) + (p0.y - c2_.y) * (p1.y - c2_.y) < 0;
            Coordinate c1, c2;
            if (!v1 && !v2) return new RingEdgeDeletionOut(null, false); else if (!v1 && v2) {
                c1 = p0;
                c2 = c2_;
            } else if (v1 && !v2) {
                c1 = c1_;
                c2 = p3;
            } else {
                double d1 = p0.distance(p1);
                double d2 = p3.distance(p2);
                if (d1 < d2) {
                    c1 = c1_;
                    c2 = p3;
                } else {
                    c1 = p0;
                    c2 = c2_;
                }
            }
            Coordinate[] cs_ = new Coordinate[cs.length - 2];
            cs_[0] = c1;
            cs_[1] = c2;
            if (seg.id != 0) {
                for (int i = seg.id + 3; i < cs.length; i++) cs_[i - seg.id - 1] = cs[i];
                for (int i = 1; i < seg.id - 1; i++) cs_[cs.length - seg.id - 2 + i] = cs[i];
                cs_[cs.length - 3] = c1;
            } else {
                for (int i = 2; i < cs.length - 3; i++) cs_[i] = cs[i + 1];
                cs_[cs.length - 3] = c1;
            }
            if (cs_[0].x != cs_[cs_.length - 1].x || cs_[0].y != cs_[cs_.length - 1].y) return new RingEdgeDeletionOut(null, false); else if (cs_.length <= 3) return new RingEdgeDeletionOut(null, false); else return new RingEdgeDeletionOut(new GeometryFactory().createLinearRing(cs_), true);
        } else {
            return new RingEdgeDeletionOut(null, false);
        }
    }
