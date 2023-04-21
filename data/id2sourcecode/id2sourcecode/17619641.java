    public void layout(LWSelection selection) throws Exception {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double xAdd = 0;
        double yAdd = 0;
        int count = 0;
        int total = 0;
        Iterator<LWComponent> i = selection.iterator();
        double initialCenterX = selection.getBounds().getCenterX();
        double initialCenterY = selection.getBounds().getCenterY();
        while (i.hasNext()) {
            LWComponent c = i.next();
            if (c.isManagedLocation()) continue;
            if (c instanceof LWNode) {
                minX = c.getLocation().getX() < minX ? c.getLocation().getX() : minX;
                minY = c.getLocation().getY() < minY ? c.getLocation().getY() : minY;
                maxX = c.getLocation().getX() > maxX ? c.getLocation().getX() : maxX;
                maxY = c.getLocation().getY() > maxY ? c.getLocation().getY() : maxY;
                xAdd = xAdd > c.getWidth() ? xAdd : c.getWidth();
                yAdd = yAdd > c.getHeight() ? yAdd : c.getHeight();
                total++;
            }
        }
        xAdd = xAdd * 1.10;
        xAdd = xAdd * 1.10;
        double radiusX = total * xAdd / 6;
        double radiusY = total * yAdd / 3;
        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;
        i = selection.iterator();
        double angle = 0.0;
        while (i.hasNext()) {
            LWComponent c = i.next();
            if (c.isManagedLocation()) continue;
            if (c instanceof LWNode) {
                c.setLocation(centerX + radiusX * Math.cos(angle), centerY + radiusY * Math.sin(angle));
                count++;
                angle = Math.PI * 2 * count / total;
            }
        }
        double finalCenterX = selection.getBounds().getCenterX();
        double finalCenterY = selection.getBounds().getCenterY();
        double driftX = finalCenterX - initialCenterX;
        double driftY = finalCenterY - initialCenterY;
        if (Math.abs(driftX) > 0 || Math.abs(driftY) > 0) {
            i = selection.iterator();
            while (i.hasNext()) {
                LWComponent c = i.next();
                if (c.isManagedLocation()) continue;
                if (c instanceof LWNode) {
                    c.setLocation(c.getX() - driftX, c.getY() - driftY);
                    count++;
                    angle = Math.PI * 2 * count / total;
                }
            }
        }
    }
