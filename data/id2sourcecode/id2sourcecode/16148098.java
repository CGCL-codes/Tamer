    public Oval(Point p1, Point p2, float thickness) {
        double xMax = Math.max(p1.x, p2.x);
        double xMin = Math.min(p1.x, p2.x);
        double yMax = Math.max(p1.y, p2.y);
        double yMin = Math.min(p1.y, p2.y);
        super.width = xMax - xMin;
        super.height = yMax - yMin;
        super.centerX = (xMax + xMin) / 2;
        super.centerY = (yMax + yMin) / 2;
        super.lineThickness = thickness;
        super.editor = DefaultEditor.instance;
    }
