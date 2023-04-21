    public static void drawSpline(Graphics g, double p1_x, double p1_y, double p2_x, double p2_y, double p3_x, double p3_y, double p4_x, double p4_y, int depth) {
        if (depth > 0) {
            double l1_x = (p1_x + p2_x) / 2;
            double l1_y = (p1_y + p2_y) / 2;
            double m_x = (p2_x + p3_x) / 2;
            double m_y = (p2_y + p3_y) / 2;
            double l2_x = (l1_x + m_x) / 2;
            double l2_y = (l1_y + m_y) / 2;
            double r1_x = (p3_x + p4_x) / 2;
            double r1_y = (p3_y + p4_y) / 2;
            double r2_x = (r1_x + m_x) / 2;
            double r2_y = (r1_y + m_y) / 2;
            double m2_x = (l2_x + r2_x) / 2;
            double m2_y = (l2_y + r2_y) / 2;
            drawSpline(g, p1_x, p1_y, l1_x, l1_y, l2_x, l2_y, m2_x, m2_y, depth - 1);
            drawSpline(g, m2_x, m2_y, r2_x, r2_y, r1_x, r1_y, p4_x, p4_y, depth - 1);
        } else {
            g.drawLine((int) p1_x, (int) p1_y, (int) p4_x, (int) p4_y);
        }
    }
