    public ConcentricCircle(boolean before, ConcentricCircle x) {
        super(before, x);
        ConcentricCircle sibling;
        double outer;
        double inner = this.getInner();
        if (this.nextAvailable()) {
            sibling = (ConcentricCircle) this.getNext();
            outer = sibling.getDiameter();
        } else {
            sibling = (ConcentricCircle) this.getPrevious();
            outer = sibling.getDiameter() * 4;
        }
        this.diameter = (inner + outer) / 2;
    }
