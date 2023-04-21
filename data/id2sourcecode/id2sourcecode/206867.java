    public void approximation(Graphics g, int succ_x, int succ_y, int prev_x, int prev_y, Vector v) {
        double hypo = Math.sqrt(Math.pow((prev_x - succ_x), 2) + Math.pow((prev_y - succ_y), 2));
        if (hypo > 1D) {
            int middle_x = (prev_x + succ_x) / 2;
            int middle_y = (prev_y + succ_y) / 2;
            approximation(g, succ_x, succ_y, middle_x, middle_y, v);
            approximation(g, prev_x, prev_y, middle_x, middle_y, v);
            g.drawLine(middle_x, middle_y, middle_x, middle_y);
            v.addElement(new Point(middle_x, middle_y));
        }
    }
