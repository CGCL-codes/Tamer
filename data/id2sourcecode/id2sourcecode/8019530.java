    protected void showLayoutTargetFeedback(Request request) {
        List realChildren = getHost().getChildren();
        List children = new ArrayList();
        children.addAll(realChildren);
        List removedChildrens = new ArrayList();
        for (Object object : children) {
            EditPart child = (EditPart) object;
            if ((child instanceof OwlsInputClientMessageEditPart) || (child instanceof OwlsOutputClientMessageEditPart)) {
                removedChildrens.add(child);
            }
        }
        children.removeAll(removedChildrens);
        if (children.size() == 0) return;
        Polyline fb = getLineFeedback();
        Transposer transposer = new Transposer();
        transposer.setEnabled(!isHorizontal());
        boolean before = true;
        int epIndex = getFeedbackIndexFor(request);
        Rectangle r = null;
        if (epIndex == -1) {
            before = false;
            epIndex = children.size() - 1;
            EditPart editPart = (EditPart) children.get(epIndex);
            r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
        } else {
            EditPart editPart = (EditPart) children.get(epIndex);
            r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
            Point p = transposer.t(getLocationFromRequest(request));
            if (p.x <= r.x + (r.width / 2)) before = true; else {
                before = false;
                epIndex--;
                editPart = (EditPart) children.get(epIndex);
                r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
            }
        }
        int x = Integer.MIN_VALUE;
        if (before) {
            if (epIndex > 0) {
                Rectangle boxPrev = transposer.t(getAbsoluteBounds((GraphicalEditPart) children.get(epIndex - 1)));
                int prevRight = boxPrev.right();
                if (prevRight < r.x) {
                    x = prevRight + (r.x - prevRight) / 2;
                } else if (prevRight == r.x) {
                    x = prevRight + 1;
                }
            }
            if (x == Integer.MIN_VALUE) {
                Rectangle parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost()));
                x = r.x - 5;
                if (x < parentBox.x) x = parentBox.x + (r.x - parentBox.x) / 2;
            }
        } else {
            Rectangle parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost()));
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
