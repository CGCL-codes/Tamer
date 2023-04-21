    private void calcArcNotify() {
        long left_tacho_total = this.left_start_tacho - this.left_tacho_target;
        long right_tacho_total = this.right_start_tacho - this.right_tacho_target;
        long tacho_total = (left_tacho_total + right_tacho_total) / 2;
        double circ = Math.PI * wheelDiameter;
        double dist = (circ * tacho_total) / 360;
        double angle = (1.0 * tacho_total) / arc_target_tacho_avg;
        angle *= arc_target_angle;
        angle *= angle_parity;
        for (MoveListener ml : listeners) ml.moveStopped(new Move((float) dist, (float) angle, false), this);
    }
