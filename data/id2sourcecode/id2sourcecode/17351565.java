    public void run(double frac) {
        Iterator iter = m_vis.items(m_group);
        while (iter.hasNext()) {
            DecoratorItem decorator = (DecoratorItem) iter.next();
            VisualItem decoratedItem = decorator.getDecoratedItem();
            Rectangle2D bounds = decoratedItem.getBounds();
            double x = bounds.getCenterX();
            double y = bounds.getCenterY();
            double x2 = 0;
            double y2 = 0;
            if (decoratedItem instanceof EdgeItem) {
                VisualItem dest = ((EdgeItem) decoratedItem).getTargetItem();
                x2 = dest.getX();
                y2 = dest.getY();
                x = (x + x2) / 2;
                y = (y + y2) / 2;
            }
            setX(decorator, null, x);
            setY(decorator, null, y);
        }
    }
