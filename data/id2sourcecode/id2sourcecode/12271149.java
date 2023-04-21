    public void paint(Graphics g) {
        Vector tmp = new Vector();
        for (int i = 0; i < exp_levels.size(); i++) {
            tmp.addElement(exp_levels.elementAt(i));
        }
        Collections.sort(tmp, new ExpressionComparer());
        int midindex = (int) (tmp.size() / 2);
        int mid = ((ExpressionLevel) tmp.elementAt(midindex)).getLevel();
        int min = ((ExpressionLevel) tmp.elementAt(0)).getLevel();
        int max = ((ExpressionLevel) tmp.lastElement()).getLevel();
        if (absolute) {
            min = minval;
            max = maxval;
            mid = (min + max) / 2;
        }
        Color c = null;
        for (int i = 0; i < exp_levels.size(); i++) {
            int val = ((ExpressionLevel) exp_levels.elementAt(i)).getLevel();
            if (val < min) {
                c = Color.cyan;
            } else if (val > max) {
                c = Color.magenta;
            } else if (val >= mid) {
                int tmpval = (val - mid) * 255 / (max - mid + 1);
                if (tmpval > 255) {
                    tmpval = 255;
                }
                if (tmpval < 0) {
                    tmpval = 0;
                }
                c = new Color(tmpval, 0, 0);
            } else {
                int tmpval = (mid - val) * 255 / (mid - min + 1);
                if (tmpval > 255) {
                    tmpval = 255;
                }
                if (tmpval < 0) {
                    tmpval = 0;
                }
                c = new Color(0, tmpval, 0);
            }
            g.setColor(c);
            g.fillRect(boxsize * i, 0, boxsize, boxsize);
        }
    }
