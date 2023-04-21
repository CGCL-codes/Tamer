    public void nextTacho(double leftTacho, double rightTacho) {
        double distance_left = distance_left_degree * (leftTacho - prevLeftTacho);
        double distance_right = distance_right_degree * (rightTacho - prevRightTacho);
        this.orientation = this.orientation + (distance_right - distance_left) / this.distance_wheels;
        double distanceTraveled = (distance_left + distance_right) / 2;
        distanceTraveled *= 0.917;
        this.x = this.x + (distanceTraveled) * Math.cos(orientation);
        this.y = this.y + (distanceTraveled) * Math.sin(orientation);
        prevLeftTacho = leftTacho;
        prevRightTacho = rightTacho;
    }
