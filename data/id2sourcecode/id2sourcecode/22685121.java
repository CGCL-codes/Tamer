    public void setDiameter(double diameter, double minimumDistance) {
        ConcentricCircle sibling;
        double outer;
        double inner = this.getInner();
        minimumDistance = Math.abs(minimumDistance);
        diameter = Math.abs(diameter);
        if (this.nextAvailable()) {
            sibling = (ConcentricCircle) this.getNext();
            outer = sibling.getDiameter();
        } else {
            outer = Double.MAX_VALUE;
        }
        if ((inner + minimumDistance) > (outer - minimumDistance)) {
            if (minimumDistance < inner || minimumDistance > outer) {
                this.diameter = (inner + outer) / 2;
            } else {
                this.diameter = diameter;
            }
        } else {
            this.diameter = Math.max(Math.abs(diameter), inner + minimumDistance);
            this.diameter = Math.min(this.diameter, outer - minimumDistance);
        }
        if (this.diameter < 0.1D) {
            this.diameter = 0.1D;
        }
    }
