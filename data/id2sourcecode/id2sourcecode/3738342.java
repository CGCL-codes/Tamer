    void update(int take, int write, int read) {
        values = new int[] { take, write, read };
        calcValues();
        repaint();
    }
