    @Override
    public void paint(final MyGraphics g, final ZirkelCanvas zc) {
        if (!Valid || mustHide(zc)) {
            return;
        }
        final double c1 = zc.col(X1), r1 = zc.row(Y1), c2 = zc.col(X2), r2 = zc.row(Y2);
        if (visible(zc)) {
            if (isStrongSelected() && g instanceof MyGraphics13) {
                ((MyGraphics13) g).drawMarkerLine(c1, r1, c2, r2);
            }
            g.setColor(this);
            if (tracked()) {
                zc.UniversalTrack.drawTrackLine(this, c1, r1, c2, r2);
            }
            g.drawLine(c1, r1, c2, r2, this);
            if (code_symbol > 0) {
                final double rr = 7;
                final double dd = 3;
                final double ob = 2;
                final double cM = (c1 + c2) / 2, rM = (r1 + r2) / 2;
                final double A = c2 - cM, B = r2 - rM;
                final double sqrt2 = Math.sqrt(B * B + A * A);
                final double xx1 = -(rr * B) / sqrt2 + cM - ob * A / sqrt2;
                final double yy1 = (rr * A) / sqrt2 + rM - ob * B / sqrt2;
                final double xx2 = (rr * B) / sqrt2 + cM + ob * A / sqrt2;
                final double yy2 = -(rr * A) / sqrt2 + rM + ob * B / sqrt2;
                final double xt = dd * A / sqrt2, yt = dd * B / sqrt2;
                switch(code_symbol) {
                    case 1:
                        g.drawLine(xx1, yy1, xx2, yy2, this);
                        break;
                    case 2:
                        g.drawLine(xx1 - xt, yy1 - yt, xx2 - xt, yy2 - yt, this);
                        g.drawLine(xx1 + xt, yy1 + yt, xx2 + xt, yy2 + yt, this);
                        break;
                    case 3:
                        g.drawLine(xx1 - 2 * xt, yy1 - 2 * yt, xx2 - 2 * xt, yy2 - 2 * yt, this);
                        g.drawLine(xx1, yy1, xx2, yy2, this);
                        g.drawLine(xx1 + 2 * xt, yy1 + 2 * yt, xx2 + 2 * xt, yy2 + 2 * yt, this);
                        break;
                    case 4:
                        g.drawLine(xx1 - 3 * xt, yy1 - 3 * yt, xx2 - 3 * xt, yy2 - 3 * yt, this);
                        g.drawLine(xx1 - xt, yy1 - yt, xx2 - xt, yy2 - yt, this);
                        g.drawLine(xx1 + xt, yy1 + yt, xx2 + xt, yy2 + yt, this);
                        g.drawLine(xx1 + 3 * xt, yy1 + 3 * yt, xx2 + 3 * xt, yy2 + 3 * yt, this);
                        break;
                    case 5:
                        g.drawLine(xx1 - 2 * xt, yy1 - 2 * yt, xx2 + 2 * xt, yy2 + 2 * yt, this);
                        g.drawLine(xx1 + 2 * xt, yy1 + 2 * yt, xx2 - 2 * xt, yy2 - 2 * yt, this);
                        break;
                    case 6:
                        g.drawCircle(cM, rM, 2 * dd, this);
                        break;
                }
            }
            if (Arrow) {
                final double a = Math.PI * 0.9;
                final double r = zc.dx(zc.scale(Global.getParameter("arrowsize", 15)));
                final double[] cols = new double[3];
                cols[0] = c2;
                cols[1] = zc.col(X2 + (DX * Math.cos(a) + DY * Math.sin(a)) * r);
                cols[2] = zc.col(X2 + (DX * Math.cos(-a) + DY * Math.sin(-a)) * r);
                final double[] rows = new double[3];
                rows[0] = r2;
                rows[1] = zc.row(Y2 + (-DX * Math.sin(a) + DY * Math.cos(a)) * r);
                rows[2] = zc.row(Y2 + (-DX * Math.sin(-a) + DY * Math.cos(-a)) * r);
                g.fillPolygon(cols, rows, 3, true, false, this);
            }
        }
        final String s = getDisplayText();
        if (!s.equals("")) {
            g.setLabelColor(this);
            setFont(g);
            DisplaysText = true;
            if (KeepClose) {
                final double side = (YcOffset < 0) ? 1 : -1;
                drawLabel(g, s, zc, X1 + XcOffset * (X2 - X1), Y1 + XcOffset * (Y2 - Y1), side * DX, side * DY, 0, 0);
            } else {
                drawLabel(g, s, zc, (X1 + X2) / 2, (Y1 + Y2) / 2, DX, DY, XcOffset, YcOffset);
            }
        }
    }
