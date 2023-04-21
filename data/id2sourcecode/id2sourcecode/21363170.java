    protected void showLayoutTargetFeedback(Request request) {
        if (getHost().getChildren().size() == 0) return;
        Polyline fb = getLineFeedback();
        Transposer transposer = new Transposer();
        transposer.setEnabled(!isHorizontal());
        boolean before = true;
        int epIndex = getFeedbackIndexFor(request);
        RectD2D r = null;
        if (epIndex == -1) {
            before = false;
            epIndex = getHost().getChildren().size() - 1;
            EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
            r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
        } else {
            EditPart editPart = (EditPart) getHost().getChildren().get(epIndex);
            r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
            Point p = transposer.t(getLocationFromRequest(request));
            if (p.x <= r.x + (r.width / 2)) before = true; else {
                before = false;
                epIndex--;
                editPart = (EditPart) getHost().getChildren().get(epIndex);
                r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
            }
        }
        int x = Integer.MIN_VALUE;
        if (before) {
            if (epIndex > 0) {
                RectD2D boxPrev = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost().getChildren().get(epIndex - 1)));
                int prevRight = boxPrev.right();
                if (prevRight < r.x) {
                    x = prevRight + (r.x - prevRight) / 2;
                } else if (prevRight == r.x) {
                    x = prevRight + 1;
                }
            }
            if (x == Integer.MIN_VALUE) {
                RectD2D parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost()));
                x = r.x - 5;
                if (x < parentBox.x) x = parentBox.x + (r.x - parentBox.x) / 2;
            }
        } else {
            RectD2D parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost()));
            int rRight = r.x + r.width;
            int pRight = parentBox.x + parentBox.width;
            x = rRight + 5;
            if (x > pRight) x = rRight + (pRight - rRight) / 2;
        }
        Point p1 = new Point(x, r.y - 4);
        p1 = transposer.t(p1);
        fb.translateToRelative(p1);
        Point p2 = new Point(x, r.y + r.height + 4);
        p2 = transposer.t(p2);
        fb.translateToRelative(p2);
        fb.setPoint(p1, 0);
        fb.setPoint(p2, 1);
    }
