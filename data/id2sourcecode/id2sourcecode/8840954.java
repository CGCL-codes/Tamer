    public Paint transform(E e) {
        Layout<V, E> layout = vv.getGraphLayout();
        Pair<V> p = layout.getGraph().getEndpoints(e);
        V b = p.getFirst();
        V f = p.getSecond();
        Point2D pb = transformer.transform(layout.transform(b));
        Point2D pf = transformer.transform(layout.transform(f));
        float xB = (float) pb.getX();
        float yB = (float) pb.getY();
        float xF = (float) pf.getX();
        float yF = (float) pf.getY();
        if ((layout.getGraph().getEdgeType(e)) == EdgeType.UNDIRECTED) {
            xF = (xF + xB) / 2;
            yF = (yF + yB) / 2;
        }
        if (selfLoop.evaluate(Context.<Graph<V, E>, E>getInstance(layout.getGraph(), e))) {
            yF += 50;
            xF += 50;
        }
        return new GradientPaint(xB, yB, getColor1(e), xF, yF, getColor2(e), true);
    }
