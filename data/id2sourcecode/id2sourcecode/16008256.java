    private void doValues() {
        double x1Val = 0, x2Val = 0, y1Val = 0, y2Val = 0;
        double width, height;
        if (x1 != null) {
            x1Val = x1.getVal();
        }
        if (y1 != null) {
            y1Val = y1.getVal();
        }
        if (x2 != null) {
            x2Val = x2.getVal();
            width = x2Val;
        } else {
            width = coords.pixelToX(coords.xToPixelF(x1Val) + h) - x1Val;
        }
        if (y2 != null) {
            y2Val = y2.getVal();
            height = y2Val;
        } else {
            height = coords.pixelToY(coords.yToPixelF(y1Val) - v) - y1Val;
        }
        if (arcStart != null) {
            asVal = arcStart.getVal();
        }
        if (arcEnd != null) {
            aeVal = arcEnd.getVal();
        }
        double angle = (aeVal + asVal) / 2;
        double radius = height * width / Math.sqrt(Math.pow(width, 2) * Math.pow(Math.sin(angle), 2) + Math.pow(height, 2) * Math.pow(Math.cos(angle), 2));
        labelX = radius * Math.cos(angle) + x1Val;
        labelY = radius * Math.sin(angle) + y1Val;
        changed = false;
    }
