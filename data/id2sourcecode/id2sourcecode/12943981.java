    public void paint(Graphics g) {
        EmulationStatisticMaker statistic = new EmulationStatisticMaker(emulator);
        statistic.calculateStatistic();
        f.setMaximumFractionDigits(6);
        LA = (int) (LA);
        TableManagment myTable = new TableManagment(this.data);
        int[] typecrossing = new int[myTable.getAllT().size()];
        for (int i = 0; i < typecrossing.length; i++) {
            typecrossing[i] = 0;
            if (myTable.getAllT().get(i).getLyambda() == 0) {
                typecrossing[i] = 1;
            }
        }
        TreeofPetriNet mytree = new TreeofPetriNet(myTable.getAllP().size(), myTable.getAllT().size(), myTable.getMatrixDi(), myTable.getMatrixDq(), myTable.getMarkirovka(), typecrossing, data);
        Z = mytree.WriteResult(0);
        int i, count, j;
        int Angle = 0;
        int dAngle;
        double DegToRad = Math.PI / 180;
        double RadToDeg = 180.0 / Math.PI;
        super.paint(g);
        count = TreeofPetriNet.RepeatCount;
        if (count != 0) {
            dAngle = 360 / count;
            int size = 0;
            for (i = 0; i < count; i++) {
                for (j = 0; j < Z[i].getColVhod(); j++) {
                    if (size < Z[i].getElementVhod(j)) size = Z[i].getElementVhod(j);
                }
            }
            double[][] res = null;
            Object[][] matrix = statistic.makeChangingProbabilityMarkingsStatistic();
            int length = matrix.length;
            if (length > 0) {
                res = new double[matrix.length][matrix[0].length - 1];
                for (i = 0; i < matrix.length; i++) {
                    for (j = 1; j < matrix[0].length; j++) {
                        if (matrix[i][j] != null) {
                            res[i][j - 1] = (Double) matrix[i][j];
                        } else {
                            res[i][j - 1] = 0.0;
                        }
                        if (res[i][j - 1] > 0) {
                            res[i][j - 1] = new BigDecimal(res[i][j - 1]).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                    }
                }
            }
            for (i = 0; i < count; i++) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint((int) (CenterX * scale) + (int) (Dist * scale * Math.cos(Angle * DegToRad)), (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(Angle * DegToRad)), up, (int) (CenterX * scale) + (int) (Dist * scale * Math.cos(Angle * DegToRad)) + (int) (d * scale), (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(Angle * DegToRad)) + (int) (d * scale), down);
                g2.setPaint(gradient);
                g.fillOval((int) (CenterX * scale) + (int) (Dist * scale * Math.cos(Angle * DegToRad)), (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(Angle * DegToRad)), (int) (d * scale), (int) (d * scale));
                g.setColor(oval);
                g.drawOval((int) (CenterX * scale) + (int) (Dist * scale * Math.cos(Angle * DegToRad)), (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(Angle * DegToRad)), (int) (d * scale), (int) (d * scale));
                g.setColor(Color.black);
                g.drawString(Integer.toString(i), (int) (CenterX * scale) + (int) (Dist * scale * Math.cos(Angle * DegToRad)) + (int) (r * scale - 5 * scale), (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(Angle * DegToRad)) + (int) (r * scale));
                Angle = Angle + dAngle;
            }
            int A1, A2, dx1, dy1, dx2, dy2, s;
            int x1, x2, y1, y2, cx, cy, lx1, ly1, lx2, ly2;
            double k;
            for (i = 0; i < count; i++) {
                for (j = 0; j < Z[i].getColVhod(); j++) {
                    A2 = dAngle * Z[i].getElementVuhod(j);
                    A1 = dAngle * Z[i].getElementVhod(j);
                    dx1 = (int) (r * scale * Math.cos(A1 * DegToRad));
                    dy1 = (int) (r * scale * Math.sin(A1 * DegToRad));
                    dx2 = (int) (r * scale * Math.cos(A2 * DegToRad));
                    dy2 = (int) (r * scale * Math.sin(A2 * DegToRad));
                    x1 = (int) (CenterX * scale) + (int) (Dist * scale * Math.cos(A1 * DegToRad)) + (int) (r * scale) - dx1;
                    y1 = (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(A1 * DegToRad)) + (int) (r * scale) - dy1;
                    x2 = (int) (CenterX * scale) + (int) (Dist * scale * Math.cos(A2 * DegToRad)) + (int) (r * scale) - dx2;
                    y2 = (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(A2 * DegToRad)) + (int) (r * scale) - dy2;
                    if (A1 != A2) {
                        g.setColor(line);
                        g.drawLine(x2, y2, x1, y1);
                        k = (double) ((double) ((y1 - y2)) / (double) (x1 - x2));
                        s = 1;
                        if (x1 - x2 >= 0) s = -1;
                        cx = (x1 + x2) / 2;
                        cy = (y1 + y2) / 2;
                        Angle = (int) (Math.atan(k) * RadToDeg);
                        lx1 = s * ((int) (L * scale * Math.cos((Angle + LA) * DegToRad)));
                        ly1 = s * ((int) (L * scale * Math.sin((Angle + LA) * DegToRad)));
                        lx2 = s * ((int) (L * scale * Math.cos((Angle - LA) * DegToRad)));
                        ly2 = s * ((int) (L * scale * Math.sin((Angle - LA) * DegToRad)));
                        Polygon P = new Polygon();
                        P.addPoint(cx, +cy);
                        P.addPoint(+cx + lx1, +cy + ly1);
                        P.addPoint(+cx + lx2, +cy + ly2);
                        g.setColor(Color.black);
                        g.fillPolygon(P);
                        int Y = 0;
                        int X = 0;
                        X = (int) ((x2 + x1) / 2);
                        X = (int) ((x2 + X) / 2);
                        Y = (int) ((y2 + y1) / 2);
                        Y = (int) ((y2 + Y) / 2);
                        if (statistic.getSumTime() > 0) {
                            String ss = "" + res[Z[i].getElementVuhod(j)][Z[i].getElementVhod(j)];
                            g.drawString((ss), X, Y);
                        }
                    } else {
                        g.setColor(line);
                        g.drawArc(x2 + (int) (r * scale) + dx2, y2 - (int) (d * scale / 4), (int) (d * scale / 2), (int) (d * scale / 2), 0, 360);
                        g.setColor(Color.black);
                        g.fillOval(x2 + (int) (r * scale) + dx2 - 3, y2 - 3, 6, 6);
                    }
                }
            }
            if (statistic.getSumTime() > 0) {
                for (i = 0; i < count; i++) {
                    for (j = 0; j < Z[i].getColVhod(); j++) {
                        A2 = dAngle * Z[i].getElementVuhod(j);
                        A1 = dAngle * Z[i].getElementVhod(j);
                        dx1 = (int) (r * scale * Math.cos(A1 * DegToRad));
                        dy1 = (int) (r * scale * Math.sin(A1 * DegToRad));
                        x1 = (int) (CenterX * scale) + (int) (Dist * scale * Math.cos(A1 * DegToRad)) + (int) (r * scale) - dx1;
                        y1 = (int) (CenterY * scale) + (int) (Dist * scale * Math.sin(A1 * DegToRad)) + (int) (r * scale) - dy1;
                        g.setColor(line);
                        g.drawArc(x1 + (int) (r * scale) + dx1, y1 - (int) (d * scale / 4), (int) (d * scale / 2), (int) (d * scale / 2), 0, 360);
                        g.setColor(Color.black);
                        g.fillOval(x1 + (int) (r * scale) + dx1 - 3, y1 - 3, 6, 6);
                        if (statistic.getSumTime() > 0) {
                            String ss = "" + res[Z[i].getElementVhod(j)][Z[i].getElementVhod(j)];
                            g.drawString((ss), x1 + (int) (r * scale) + dx1, y1 - (int) (d * scale / 4));
                        }
                    }
                }
            }
            this.setPreferredSize(new Dimension((int) (WIDTH * scale), (int) (HEIGTH * scale)));
            this.revalidate();
        }
    }
