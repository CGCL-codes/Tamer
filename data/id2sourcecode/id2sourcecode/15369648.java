    public void layout(LWSelection selection) {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double xAdd = X_COL_SIZE;
        double yAdd = Y_COL_SIZE;
        int total = 0;
        Iterator<LWComponent> i = selection.iterator();
        while (i.hasNext()) {
            LWComponent c = i.next();
            if (c instanceof LWNode) {
                LWNode node = (LWNode) c;
                minX = node.getLocation().getX() < minX ? node.getLocation().getX() : minX;
                minY = node.getLocation().getY() < minY ? node.getLocation().getY() : minY;
                maxX = node.getLocation().getX() > maxX ? node.getLocation().getX() : maxX;
                maxY = node.getLocation().getY() > maxY ? node.getLocation().getY() : maxY;
                xAdd = xAdd > node.getWidth() ? xAdd : node.getWidth();
                yAdd = yAdd > node.getHeight() ? yAdd : node.getHeight();
                total++;
            }
        }
        double xSize = Math.sqrt(total) * (xAdd + X_SPACING) * FACTOR;
        double ySize = Math.sqrt(total) * (yAdd + Y_SPACING) * FACTOR;
        i = selection.iterator();
        while (i.hasNext()) {
            LWComponent c = i.next();
            if (c instanceof LWNode) {
                LWNode node = (LWNode) c;
                double centerX = (minX + maxX) / 2;
                double centerY = (minY + maxY) / 2;
                double x = centerX + xSize * (Math.random() - 0.5);
                double y = centerY + ySize * (Math.random() - 0.5);
                int col_count = 0;
                boolean flag = true;
                while (flag && col_count < MAX_COLLISION_CHECK) {
                    LWComponent overlapComponent;
                    if (VUE.getActiveViewer() != null) {
                        if ((VUE.getActiveViewer().pickNode((float) x, (float) y) != null) || (VUE.getActiveViewer().pickNode((float) x + node.getWidth(), (float) y + node.getHeight()) != null) || (VUE.getActiveViewer().pickNode((float) x, (float) y + node.getHeight()) != null) || (VUE.getActiveViewer().pickNode((float) x + node.getWidth(), (float) y) != null)) {
                            x = centerX + xSize * (Math.random() - 0.5);
                            y = centerY + ySize * (Math.random() - 0.5);
                        } else {
                            flag = false;
                        }
                    }
                    col_count++;
                }
                node.setLocation(x, y);
            }
        }
    }
