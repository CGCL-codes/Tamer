    public static void getPoint(int ax1, int ay1, int ax2, int ay2, int n) {
        if (n < 3) JOptionPane.showMessageDialog(null, "��������Ϊ3");
        x = new int[n];
        y = new int[n];
        r = Math.sqrt(Math.pow((ax1 - ax2), 2) + Math.pow((ay1 - ay2), 2)) / 2;
        angle = 2 * Math.PI / n;
        centerX = (ax2 + ax1) / 2;
        centerY = (ay2 + ay1) / 2;
        for (int i = 0; i < n; i++) {
            x[i] = (int) (r * Math.cos(angle * i) + centerX);
            y[i] = (int) (r * Math.sin(angle * i) + centerY);
        }
    }
