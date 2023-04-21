    public Shape render(Object o, Memory memory, Graphics g, ScreenTransform transform) {
        Road road = (Road) o;
        Node roadHead = (Node) memory.lookup(road.getHead());
        Node roadTail = (Node) memory.lookup(road.getTail());
        int headX = transform.toScreenX(roadHead.getX());
        int headY = transform.toScreenY(roadHead.getY());
        int tailX = transform.toScreenX(roadTail.getX());
        int tailY = transform.toScreenY(roadTail.getY());
        int blockedLines = 0;
        int total = road.getLinesToHead() + road.getLinesToTail();
        int free = total - blockedLines;
        boolean isBlocked = road.getBlock() > 0;
        RenderTools.setLineMode(g, ViewConstants.LINE_MODE_SOLID, Color.black, (road.getLinesToHead() + road.getLinesToTail()) * 3);
        g.drawLine(headX, headY, tailX, tailY);
        if (isBlocked) {
            Color blockColour = Color.white;
            if (blockedLines > 0) blockColour = Color.gray;
            if (free == 0) blockColour = Color.red;
            RenderTools.setLineMode(g, ViewConstants.LINE_MODE_SOLID, blockColour, 2);
            int centerX = (headX + tailX) / 2;
            int centerY = (headY + tailY) / 2;
            g.drawLine(centerX - 3, centerY - 3, centerX + 3, centerY + 3);
            g.drawLine(centerX - 3, centerY + 3, centerX + 3, centerY - 3);
        }
        Shape shape = new java.awt.geom.Line2D.Double(headX, headY, tailX, tailY);
        shape = new BasicStroke((road.getLinesToTail() + road.getLinesToHead()) * 3).createStrokedShape(shape);
        return shape;
    }
