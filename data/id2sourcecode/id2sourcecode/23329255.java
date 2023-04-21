    private AnnotationElement getClosestPossibleAnnotation(int x, int y) {
        AnnotationSegment seg = getActiveSegment();
        int size = Integer.MIN_VALUE;
        LinkedList<AnnotationElementShape> list = null;
        if (isRightHandedPick(x)) {
            list = rhList;
        } else {
            list = lhList;
        }
        size = list.size();
        if (size == 0) return null;
        float dist[] = new float[size];
        Point p = new Point(x, y);
        Rectangle r = annotator.calculateSegmentRectangle(seg);
        int h = r.y;
        for (int i = 0; i < size; i++) {
            ArrowShape arrow = list.get(i);
            int x0 = arrow.getStartPosition().x;
            int x1 = arrow.getCurrentPosition().x;
            float avg = (x0 + x1) / 2;
            float y0 = arrow.getStartPosition().y;
            dist[i] = (float) p.distance(avg, y0);
        }
        float ref = Float.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (ref > dist[i]) {
                index = i;
                ref = dist[i];
            }
        }
        return list.get(index).getAnnotationElement();
    }
