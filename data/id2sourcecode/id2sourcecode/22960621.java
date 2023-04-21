    public CircleArrow(boolean selected, String arrowName, int x, int ymin, int ymax, int diameter, boolean flag) {
        super(selected, arrowName, flag, x, x + diameter, ymin, ymax);
        this.x = xmin;
        this.y = (ymin + ymax) / 2;
        this.diameter = diameter;
    }
