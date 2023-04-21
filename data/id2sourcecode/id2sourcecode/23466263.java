    private void a(final Graphics g1, final String s1, final int i1, final int j1, final int k1, final int l1, final int i2, final int j2) {
        final int k2 = j1 / 2;
        final int l2 = i1 + k2;
        final int i3 = i1 + j1;
        final int j3 = i1 + k2 / 2;
        final int k3 = l2 + k2 / 2;
        final int l3 = (k1 + l1) / 2;
        final int i4 = (int) ((double) k2 * 0.86602540378444004D);
        final int j4 = k1 + i4;
        final int k4 = l1 - i4;
        if (s1.equals("[")) {
            g1.drawLine(j3, k1, j3, l1);
            g1.drawLine(j3, l1, k3, l1);
            g1.drawLine(j3, k1, k3, k1);
        } else if (s1.equals("]")) {
            g1.drawLine(k3, k1, k3, l1);
            g1.drawLine(j3, l1, k3, l1);
            g1.drawLine(j3, k1, k3, k1);
        } else if (s1.equals("|")) {
            g1.drawLine(l2, k1, l2, l1);
        } else if (s1.equals("||")) {
            final int l4 = l2 + i2 / 4;
            g1.drawLine(l2, k1, l2, l1);
            g1.drawLine(l4, k1, l4, l1);
        } else if (s1.equals("(")) {
            for (int i5 = j2; i5 < 2 + j2; i5++) {
                final int i6 = j3 + i5;
                a(g1, k3 + i5, j4, k2, 180, -60);
                g1.drawLine(i6, j4, i6, k4);
                a(g1, k3 + i5, k4, k2, 180, 60);
            }
        } else if (s1.equals(")")) {
            for (int j5 = j2; j5 < 2 + j2; j5++) {
                final int j6 = k3 + j5;
                a(g1, j3 + j5, j4, k2, 0, 60);
                g1.drawLine(j6, j4, j6, k4);
                a(g1, j3 + j5, k4, k2, 0, -60);
            }
        } else if (s1.equals("<")) {
            g1.drawLine(j3, l3, k3, k1);
            g1.drawLine(j3, l3, k3, l1);
        } else if (s1.equals(">")) {
            g1.drawLine(k3, l3, j3, k1);
            g1.drawLine(k3, l3, j3, l1);
        } else if (s1.equals("{")) {
            for (int k5 = j2; k5 < 2 + j2; k5++) {
                final int k6 = l2 + k5;
                a(g1, i3 + k5, j4, k2, 180, -60);
                g1.drawLine(k6, j4, k6, l3 - k2);
                a(g1, i1 + k5, l3 - k2, k2, 0, -90);
                a(g1, i1 + k5, l3 + k2, k2, 0, 90);
                g1.drawLine(k6, l3 + k2, k6, k4);
                a(g1, i3 + k5, k4, k2, 180, 60);
            }
        } else if (s1.equals("}")) {
            for (int l5 = j2; l5 < 2 + j2; l5++) {
                final int l6 = l2 + l5;
                a(g1, i1 + l5, j4, k2, 0, 60);
                g1.drawLine(l6, j4, l6, l3 - k2);
                a(g1, i3 + l5, l3 - k2, k2, -180, 90);
                a(g1, i3 + l5, l3 + k2, k2, 180, -90);
                g1.drawLine(l6, l3 + k2, l6, k4);
                a(g1, i1 + l5, k4, k2, 0, -60);
            }
        }
    }
