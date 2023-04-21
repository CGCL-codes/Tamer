    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) return;
        mouseCurvePointIndex = findPointUnderMouse(e.getPoint());
        if (e.getButton() == MouseEvent.BUTTON1 && mouseCurvePointIndex == -1) {
            Point mp = e.getPoint();
            for (int p = 0; p < curve.length - 1; p++) {
                if (mp.x > curve[p].x && mp.x < curve[p + 1].x) {
                    Point[] newCurve = new Point[curve.length + 1];
                    int i = 0;
                    for (; i <= p; i++) newCurve[i] = curve[i];
                    newCurve[i++] = mp;
                    for (; i < newCurve.length; i++) newCurve[i] = curve[i - 1];
                    curve = newCurve;
                    repaint();
                    mouseCurvePointIndex = p + 1;
                    break;
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3 && mouseCurvePointIndex > 0 && mouseCurvePointIndex < curve.length - 1) {
            Point[] newCurve = new Point[curve.length - 1];
            int i = 0;
            for (; i < mouseCurvePointIndex; i++) newCurve[i] = curve[i];
            for (; i < newCurve.length; i++) newCurve[i] = curve[i + 1];
            mouseCurvePointIndex = -1;
            curve = newCurve;
            repaint();
            notifyCurveChange();
        }
    }
