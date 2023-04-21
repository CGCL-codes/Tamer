    public Vector2d getMiddle() {
        Vector2d middle = new Vector2d();
        double endAng = this.endAng;
        double middleAng = (startAng + endAng) / 2;
        double middleX = center.x() + Math.cos(middleAng) * radius;
        double middleY = center.y() + Math.sin(middleAng) * radius;
        middle.set(middleX, middleY);
        return middle;
    }
