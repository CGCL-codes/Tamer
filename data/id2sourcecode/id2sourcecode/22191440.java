    private void paintSegment(Graphics2D g2, AnnotationSegment seg, int heightOffset) {
        Point2f refPeak = seg.getLocation();
        float x = (float) refPeak.getX();
        float y = (float) refPeak.getY();
        int size = seg.size();
        AnnotationElement annotElement = null;
        int x0 = this.xMargin;
        int y0 = heightOffset;
        int x1 = x0 + w;
        final int y1 = y0;
        Rectangle r = calculateSegmentRectangle(g2, seg);
        int h = r.y;
        AbstractShape refPeakLine = new DottedLineShape();
        Color savedColor = g2.getColor();
        refPeakLine.setDrawingColor(g2.getColor());
        x0 = getScreenXPosition(x);
        y0 = getScreenYPosition(y);
        refPeakLine.init(x0, y0);
        refPeakLine.setCurrentPosition(new Point(x0, y1));
        if (inView(x0)) {
            refPeakLine.draw((Graphics) g2);
            g2.fillOval(x0 - 1, y0 - 1, 2, 2);
        }
        if (size == 0) {
            g2.setColor(savedColor);
            return;
        }
        LinkedList sAnnot = new LinkedList();
        for (int i = 0; i < size; i++) {
            annotElement = (AnnotationElement) seg.getElement(i);
            sAnnot.add(getElementAnnotation(annotElement));
            Point2f loc = annotElement.getLocation();
            x = (float) loc.getX();
            y = (float) loc.getY();
            int ix = getScreenXPosition(x);
            int iy = getScreenYPosition(y);
            AbstractShape line = new DottedLineShape();
            line.init(ix, y1);
            line.setCurrentPosition(new Point(ix, iy));
            line.setDrawingColor(g2.getColor());
            if (inView(ix)) {
                line.draw((Graphics) g2);
                g2.fillOval(ix - 1, iy - 1, 2, 2);
            }
            x0 = ix;
        }
        AnnotationElement obj[] = seg.toArray();
        if (obj == null) {
            g2.setColor(savedColor);
            return;
        }
        x0 = getScreenXPosition((float) obj[0].getX());
        y0 = getScreenYPosition((float) obj[0].getY());
        for (int i = 1; i < obj.length; i++) {
            AnnotationElement currentObj = obj[i];
            int iCurrentX = getScreenXPosition((float) currentObj.getX());
            int loc = (iCurrentX + x0) / 2;
            if (inView(loc)) {
                paintSegmentText(g2, (String) sAnnot.get(i - 1), loc, y1);
            }
            x0 = iCurrentX;
        }
        g2.setColor(savedColor);
    }
