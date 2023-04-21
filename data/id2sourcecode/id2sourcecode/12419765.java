    public Cannon(Vec2D position, int minAngle, int maxAngle, int power) {
        this.position = position;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.angle = (maxAngle + minAngle) / 2;
        this.power = power;
    }
