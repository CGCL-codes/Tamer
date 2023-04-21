    public static CubicBezierCurve computeBezierFrom(QuadraticCurve curve, float startX, float endX) {
        Point2f p1 = p(startX, curve.getY(startX));
        Point2f p2 = p(endX, curve.getY(endX));
        float hx = (startX + endX) / 2;
        Point2f h = p(hx, curve.getY(hx));
        float t = 0.5f;
        Point2f c = h.sub(p1.scale((1 - t) * (1 - t))).sub(p2.scale(t * t)).scale(1f / (2 * t * (1 - t)));
        Point2f c1 = p1.scale(1f / 3).add(c.scale(2f / 3));
        Point2f c2 = p2.scale(1f / 3).add(c.scale(2f / 3));
        return new CubicBezierCurve(p1, c1, c2, p2);
    }
