    public void splitEdgeNearPoint(GraphEdgeModel edge, Point2D pt) {
        Point2D edgeControls[] = getActualEdgeControls(edge);
        Point2D ctrls[];
        if (edgeControls.length < 4) {
            ctrls = new Point2D[4];
            lineToBezier(edgeControls, 0, ctrls, 0);
        } else ctrls = NearestPointOnCurve.splitBezierSetNearPoint(pt, edgeControls);
        Point2D newCtrls[] = new Point2D[ctrls.length - 2];
        for (int i = 0; i < newCtrls.length; i++) newCtrls[i] = ctrls[i + 1];
        edge.setControlPoints(newCtrls);
        repaint(edge.getChangeBBox());
    }
